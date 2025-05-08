package com.dimu.dimuapi.model;

import com.dimu.dimuapi.Enum.EscrowStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EscrowAccount extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String escrowId;

    private double escrowBalance;

    @Enumerated(EnumType.STRING)
    private EscrowStatus escrowStatus;


    private boolean isReleased;

    private LocalDateTime releasedAt;

    @OneToOne
    @JoinColumn(name = "transactionId")
    private Transaction transaction;
}
