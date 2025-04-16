package com.dimu.dimuapi.service.agreement;

import ch.qos.logback.classic.net.SocketReceiver;
import com.dimu.dimuapi.Enum.*;
import com.dimu.dimuapi.dto.AgreementDto;
import com.dimu.dimuapi.dto.ApiResponseDto;
import com.dimu.dimuapi.dto.EditAgreementDto;
import com.dimu.dimuapi.exceptionshandling.CustomException;
import com.dimu.dimuapi.exceptionshandling.ResourceNotFoundException;
import com.dimu.dimuapi.model.*;
import com.dimu.dimuapi.repository.*;
import com.dimu.dimuapi.service.notification.NotificationService;
import com.dimu.dimuapi.service.payment.paystack.PaystackService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AgreementServiceImpl implements AgreementService {
    @Autowired
    UserRepository  userRepository;

    @Autowired
    GoodServicesRepository goodServicesRepository;

    @Autowired
    AgreementRepository agreementRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    WalletRepository walletRepository;

    @Autowired
    SimpMessagingTemplate messagingTemplate;

    @Autowired
    NotificationService notificationService;

    @Autowired
    EscrowAccountRepository escrowAccountRepository;

    @Autowired
    PaystackService paystackService;

    public Sort sort = Sort.by(Sort.Direction.DESC,"createdAt");

    @Override
    public ApiResponseDto createAgreementByBuyer(AgreementDto agreementDto, User user) {
        try {
          User seller = userRepository.findByEmail(agreementDto.sellerEmail())
                  .orElseThrow(() -> new ResourceNotFoundException("User", "email", agreementDto.sellerEmail()));
          if(seller.getPhoneNumber().equals(agreementDto.sellerPhoneNumber())){
              log.info("seller found by phone number successfully");
              GoodServices goodServices = new GoodServices();
              goodServices.setItemName(agreementDto.itemName());
              goodServices.setCategory(ItemCategory.valueOf(agreementDto.category()));
              goodServices.setPrice(agreementDto.price());
              goodServices.setDeliveryAddress(agreementDto.deliveryAddress());
              goodServices.setInspectionPeriod(agreementDto.inspectionPeriod());
              GoodServices savedGoodServices = goodServicesRepository.save(goodServices);

              Agreement agreement = new Agreement();
              agreement.setSeller(seller);
              agreement.setBuyer(user);
              agreement.setAmount(agreementDto.price());
              agreement.setUpfrontPayment(agreementDto.upfrontPayment());
              agreement.setApproved(true);

              Transaction transaction = new Transaction();
              transaction.setAmount(agreement.getAmount());
              transaction.setPaymentType(PaymentType.valueOf(agreementDto.paymentType()));
              transaction.setTransactionFlow(TransactionFlow.OUTGOING);
              transaction.setTransactionType(TransactionType.ESCROW);
              transaction.setStatus(TransactionStatus.PENDING);
              Transaction savedTransaction = transactionRepository.save(transaction);
              agreement.setTransaction(savedTransaction);



              log.info("transaction saved successfully");
              agreement.setGoodServices(savedGoodServices);
              log.info("good services saved successfully");
              Agreement savedAgreement = agreementRepository.save(agreement);
              log.info("agreement saved successfully");

              SocketMessage message = new SocketMessage();
              message.setTo(savedAgreement.getBuyer().getUserId());
              message.setFrom(savedAgreement.getSeller().getUserId());
              message.setSubject("Agreement Created");
              message.setContent("The agreement between " + savedAgreement.getBuyer().getFirstName() + " and "
                      + savedAgreement.getSeller().getFirstName() + " has been created successfully");

              log.info("notification sent successfully");

              messagingTemplate.convertAndSendToUser(message.getTo(),"/queue/notifications",message);

              notificationService.saveNotification(message.getSubject(),message.getContent(),user);
              log.info("notification saved successfully");
              notificationService.saveNotification(message.getSubject(),message.getContent(),seller);
              log.info("seller notif saved successfully");

              return new ApiResponseDto(true,"New Agreement created successfully",savedAgreement);
          }
          else {
              throw new CustomException("Phone number does not correlate with seller's phone number");
          }
        } catch (ResourceNotFoundException | CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Unexpected error occurred while creating agreement: " + e.getMessage());
        }
    }

    @Override
    public String acceptOrDeclineAgreement(String agreementId, boolean isAccepted, User user) {
        try {
            Agreement agreement = agreementRepository.findById(agreementId)
                    .orElseThrow(() -> new ResourceNotFoundException("Agreement", "id", agreementId));
            if(agreement.getSeller().equals(user)){
                if(agreement.isApproved()){
                    throw new CustomException("Agreement is already approved");
                }else{
                    if(isAccepted){
                        agreement.setApproved(true);

                        Transaction transaction = new Transaction();
                        transaction.setAmount(agreement.getAmount());
                        transaction.setPaymentType(PaymentType.WALLET);
                        transaction.setTransactionFlow(TransactionFlow.OUTGOING);
                        transaction.setTransactionType(TransactionType.ESCROW);
                        transaction.setStatus(TransactionStatus.PENDING);

                        agreement.setTransaction(transactionRepository.save(transaction));
                        agreementRepository.save(agreement);

                        return "Agreement accepted by second party";
                    }
                    else{
                        agreement.setApproved(false);
                        return "Agreement declined by second party";
                    }
                }
            }else
                throw new CustomException("You are not the seller of this agreement");
        }catch (ResourceNotFoundException e) {
            throw e;
        }catch (Exception e) {
            throw new CustomException("Unexpected error occurred while accepting or declining agreement: " + e.getMessage());
        }
    }

    @Override
    public ApiResponseDto getAgreementsByUser(User user){
        try{
            List<Agreement> agreements = agreementRepository.findAgreementsByBuyer(user,sort);
            return new ApiResponseDto(true,"List of agreements fetched successfully",agreements);
        }catch(Exception ex){
            throw new CustomException("An unexpected error occurred" +
                    ", could not fetch agreements made by user");
        }
    }

    @Override
    public ApiResponseDto getAgreement(User user, String agreementId) {
        try{
            Agreement agreement = agreementRepository.findByAgreementIdAndBuyer(agreementId,user)
                    .orElseThrow(() -> new ResourceNotFoundException("Agreement", "id", agreementId));
            return new ApiResponseDto(true,"Agreement fetched successfully",agreement);
        }catch (ResourceNotFoundException e){
            throw e;
        }catch(Exception e){
            throw new CustomException("An unexpected error occurred" +
                    ", could not fetch agreement with id: "+agreementId);
        }
    }

    @Override
    public ApiResponseDto editAgreement(User user, String agreementId, EditAgreementDto agreementDto) {
        try{
            Agreement agreement = agreementRepository.findByAgreementIdAndBuyer(agreementId,user)
                    .orElseThrow(() -> new ResourceNotFoundException("Agreement", "id", agreementId));
//            if(agreement.getTransaction()!=null){
//                throw new CustomException("Agreement is already approved and cannot be edited");
//            }
//            else{
                User seller = null;

// Validate and fetch seller by email or phone
                if (agreementDto.sellerEmail() != null) {
                    seller = userRepository.findByEmail(agreementDto.sellerEmail())
                            .orElseThrow(() -> new ResourceNotFoundException("User", "email", agreementDto.sellerEmail()));
                }
                if (agreementDto.sellerPhoneNumber() != null) {
                    seller = userRepository.findByPhoneNumber(agreementDto.sellerPhoneNumber())
                            .orElseThrow(() -> new ResourceNotFoundException("User", "phone number", agreementDto.sellerPhoneNumber()));
                }

// If both email and phone number are provided, ensure they match the same user
                if (agreementDto.sellerEmail() != null && agreementDto.sellerPhoneNumber() != null) {
                    if (!seller.getEmail().equals(agreementDto.sellerEmail()) || !seller.getPhoneNumber().equals(agreementDto.sellerPhoneNumber())) {
                        throw new CustomException("Email and phone number do not match the same seller");
                    }
                }

// Update agreement with the new seller
                if (seller != null) {
                    agreement.setSeller(seller);
                }

                GoodServices goodServices = agreement.getGoodServices();
                goodServices.setItemName(agreementDto.itemName()!=null?agreementDto.itemName()
                        :goodServices.getItemName());
                goodServices.setCategory(agreementDto.category()!=null?ItemCategory.valueOf(agreementDto.category())
                        :goodServices.getCategory());
                goodServices.setPrice(agreementDto.price()!=null?agreementDto.price()
                        :goodServices.getPrice());
                goodServices.setDeliveryAddress(agreementDto.deliveryAddress()!=null?agreementDto.deliveryAddress()
                        :goodServices.getDeliveryAddress());
                goodServices.setInspectionPeriod(agreementDto.inspectionPeriod()!=null?agreementDto.inspectionPeriod()
                        :goodServices.getInspectionPeriod());
                GoodServices updatedGoodServices = goodServicesRepository.save(goodServices);
                agreement.setAmount(agreementDto.price()!=null?
                        agreementDto.price():agreement.getAmount());
                agreement.setUpfrontPayment(agreementDto.upfrontPayment()!=null?
                        agreementDto.upfrontPayment():agreement.getUpfrontPayment());
                agreement.setGoodServices(updatedGoodServices);
                agreementRepository.save(agreement);
                return new ApiResponseDto(true,"Agreement edited successfully",agreement);
//            }

        }catch (ResourceNotFoundException e){
            throw e;
        }catch(Exception e){
            log.error(e.getMessage());
            throw new CustomException("An unexpected error occurred" +
                    ", could not edit agreement with id: "+agreementId);
        }
    }

    @Override
    public ApiResponseDto payForAgreement( String transactionId,String paymentType,String walletId,User user) {
        try {
            // Fetch transaction
            Transaction transaction = transactionRepository.findByTransactionId(transactionId)
                    .orElseThrow(() -> new ResourceNotFoundException("Transaction", "id", transactionId));

            // Check if already paid
            if (isAlreadyPaid(transaction)) {
                throw new CustomException("Transaction has already been paid for");
            }

            // Handle wallet payment
            if (PaymentType.valueOf(paymentType) == PaymentType.WALLET) {

                if (walletId == null) {
                    throw new CustomException("Wallet ID must be provided for wallet payment");
                }

                DiimuWallet wallet = walletRepository.findByWalletId(walletId)
                        .orElseThrow(() -> new ResourceNotFoundException("Wallet", "wallet Id", walletId));

                if(!wallet.getUser().equals(user)){
                    throw new CustomException("invalid wallet used");
                }

                if (wallet.getAccessibleBalance() >= transaction.getAmount() &&
                        transaction.getStatus() == TransactionStatus.PENDING) {
                    wallet.setAccessibleBalance(wallet.getAccessibleBalance()
                            - transaction.getAmount());
                    walletRepository.save(wallet);
                    holdInEscrow(transaction);
                    return new ApiResponseDto(true, "Transaction paid for successfully", transaction);
                } else {
                    throw new CustomException("Insufficient wallet balance or invalid transaction status");
                }
            }

            // Handle Paystack payment
            if (PaymentType.valueOf(paymentType) == PaymentType.ONLINE) {
                PaystackVerifyTransactionResponse response = paystackService.verifyTransaction(transactionId);
                double paystackAmount = response.getData().getAmount() / 100.0;

                if (Double.compare(paystackAmount, transaction.getAmount()) != 0) {
                    throw new CustomException("Transaction amount does not match Paystack transaction amount");
                }

                if (response.getData().getStatus().equals("success") && transaction.getStatus() == TransactionStatus.PENDING) {
                    holdInEscrow(transaction);
                    return new ApiResponseDto(true, "Transaction paid for successfully", transaction);
                } else {
                    throw new CustomException("Paystack transaction failed or transaction already handled");
                }
            }

            throw new CustomException("Invalid payment type passed");

        } catch (ResourceNotFoundException | CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException("An unexpected error occurred" +
                    ", could not pay for transaction with id: " + transactionId);
        }
    }


    private boolean isAlreadyPaid(Transaction transaction) {
        return transaction.getStatus() == TransactionStatus.IN_ESCROW ||
                transaction.getStatus() == TransactionStatus.COMPLETED;
    }

    private void holdInEscrow(Transaction transaction) {
        EscrowAccount escrowAccount = new EscrowAccount();
        escrowAccount.setEscrowBalance(transaction.getAmount());
        escrowAccount.setEscrowStatus(EscrowStatus.HELD);
        escrowAccount.setReleased(false);
        escrowAccount.setTransaction(transaction);
        escrowAccountRepository.save(escrowAccount);

        transaction.setStatus(TransactionStatus.IN_ESCROW);
        transactionRepository.save(transaction);
    }
}
