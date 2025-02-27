package com.dimu.dimuapi.model;

import com.dimu.dimuapi.Enum.ItemType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@Entity
@AllArgsConstructor
@Data
public class GoodServices extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String itemName;
    private String description;
    @OneToOne
    @JoinColumn(name = "transactionId", referencedColumnName = "transactionId")
    private Transaction transaction;
    @Column(nullable = false,columnDefinition = "int default 1")
    private  int quantity;
    @Enumerated(EnumType.STRING)
    private ItemType itemType;
    @Column(nullable = false)
    private double price = 0.0;

}
