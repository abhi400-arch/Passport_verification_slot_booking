package com.passport.dao;

import com.passport.model.WaitlistEntry;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface WaitlistDAO {
    void create(WaitlistEntry entry) throws SQLException;
    List<WaitlistEntry> findByUserId(int userId) throws SQLException;
    List<WaitlistEntry> findAllActive() throws SQLException;
    List<WaitlistEntry> findActiveMatchingDate(LocalDate date) throws SQLException;
    boolean updateStatus(int waitlistId, String status) throws SQLException;
    int countActive() throws SQLException;
    int getQueuePosition(int waitlistId) throws SQLException;
}
