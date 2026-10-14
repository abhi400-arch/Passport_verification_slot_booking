package com.passport.service.impl;

import com.passport.dao.WaitlistDAO;
import com.passport.dao.impl.WaitlistDAOImpl;
import com.passport.model.WaitlistEntry;
import com.passport.service.NotificationService;
import com.passport.service.WaitlistService;
import java.time.LocalDate;
import java.util.List;

public class WaitlistServiceImpl implements WaitlistService {
    private final WaitlistDAO dao = new WaitlistDAOImpl();
    private final NotificationService notificationService = new NotificationServiceImpl();

    @Override
    public void joinWaitlist(int userId, LocalDate fromDate, LocalDate toDate, String urgencyCategory, String reason) throws Exception {
        if (fromDate == null || toDate == null) {
            throw new IllegalArgumentException("Preferred date range is required.");
        }
        if (toDate.isBefore(fromDate)) {
            throw new IllegalArgumentException("End date cannot be earlier than start date.");
        }
        if (fromDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Waitlist dates must be today or in the future.");
        }

        WaitlistEntry entry = new WaitlistEntry(userId, fromDate, toDate, urgencyCategory, reason);
        dao.create(entry);

        notificationService.notify(userId,
                "Priority Waitlist Registered",
                "Your request for " + entry.getCategoryLabel() + " (" + fromDate + " to " + toDate + ") has been placed in the priority queue at position #" + entry.getQueuePosition() + ".",
                "INFO");
    }

    @Override
    public List<WaitlistEntry> getWaitlistByUser(int userId) throws Exception {
        return dao.findByUserId(userId);
    }

    @Override
    public List<WaitlistEntry> getAllActiveWaitlist() throws Exception {
        return dao.findAllActive();
    }

    @Override
    public boolean cancelWaitlist(int waitlistId) throws Exception {
        return dao.updateStatus(waitlistId, "CANCELLED");
    }

    @Override
    public int countActive() throws Exception {
        return dao.countActive();
    }

    @Override
    public void notifyMatchesForAvailableSlot(LocalDate slotDate, String startTime, String endTime) {
        try {
            List<WaitlistEntry> matches = dao.findActiveMatchingDate(slotDate);
            for (WaitlistEntry w : matches) {
                notificationService.notify(w.getUserId(),
                        "⚡ Tatkal Alert: Slot Available on " + slotDate,
                        "A verification appointment slot on " + slotDate + " (" + startTime + " - " + endTime + ") has just become available! Hurry to Book Slot to claim your appointment.",
                        "URGENT");
            }
        } catch (Exception e) {
            System.err.println("Failed to match waitlist: " + e.getMessage());
        }
    }
}
