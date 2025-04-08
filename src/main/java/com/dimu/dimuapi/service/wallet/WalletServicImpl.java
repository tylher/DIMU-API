package com.dimu.dimuapi.service.wallet;

import com.dimu.dimuapi.Enum.*;
import com.dimu.dimuapi.dto.ApiResponseDto;
import com.dimu.dimuapi.exceptionshandling.CustomException;
import com.dimu.dimuapi.exceptionshandling.ResourceNotFoundException;
import com.dimu.dimuapi.model.DiimuWallet;
import com.dimu.dimuapi.model.PaystackVerifyTransactionResponse;
import com.dimu.dimuapi.model.Transaction;
import com.dimu.dimuapi.model.User;
import com.dimu.dimuapi.repository.TransactionRepository;
import com.dimu.dimuapi.repository.WalletRepository;
import com.dimu.dimuapi.service.payment.paystack.PaystackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class WalletServicImpl implements WalletService{
    @Autowired
    WalletRepository walletRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    PaystackService paystackService;

    @Override
    public DiimuWallet createWallet(User user, WalletType type) {
        try{
            if (walletRepository.existsByUserAndWalletType(user,type)){
                throw new CustomException("wallet with type, "
                        + type.toString().toLowerCase()+"has been created previously for user, "+
                        user.getFirstName());
            }else{
                DiimuWallet diimuWallet = new DiimuWallet();
                diimuWallet.setUser(user);
                diimuWallet.setWalletType(type);
              return  walletRepository.save(diimuWallet);
            }
        } catch (CustomException e) {
            log.error("Unable to create wallet: {}", e.getMessage());
            throw new CustomException("Unable to create wallet: " + e.getMessage() );

        }catch(Exception ex){
            log.error("An unknown error occured: {}", ex.getMessage());
            throw new CustomException("Unable to create wallet: " + ex.getMessage() );
        }
    }

    @Override
    public List<DiimuWallet> getWallets(User user) {
        return walletRepository.findByUser(user);
    }


    @Override
    @Transactional
    public ApiResponseDto fundWallet(String walletId, String reference ){
        try{
            DiimuWallet wallet = walletRepository.findByWalletId(walletId).orElseThrow(
                    ()-> new ResourceNotFoundException("wallet", "wallet id",walletId)
            );

            Optional<Transaction> transactionOptional = transactionRepository.findByReference(reference);
            PaystackVerifyTransactionResponse response = paystackService.verifyTransaction(reference);

            String paymentStatus = response.getData().getStatus();
            String message = "";
            if(transactionOptional.isPresent()){
                if(transactionOptional.get().getStatus().equals(TransactionStatus.PENDING)&&
                        paymentStatus.equalsIgnoreCase(PaymentStatus.COMPLETED.getStatus())){
                    Transaction transaction = transactionOptional.get();
                    wallet.setAccessibleBalance(wallet.getAccessibleBalance()+transaction.getAmount());
                    wallet.setLedgerBalance(wallet.getLedgerBalance()+transaction.getAmount());

                    transaction.setStatus(TransactionStatus.COMPLETED);
                    walletRepository.save(wallet);
                    transactionRepository.save(transaction);

                    message = "Wallet funded successfully";
                }


            }
            else {
                Transaction transaction = new Transaction();
                transaction.setTransactionFlow(TransactionFlow.INCOMING);
                transaction.setAmount((double) response.getData().getAmount() /100);
                transaction.setReference(reference);
                transaction.setTransactionType(TransactionType.WALLET_FUNDING);
                transaction.setPaymentType(PaymentType.ONLINE);



                message = verifyTransactionStatus(transaction,wallet,paymentStatus);
                transactionRepository.save(transaction);
                wallet.addTransaction(transaction);

                walletRepository.save(wallet);



            }

            return new ApiResponseDto(true,message,wallet);
        }catch (ResourceNotFoundException | CustomException e){
            throw e;
        }catch (Exception ex){
            throw new CustomException(ex.getMessage());
        }
    };

    private String verifyTransactionStatus(Transaction transaction,DiimuWallet wallet
            ,String status){
        if(status.equalsIgnoreCase(PaymentStatus.COMPLETED.getStatus())){
            transaction.setStatus(TransactionStatus.COMPLETED);
            wallet.setAccessibleBalance(wallet.getAccessibleBalance()+transaction.getAmount());
            wallet.setLedgerBalance(wallet.getLedgerBalance()+transaction.getAmount());

            return "wallet funded successfully";
        }else if (status.equalsIgnoreCase(PaymentStatus.INITIATED.getStatus())
                || status.equalsIgnoreCase(PaymentStatus.ABANDONED.getStatus())) {
            transaction.setStatus(TransactionStatus.PENDING);
            return "wallet funding is pending";
        } else {
            throw new CustomException("Paystack transaction failed");
        }

    }
}
