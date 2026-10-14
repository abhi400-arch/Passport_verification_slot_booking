package com.passport.service;

import com.passport.model.Notification;
import java.util.List;

public interface NotificationService {
    void notify(int userId, String title, String message, String type);
    List<Notification> getNotifications(int userId) throws Exception;
    int getUnreadCount(int userId) throws Exception;
    boolean markAsRead(int notificationId, int userId) throws Exception;
    boolean markAllAsRead(int userId) throws Exception;
}
