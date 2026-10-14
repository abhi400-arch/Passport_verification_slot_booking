package com.passport.service;

import com.passport.model.WaitlistEntry;
import java.time.LocalDate;
import java.util.List;

public interface WaitlistService {
    void joinWaitlist(int userId, LocalDate fromDate, LocalDate toDate, String urgencyCategory, String reason) throws Exception;
    List<WaitlistEntry> getWaitlistByUser(int userId) throws Exception;
    List<WaitlistEntry> getAllActiveWaitlist() throws Exception;
    boolean cancelWaitlist(int waitlistId) throws Exception;
    int countActive() throws Exception;
    void notifyMatchesForAvailableSlot(LocalDate slotDate, String startTime, String endTime);
}
