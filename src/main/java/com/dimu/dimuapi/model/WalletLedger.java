package com.dimu.dimuapi.model;

import jakarta.persistence.*;

public class WalletLedger extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String ledgerId;

    private DiimuWallet wallet;

    private double amount;
    @ManyToOne
    @JoinColumn(name = "transactionId")
    private Transaction transaction;
}
