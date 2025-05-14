package com.dimu.dimuapi.service.notification;

import com.dimu.dimuapi.Enum.MessageStatus;
import com.dimu.dimuapi.Enum.NotificationType;
import com.dimu.dimuapi.dto.ApiResponseDto;
import com.dimu.dimuapi.exceptionshandling.CustomException;
import com.dimu.dimuapi.model.Agreement;
import com.dimu.dimuapi.model.Notification;
import com.dimu.dimuapi.model.User;
import com.dimu.dimuapi.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.dimu.dimuapi.constant.ApplicationConstants.createdAtSort;

@Service
public class NotificationServiceImpl implements NotificationService{
    @Autowired
    NotificationRepository notificationRepository;



    @Override
    public void saveNotification(String subject, String content, User user, Agreement agreement,String party) {
        try{
            Notification notification = new Notification();
            notification.setSubject(subject);
            notification.setContent(content);
            notification.setUser(user);
            notification.setType(NotificationType.TRANSACTION);
            notification.setAgreementId(agreement.getAgreementId());
            notification.setParty(party);
            notificationRepository.save(notification);
        }catch (Exception e){
            throw new CustomException(e.getMessage());
        }

    }


    @Override
    public ApiResponseDto getNotifications(User user) {
        try{
            List<Notification> notifications =  notificationRepository.findNotificationsByUser(user,createdAtSort);
            return new ApiResponseDto(true,"Notifications fetched successfully",notifications);
        }catch (Exception e){
            throw new CustomException("Error getting noifications for user: "+ e.getMessage());
        }
    }

    @Override
    @Transactional
    public void markNotificationsAsRead(List<String> notificationIds) {
        try{
            notificationIds.forEach(notificationId -> notificationRepository.findById(notificationId)
                    .ifPresent(notification -> notification.setStatus(MessageStatus.READ)));
        }catch (Exception e){
            throw new CustomException("Error marking notifications as read: "+ e.getMessage());
        }
    }


}
