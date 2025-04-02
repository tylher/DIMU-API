package com.dimu.dimuapi.service.notification;


import com.dimu.dimuapi.dto.ApiResponseDto;
import com.dimu.dimuapi.model.Notification;
import com.dimu.dimuapi.model.User;

import java.util.List;


public interface NotificationService {
    void saveNotification(String subject, String content, User user);
    ApiResponseDto getNotifications(User user);
    void markNotificationsAsRead(List<String> notificationIds);
}
