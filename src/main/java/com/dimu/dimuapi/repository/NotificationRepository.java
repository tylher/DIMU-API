package com.dimu.dimuapi.repository;

import com.dimu.dimuapi.model.Notification;
import com.dimu.dimuapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification,String> {
    List<Notification> findNotificationsByUser(User user);
}
