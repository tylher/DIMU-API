package com.dimu.dimuapi.service.goodservice;

import com.dimu.dimuapi.Enum.*;
import com.dimu.dimuapi.dto.ApiResponseDto;
import com.dimu.dimuapi.dto.CreateGoodServiceDto;
import com.dimu.dimuapi.exceptionshandling.CustomException;
import com.dimu.dimuapi.exceptionshandling.ResourceNotFoundException;
import com.dimu.dimuapi.model.*;
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
    public ApiResponseDto updateDeliveryStatus(String goodServiceId, String deliveryStatus,String riderNumber) {
        try{
            GoodServices goodServices = goodServicesRepository.findById(goodServiceId)
                    .orElseThrow(() -> new ResourceNotFoundException("Good or service", "id",goodServiceId));
            if(riderNumber != null){
                goodServices.setRiderNumber(riderNumber);
            }
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
            notification.setUser(agreement.getBuyer());
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

    @Override
    public ApiResponseDto confirmDelivery(String goodServiceId, User user) {
        try{
            GoodServices goodServices = goodServicesRepository.findById(goodServiceId)
                    .orElseThrow(() -> new ResourceNotFoundException("Good or service", "id", goodServiceId));
            String buyerId = goodServices.getAgreement().getBuyer().getUserId();
            String userId = user.getUserId();
            if(!buyerId.equals(userId)){
                throw new CustomException("You are not authorized to confirm delivery for this good or service");
            }
            goodServices.setDeliveryConfirmed(true);

            goodServicesRepository.save(goodServices);
            Agreement agreement = goodServices.getAgreement();

            SocketMessage baseMessage = new SocketMessage();
            baseMessage.setSubject("Delivery Confirmed");
            SocketMessage.Metadata metadata = new SocketMessage.Metadata();
            metadata.setGoodServiceId(goodServiceId);
            baseMessage.setData(metadata);
            baseMessage.setTo(agreement.getSeller().getUserId());
            baseMessage.setFrom(agreement.getBuyer().getUserId());
            baseMessage.setContent("The delivery for your agreement with "
                    + agreement.getBuyer().getFirstName()
                    + " has been confirmed");
            messagingTemplate.convertAndSendToUser(baseMessage.getTo(), "/queue/notifications", baseMessage);

            Notification notification = new Notification();
            notification.setSubject("Delivery Confirmed");
            notification.setContent("The delivery for your agreement with "
                    + agreement.getBuyer().getFirstName()
                    + " has been confirmed");
            notification.setUser(agreement.getSeller());
            notification.setType(NotificationType.TRANSACTION);
            notification.setAgreementId(agreement.getAgreementId());
            notificationRepository.save(notification);
            return new ApiResponseDto(true,"Delivery confirmed successfully",goodServices);
        }
        catch (Exception ex){
            log.error(ex.getMessage());
            throw new CustomException("Error updating good or service: " + ex.getMessage());
        }
    }


}
