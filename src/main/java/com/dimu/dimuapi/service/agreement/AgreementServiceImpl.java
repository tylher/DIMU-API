package com.dimu.dimuapi.service.agreement;

import com.dimu.dimuapi.Enum.*;
import com.dimu.dimuapi.dto.AgreementDto;
import com.dimu.dimuapi.dto.ApiResponseDto;
import com.dimu.dimuapi.exceptionshandling.CustomException;
import com.dimu.dimuapi.exceptionshandling.ResourceNotFoundException;
import com.dimu.dimuapi.model.Agreement;
import com.dimu.dimuapi.model.GoodServices;
import com.dimu.dimuapi.model.Transaction;
import com.dimu.dimuapi.model.User;
import com.dimu.dimuapi.repository.AgreementRepository;
import com.dimu.dimuapi.repository.GoodServicesRepository;
import com.dimu.dimuapi.repository.TransactionRepository;
import com.dimu.dimuapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgreementServiceImpl implements AgreementService {
    @Autowired
    UserRepository  userRepository;

    @Autowired
    GoodServicesRepository goodServicesRepository;

    @Autowired
    AgreementRepository agreementRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public ApiResponseDto createAgreementByBuyer(AgreementDto agreementDto, User user) {
        try {
          User seller = userRepository.findByEmail(agreementDto.sellerEmail())
                  .orElseThrow(() -> new ResourceNotFoundException("User", "email", agreementDto.sellerEmail()));
          if(seller.equals(userRepository.findByPhoneNumber(agreementDto.sellerPhoneNumber()).get())){
              GoodServices goodServices = new GoodServices();
              goodServices.setItemName(agreementDto.itemName());
              goodServices.setCategory(ItemCategory.valueOf(agreementDto.category()));
              goodServices.setPrice(agreementDto.price());
              goodServices.setDeliveryAddress(agreementDto.deliveryAddress());
              goodServices.setInspectionPeriod(agreementDto.inspectionPeriod());
              goodServicesRepository.save(goodServices);

              Agreement agreement = new Agreement();
              agreement.setSeller(seller);
              agreement.setBuyer(user);
              agreement.setAmount(agreementDto.price());
              agreement.setUpfrontPayment(agreementDto.upfrontPayment());
              agreement.setApproved(true);

              Transaction transaction = new Transaction();
              transaction.setAmount(agreement.getAmount());
              transaction.setPaymentType(PaymentType.WALLET);
              transaction.setTransactionFlow(TransactionFlow.OUTGOING);
              transaction.setTransactionType(TransactionType.ESCROW);
              transaction.setStatus(TransactionStatus.PENDING);

              agreement.setTransaction(transactionRepository.save(transaction));


              agreement.setGoodServices(goodServices);
              return new ApiResponseDto(true,"New Agreement created successfully",agreementRepository.save(agreement));
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
            List<Agreement> agreements = agreementRepository.findAgreementsByBuyer(user);
            return new ApiResponseDto(true,"List of agreements fetched successfully",agreements);
        }catch(Exception ex){
            throw new CustomException("An unexpected error occurred" +
                    ", could not fetch agreements made by user");
        }
    }
}
