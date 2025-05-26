package com.dimu.dimuapi.model;

import com.dimu.dimuapi.Enum.DeliveryMethod;
import com.dimu.dimuapi.Enum.ItemCategory;
import com.dimu.dimuapi.Enum.ItemCondition;
import com.dimu.dimuapi.Enum.ItemType;
import com.dimu.dimuapi.exceptionshandling.CustomException;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Slf4j
public class GoodServices extends BaseEntity{


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String itemName;
    private String description;
    @OneToOne
    @JoinColumn(name = "agreementId", referencedColumnName = "agreementId")
    @JsonBackReference
    private Agreement agreement;
    @Column(nullable = false,columnDefinition = "int default 1")
    private  int quantity;
    @Enumerated(EnumType.STRING)
    private ItemType itemType;
    @Column(nullable = false)
    private double price = 0.0;
    @Enumerated(EnumType.STRING)
    private ItemCategory category;
    @Enumerated(EnumType.STRING)
    private ItemCondition itemCondition = ItemCondition.NEW;
    private String deliveryAddress;
    private String deliveryStatus;
    private int inspectionPeriod;
    @Enumerated(EnumType.STRING)
    private DeliveryMethod deliveryMethod;
    private String proofOfAutheticity;
    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean additionalItems = false;
    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean proofOfAuthenticationExists;



//    public List<String> getAdditionalItems(){
//        try{
//            return new Gson().fromJson(additionalItems, new TypeToken<List<String>>() {
//            }.getType());
//        }catch (Exception ex){
//            log.error("JSON reading error {}", ex.getMessage());
//            throw new CustomException("JSON reading error "+ex.getMessage());
//        }
//    }
//
//    public void setAdditionalItems(List<String> items){
//        try{
//            additionalItems = new Gson().toJson(items);
//        } catch (Exception e) {
//            throw new CustomException("JSON reading error "+e.getMessage());
//        }
//    }

}
