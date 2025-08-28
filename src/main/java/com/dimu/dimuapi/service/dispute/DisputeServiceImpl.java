package com.dimu.dimuapi.service.dispute;

import com.dimu.dimuapi.Enum.AWSBucketList;
import com.dimu.dimuapi.Enum.ComplaintType;
import com.dimu.dimuapi.Enum.NotificationType;
import com.dimu.dimuapi.dto.ApiResponseDto;
import com.dimu.dimuapi.dto.CreateDisputeDto;
import com.dimu.dimuapi.exceptionshandling.CustomException;
import com.dimu.dimuapi.exceptionshandling.ResourceNotFoundException;
import com.dimu.dimuapi.model.*;
import com.dimu.dimuapi.repository.AgreementRepository;
import com.dimu.dimuapi.repository.DisputeRepository;
import com.dimu.dimuapi.repository.NotificationRepository;
import com.dimu.dimuapi.service.S3Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DisputeServiceImpl implements DisputeService{
   @Autowired
   S3Service s3Service;

   @Autowired
   DisputeRepository disputeRepository;

   @Autowired
   AgreementRepository agreementRepository;

   @Autowired
   NotificationRepository notificationRepository;

   @Autowired
   SimpMessagingTemplate messagingTemplate;


    @Override
    public ApiResponseDto createDispute(CreateDisputeDto disputeDto
            , List<MultipartFile> productImageFiles, User user) {
        try {
            Agreement agreement = agreementRepository.findById(disputeDto.agreementId())
                    .orElseThrow(()-> new ResourceNotFoundException("Agreement", "agreementId"
                    , disputeDto.agreementId()));

            Optional<Dispute> savedDispute = disputeRepository.findByAgreement(agreement);

            if(savedDispute.isPresent()){
                throw new CustomException("Dispute already raised for this agreement");
            }

            if(!agreement.getBuyer().getUserId().equals(user.getUserId())){
                throw new CustomException("You cannot create a dispute for this agreement " +
                        "as you are not the buyer");
            }
            Dispute dispute = new Dispute();
            dispute.setAgreement(agreement);
            dispute.setComplaint(disputeDto.complaint());
            dispute.setType(ComplaintType.valueOf(disputeDto.complaintType()));

            if (productImageFiles != null && !productImageFiles.isEmpty()) {
                List<String> urls = productImageFiles.stream()
                        .map(file -> {
                            try {
                                return s3Service.uploadFile(file, AWSBucketList.DIIMU_DISPUTE_BUCKET.getBucketName());
                            } catch (IOException e) {
                                throw new CustomException(e.getMessage());
                            }
                        })
                        .collect(Collectors.toList());

                log.info(String.valueOf(urls));
                dispute.setProductImages(urls);
            }

            dispute = disputeRepository.save(dispute);

            sendDisputeNotification(dispute,agreement);
            return new ApiResponseDto(true, "Dispute raised successfully", dispute);
        } catch (ResourceNotFoundException e){
                throw e;
            }
            catch (Exception e){
                    throw new CustomException(e.getMessage());
            }
    }

    public ApiResponseDto getDisputeByAgreement(String agreementId){
        try{
            Agreement agreement = agreementRepository.findById(agreementId)
                    .orElseThrow(()-> new ResourceNotFoundException("Agreement", "agreementId", agreementId));
            Dispute dispute = disputeRepository.findByAgreement(agreement)
                    .orElseThrow(()-> new ResourceNotFoundException("Dispute", "agreementId", agreementId));
            return new ApiResponseDto(true, "Dispute fetched successfully", dispute);
        }catch (ResourceNotFoundException e){
            throw e;
        }catch (Exception e){
            throw new CustomException(e.getMessage());
        }
    }


    private void sendDisputeNotification(Dispute dispute,Agreement agreement){
        SocketMessage baseMessage = new SocketMessage();
        baseMessage.setSubject("Dispute Raised");
        SocketMessage.Metadata metadata = new SocketMessage.Metadata();
        baseMessage.setData(metadata);
        baseMessage.setTo(agreement.getSeller().getUserId());
        baseMessage.setFrom(agreement.getBuyer().getUserId());
        baseMessage.setContent("A dispute has been raised for your agreement with "
                + agreement.getBuyer().getFirstName()
                + " Please go through the details below.");
        messagingTemplate.convertAndSendToUser(baseMessage.getTo(), "/queue/notifications", baseMessage);

        Notification notification = new Notification();
        notification.setSubject("Dispute Raised");
        notification.setContent("A dispute has been raised for your agreement with "
                + agreement.getSeller().getFirstName()
                + " Please go through the details below.");
        notification.setUser(agreement.getSeller());
        notification.setType(NotificationType.DISPUTE);
        notification.setAgreementId(agreement.getAgreementId());
        notificationRepository.save(notification);
    }

}
