package com.dimu.dimuapi.model;

import com.dimu.dimuapi.Enum.WalletType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DiimuWallet extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String walletId;

    @OneToOne
    @JoinColumn(name = "userId")
    private User user;

    @OneToMany
    @JoinColumn(name = "walletId")
    private List<Transaction> transactions;

    @Enumerated(EnumType.STRING)
    private WalletType walletType;

    private double accessibleBalance = 0.0;

    private double ledgerBalance = 0.0;
}
