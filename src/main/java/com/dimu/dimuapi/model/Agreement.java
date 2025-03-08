package com.dimu.dimuapi.model;

import com.dimu.dimuapi.Enum.PaymentType;
import com.dimu.dimuapi.Enum.TransactionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Agreement extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String agreementId;

    @OneToOne(mappedBy = "agreement")
    private GoodServices goodServices;

    @OneToOne
    @JoinColumn(name = "transactionId")
    private Transaction transaction;

    @OneToOne
    @JoinColumn(name = "buyerId", referencedColumnName = "userId")
    private User buyer;

    @OneToOne
    @JoinColumn(name = "sellerId", referencedColumnName = "userId")
    private User seller;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    private double upfrontPayment=1.0;

    private double amount;

    private boolean isApproved=false;

    private LocalDateTime completedAt;



    public void setGoodServices(GoodServices goodServices) {
        this.goodServices = goodServices;
        if (goodServices != null && goodServices.getAgreement() != this) {
            goodServices.setAgreement(this);
        }
    }
}
