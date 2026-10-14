package com.passport.dao;

import com.passport.model.Notification;
import java.sql.SQLException;
import java.util.List;

public interface NotificationDAO {
    void create(Notification n) throws SQLException;
    List<Notification> findByUserId(int userId) throws SQLException;
    int countUnread(int userId) throws SQLException;
    boolean markAsRead(int notificationId, int userId) throws SQLException;
    boolean markAllAsRead(int userId) throws SQLException;
}
