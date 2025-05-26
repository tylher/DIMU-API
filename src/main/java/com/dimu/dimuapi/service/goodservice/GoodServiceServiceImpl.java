package com.dimu.dimuapi.service.goodservice;

import com.dimu.dimuapi.Enum.*;
import com.dimu.dimuapi.dto.ApiResponseDto;
import com.dimu.dimuapi.dto.CreateGoodServiceDto;
import com.dimu.dimuapi.exceptionshandling.CustomException;
import com.dimu.dimuapi.exceptionshandling.ResourceNotFoundException;
import com.dimu.dimuapi.model.Agreement;
import com.dimu.dimuapi.model.GoodServices;
import com.dimu.dimuapi.model.Notification;
import com.dimu.dimuapi.model.SocketMessage;
import com.dimu.dimuapi.repository.AgreementRepository;
import com.dimu.dimuapi.repository.GoodServicesRepository;
import com.dimu.dimuapi.repository.NotificationRepository;
import com.dimu.dimuapi.service.S3Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class GoodServiceServiceImpl implements GoodServiceService{

    @Autowired
    S3Service s3Service;

    @Autowired
    AgreementRepository agreementRepository;

    @Autowired
    GoodServicesRepository goodServicesRepository;

    @Autowired
    SimpMessagingTemplate messagingTemplate;

    @Autowired
    NotificationRepository notificationRepository;

    @Override
    public ApiResponseDto createGoodService(CreateGoodServiceDto createGoodServiceDto){

        try{
            GoodServices goodServices = new GoodServices();


            goodServices.setDeliveryMethod(DeliveryMethod.valueOf(createGoodServiceDto.deliveryMethod()));
            goodServices.setItemCondition(ItemCondition.valueOf(createGoodServiceDto.itemCondition()));
            goodServices.setCategory(ItemCategory.valueOf(createGoodServiceDto.category()));
            goodServices.setProofOfAuthenticationExists(createGoodServiceDto.proofOfAuthenticationExists() != null && Boolean.parseBoolean(createGoodServiceDto.proofOfAuthenticationExists()));
            goodServices.setAdditionalItems(createGoodServiceDto.additionalItems());
            return new ApiResponseDto(true,"Good or service created successfully",goodServicesRepository.save(goodServices));
        }
        catch (Exception ex){
            log.error(ex.getMessage());
            throw new CustomException("Error creating good or service: " + ex.getMessage());
        }
    }

    @Override
    public ApiResponseDto updateDeliveryStatus(String goodServiceId, String deliveryStatus) {
        try{
            GoodServices goodServices = goodServicesRepository.findById(goodServiceId)
                    .orElseThrow(() -> new ResourceNotFoundException("Good or service", "id", goodServiceId));
            goodServices.setDeliveryStatus(DeliveryStatus.valueOf(deliveryStatus));
            Agreement agreement = goodServices.getAgreement();
            SocketMessage baseMessage = new SocketMessage();
            baseMessage.setSubject("Delivery Status Updated");
            SocketMessage.Metadata metadata = new SocketMessage.Metadata();
            metadata.setGoodServiceId(goodServiceId);
            baseMessage.setData(metadata);
            baseMessage.setTo(agreement.getBuyer().getUserId());
            baseMessage.setFrom(agreement.getSeller().getUserId());
            baseMessage.setContent("The delivery status for your agreement with "
                    + agreement.getSeller().getFirstName()
                    + " has been updated. Kindly review the current status.");
            messagingTemplate.convertAndSendToUser(baseMessage.getTo(), "/queue/notifications", baseMessage);

            Notification notification = new Notification();
            notification.setSubject("Delivery Status Updated");
            notification.setContent("The delivery status for your agreement with "
                    + agreement.getSeller().getFirstName()
                    + " has been updated. Kindly review the current status.");
            notification.setUser(agreement.getSeller());
            notification.setType(NotificationType.TRANSACTION);
            notification.setAgreementId(agreement.getAgreementId());
            notificationRepository.save(notification);

            return new ApiResponseDto(true, "Delivery status updated successfully"
                    ,agreementRepository.save(agreement));
        }
        catch (Exception ex){
            log.error(ex.getMessage());
            throw new CustomException("Error updating good or service: " + ex.getMessage());
        }
    }
}
