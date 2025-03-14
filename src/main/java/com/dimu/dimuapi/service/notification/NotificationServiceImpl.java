package com.dimu.dimuapi.service.notification;

import com.dimu.dimuapi.dto.ApiResponseDto;
import com.dimu.dimuapi.exceptionshandling.CustomException;
import com.dimu.dimuapi.model.Notification;
import com.dimu.dimuapi.model.User;
import com.dimu.dimuapi.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService{
    @Autowired
    NotificationRepository notificationRepository;



    @Override
    public void saveNotification(String subject, String content, User user) {
        try{
            Notification notification = new Notification();
            notification.setSubject(subject);
            notification.setContent(content);
            notification.setUser(user);
            notificationRepository.save(notification);
        }catch (Exception e){
            throw new CustomException(e.getMessage());
        }

    }

    @Override
    public ApiResponseDto getNotifications(User user) {
        try{
            List<Notification> notifications =  notificationRepository.findNotificationsByUser(user);
            return new ApiResponseDto(true,"Notifications fetched successfully",notifications);
        }catch (Exception e){
            throw new CustomException("Error getting noifications for user: "+ e.getMessage());
        }
    }


}
