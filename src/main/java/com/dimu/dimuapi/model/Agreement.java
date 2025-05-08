package com.dimu.dimuapi.model;

import com.dimu.dimuapi.Enum.PaymentType;
import com.dimu.dimuapi.Enum.TransactionStatus;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @JsonManagedReference
    private GoodServices goodServices;

    @OneToOne
    @JoinColumn(name = "transactionId")
    private Transaction transaction;

    @ManyToOne
    @JoinColumn(name = "buyerId", referencedColumnName = "userId")
    private User buyer;

    @ManyToOne
    @JoinColumn(name = "sellerId", referencedColumnName = "userId")
    private User seller;

//    @Enumerated(EnumType.STRING)
//    private PaymentType paymentType;

    private double upfrontPayment=1.0;

    private double amount;

    private boolean isApproved=false;

    private LocalDateTime completedAt;

    private String initiatedBy;



    public void setGoodServices(GoodServices goodServices) {
        this.goodServices = goodServices;
        if (goodServices != null && goodServices.getAgreement() != this) {
            goodServices.setAgreement(this);
        }
    }
}
