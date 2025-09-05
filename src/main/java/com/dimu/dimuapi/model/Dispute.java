package com.dimu.dimuapi.model;

import com.dimu.dimuapi.Enum.ComplaintType;
import com.dimu.dimuapi.exceptionshandling.CustomException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Slf4j
public class Dispute extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToOne
    @JoinColumn(name = "agreement_id")
    private Agreement agreement;

    @Enumerated(EnumType.STRING)
    private ComplaintType type;

    private String complaint;

    private String productImages;

    public List<String> getProductImages(){
        try{
            return new Gson().fromJson(productImages, new TypeToken<List<String>>() {
            }.getType());
        }catch (Exception ex){
            log.error("JSON reading error {}", ex.getMessage());
            throw new CustomException("JSON reading error "+ex.getMessage());
        }
    }

    public void setProductImages(List<String> images){
        try{
            productImages = new Gson().toJson(images);
        } catch (Exception e) {
            throw new CustomException("JSON reading error "+e.getMessage());
        }
    }
}
