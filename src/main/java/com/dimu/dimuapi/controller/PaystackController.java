package com.dimu.dimuapi.controller;

import com.dimu.dimuapi.dto.PaystackInitializeRequest;
import com.dimu.dimuapi.model.PaystackBankList;
import com.dimu.dimuapi.model.PaystackInitiateTransactionResponse;
import com.dimu.dimuapi.model.PaystackVerifyAccount;
import com.dimu.dimuapi.model.PaystackVerifyTransactionResponse;
import com.dimu.dimuapi.service.payment.paystack.PaystackService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/paystack")
public class PaystackController {
    @Autowired
    private PaystackService paystackService;

    @PostMapping("/initialize")
    public ResponseEntity<PaystackInitiateTransactionResponse>initiateTransaction(
           @Valid @RequestBody PaystackInitializeRequest paystackInitializeRequest) throws Exception {
        PaystackInitiateTransactionResponse response = paystackService.initializeTransaction(paystackInitializeRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/verify/{reference}")
    public ResponseEntity<PaystackVerifyTransactionResponse>verifyTransaction(@PathVariable String reference) throws Exception {
        PaystackVerifyTransactionResponse response = paystackService.verifyTransaction(reference);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/banks")
    public ResponseEntity<PaystackBankList>getBankList() throws Exception {
        PaystackBankList response = paystackService.getBankList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/verify-account")
    public ResponseEntity<PaystackVerifyAccount>verifyAccount(@RequestParam String account_number, @RequestParam String code) throws Exception {
        PaystackVerifyAccount response = paystackService.verifyAccount(account_number, code);
        return ResponseEntity.ok(response);
    }
}
