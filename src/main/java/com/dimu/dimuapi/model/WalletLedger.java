package com.dimu.dimuapi.model;

import com.dimu.dimuapi.Enum.EntryType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class WalletLedger extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String ledgerId;

    @ManyToOne
    @JoinColumn(name = "walletId")
    private DiimuWallet wallet;

    private double amount;

    @ManyToOne
    @JoinColumn(name = "transactionId")
    private Transaction transaction;

    @Enumerated(EnumType.STRING)
    private EntryType entryType;
}
