package com.dimu.dimuapi.model;

import com.dimu.dimuapi.Enum.WalletType;
import jakarta.persistence.*;

public class DiimuWallet extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String walletId;

    @OneToOne
    @JoinColumn(name = "userId")
    private User user;

    private WalletType walletType;

    private double accessibleBalance;

    private double ledgerBalance;
}
