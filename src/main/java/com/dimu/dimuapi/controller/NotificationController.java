package com.dimu.dimuapi.controller;

import com.dimu.dimuapi.dto.ApiResponseDto;
import com.dimu.dimuapi.model.User;
import com.dimu.dimuapi.service.notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    @Autowired
    NotificationService notificationService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @GetMapping()
    public ResponseEntity<ApiResponseDto> getUserNotifications(@AuthenticationPrincipal User user) {
        ApiResponseDto response = notificationService.getNotifications(user);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @MessageMapping("/markNotificationsAsRead")
    @Transactional
    public void markNotificationsAsRead(List<String> notificationIds) {
        if(notificationIds!=null && !notificationIds.isEmpty()){
            notificationService.markNotificationsAsRead(notificationIds);
        }
    }



}
