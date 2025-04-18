package com.dimu.dimuapi.service.payment.paystack;

import com.dimu.dimuapi.dto.InitiateTransferDto;
import com.dimu.dimuapi.dto.PaystackCreateTransferRecipientDto;
import com.dimu.dimuapi.dto.PaystackInitializeRequest;
import com.dimu.dimuapi.exceptionshandling.CustomException;
import com.dimu.dimuapi.model.*;
import com.dimu.dimuapi.service.payment.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Slf4j
public class PaystackService implements PaymentService {
    @Value("${paystack.secretKey}")
    private String secretKey;


    public PaystackInitiateTransactionResponse initializeTransaction(PaystackInitializeRequest request) {
        try{
            WebClient webClient = WebClient.builder()
                    .baseUrl("https://api.paystack.co")
                    .defaultHeader("Authorization", "Bearer "+secretKey)
                    .build();

            PaystackInitiateTransactionResponse response =  webClient.post()
                    .uri("/transaction/initialize")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(PaystackInitiateTransactionResponse.class).block();

            assert response != null;
            log.info(response.getData().getAuthorizationUrl());
            return  response;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public PaystackVerifyTransactionResponse verifyTransaction(String ref){
        try{
            WebClient client = WebClient
                    .builder()
                    .defaultHeader("Authorization", "Bearer "+secretKey)
                    .baseUrl("https://api.paystack.co")
                    .build();

            PaystackVerifyTransactionResponse response =  client.get()
                    .uri("transaction/verify/"+ref)
                    .retrieve()
                    .bodyToMono(PaystackVerifyTransactionResponse.class).block();

            return response;
        } catch (Exception e) {
            throw new CustomException(e.getMessage());
        }
    }

    public PaystackBankList getBankList(){
        try{
        WebClient client = WebClient
                .builder()
                .defaultHeader("Authorization", "Bearer "+secretKey)
                .baseUrl("https://api.paystack.co")
                .build();

        PaystackBankList response =  client.get()
                .uri("/bank")
                .retrieve()
                .bodyToMono(PaystackBankList.class).block();

        return response;
        } catch (Exception e) {
            throw new CustomException(e.getMessage());
        }
    }

    public PaystackVerifyAccount verifyAccount(String account_number, String code) throws Exception{
        try{
            WebClient client = WebClient
                    .builder()
                    .defaultHeader("Authorization", "Bearer "+secretKey)
                    .baseUrl("https://api.paystack.co")
                    .build();

            PaystackVerifyAccount response =  client.get()
                    .uri("/bank/resolve?account_number="+account_number+"&bank_code="+code)
                    .retrieve()
                    .bodyToMono(PaystackVerifyAccount.class).block();

            return response;
        } catch (Exception e) {
            throw new CustomException(e.getMessage());
        }
    }

    public PaystackTransferRecipient createTransferRecipient(PaystackCreateTransferRecipientDto request) throws Exception{
        try{
            WebClient client = WebClient
                    .builder()
                    .defaultHeader("Authorization", "Bearer "+secretKey)
                    .baseUrl("https://api.paystack.co")
                    .build();

            PaystackTransferRecipient response =  client.post()
                    .uri("/transferrecipient")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(PaystackTransferRecipient.class).block();

            return response;
        } catch (Exception e) {
            throw new CustomException(e.getMessage());
        }
    }

    public PaystackInitiateTransferResponse initiateTransfer(InitiateTransferDto request) throws Exception{
        try{
            WebClient client = WebClient
                    .builder()
                    .defaultHeader("Authorization", "Bearer "+secretKey)
                    .baseUrl("https://api.paystack.co")
                    .build();

            PaystackInitiateTransferResponse response =  client.post()
                    .uri("/transfer")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(PaystackInitiateTransferResponse.class).block();

            return response;
        } catch (Exception e) {
            throw new CustomException(e.getMessage());
        }
    }

    public PaystackFetchTransferResponse fetchTransfer(String transferCode) throws Exception{
        try{
            WebClient client = WebClient
                    .builder()
                    .defaultHeader("Authorization", "Bearer "+secretKey)
                    .baseUrl("https://api.paystack.co")
                    .build();

            PaystackFetchTransferResponse response =  client.get()
                    .uri("/transfer/"+transferCode)
                    .retrieve()
                    .bodyToMono(PaystackFetchTransferResponse.class).block();

            return response;
        } catch (Exception e) {
            throw new CustomException(e.getMessage());
        }
    }


}
