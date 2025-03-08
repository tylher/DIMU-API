package com.dimu.dimuapi.model;

import com.dimu.dimuapi.Enum.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Transaction {
    @Id
    private String id;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status = TransactionStatus.PENDING;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    private double amount;

    @Enumerated(EnumType.STRING)
    private TransactionFlow transactionFlow;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @OneToMany(mappedBy = "transaction",orphanRemoval = true)
    private List<WalletLedger> walletLedgers;

    @PrePersist
    public void generateTransactionId(){
        this.id = DiimuToken
                .generateRandomToken(TokenType.TRANSACTION_ID,TokenFormat.ALPHANUMERIC);
    }
}
