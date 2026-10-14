package com.passport.service.impl;

import com.passport.dao.NotificationDAO;
import com.passport.dao.impl.NotificationDAOImpl;
import com.passport.model.Notification;
import com.passport.service.NotificationService;
import java.util.Collections;
import java.util.List;

public class NotificationServiceImpl implements NotificationService {
    private final NotificationDAO dao = new NotificationDAOImpl();

    @Override
    public void notify(int userId, String title, String message, String type) {
        try {
            dao.create(new Notification(userId, title, message, type));
        } catch (Exception e) {
            System.err.println("Failed to save notification: " + e.getMessage());
        }
    }

    @Override
    public List<Notification> getNotifications(int userId) throws Exception {
        return dao.findByUserId(userId);
    }

    @Override
    public int getUnreadCount(int userId) throws Exception {
        return dao.countUnread(userId);
    }

    @Override
    public boolean markAsRead(int notificationId, int userId) throws Exception {
        return dao.markAsRead(notificationId, userId);
    }

    @Override
    public boolean markAllAsRead(int userId) throws Exception {
        return dao.markAllAsRead(userId);
    }
}
