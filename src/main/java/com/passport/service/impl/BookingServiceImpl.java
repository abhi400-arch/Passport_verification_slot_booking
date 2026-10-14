package com.passport.service.impl;

import com.passport.dao.BookingDAO;
import com.passport.dao.impl.BookingDAOImpl;
import com.passport.model.Booking;
import com.passport.service.BookingService;
import com.passport.service.NotificationService;
import com.passport.service.WaitlistService;

import java.time.LocalDate;
import java.util.List;

public class BookingServiceImpl implements BookingService {
    private final BookingDAO dao = new BookingDAOImpl();
    private final NotificationService notificationService = new NotificationServiceImpl();
    private final WaitlistService waitlistService = new WaitlistServiceImpl();

    @Override
    public Booking book(int userId, int applicationId, int slotId) throws Exception {
        Booking b = dao.createBooking(userId, applicationId, slotId);
        if (b != null) {
            notificationService.notify(userId,
                    "Appointment Confirmed: " + b.getBookingReference(),
                    "Your passport verification is scheduled for " + b.getSlotDate() + " (" + b.getStartTime() + " - " + b.getEndTime() + "). Access your Digital E-Pass anytime.",
                    "SUCCESS");
        }
        return b;
    }

    @Override
    public List<Booking> byUser(int userId) throws Exception {
        return dao.findByUserId(userId);
    }

    @Override
    public List<Booking> all() throws Exception {
        return dao.findAll();
    }

    @Override
    public int count() throws Exception {
        return dao.countAll();
    }

    @Override
    public Booking byReference(String ref) throws Exception {
        return dao.findByReference(ref);
    }

    @Override
    public Booking byId(int bookingId) throws Exception {
        return dao.findById(bookingId);
    }

    @Override
    public boolean cancel(int bookingId, int userId) throws Exception {
        Booking b = dao.findById(bookingId);
        if (b == null) return false;

        boolean success = dao.cancelBooking(bookingId, userId);
        if (success) {
            notificationService.notify(b.getUserId(),
                    "Appointment Cancelled",
                    "Your passport verification booking " + b.getBookingReference() + " on " + b.getSlotDate() + " has been successfully cancelled.",
                    "INFO");

            // Auto-trigger priority waitlist reallocation alert
            if (b.getSlotDate() != null) {
                try {
                    LocalDate d = LocalDate.parse(b.getSlotDate());
                    waitlistService.notifyMatchesForAvailableSlot(d, b.getStartTime(), b.getEndTime());
                } catch (Exception ignored) {}
            }
        }
        return success;
    }

    @Override
    public boolean updateStatus(int bookingId, String newStatus) throws Exception {
        return dao.updateStatus(bookingId, newStatus);
    }
}
