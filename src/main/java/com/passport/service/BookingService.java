package com.passport.service;

import com.passport.model.Booking;
import java.util.List;

public interface BookingService {
    Booking book(int userId, int applicationId, int slotId) throws Exception;
    List<Booking> byUser(int userId) throws Exception;
    List<Booking> all() throws Exception;
    int count() throws Exception;
    Booking byReference(String ref) throws Exception;
    Booking byId(int bookingId) throws Exception;
    boolean cancel(int bookingId, int userId) throws Exception;
    boolean updateStatus(int bookingId, String newStatus) throws Exception;
}
