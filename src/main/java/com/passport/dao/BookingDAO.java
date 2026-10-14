package com.passport.dao;

import com.passport.model.Booking;
import java.sql.SQLException;
import java.util.List;

public interface BookingDAO {
    Booking createBooking(int userId, int applicationId, int slotId) throws SQLException;
    List<Booking> findByUserId(int userId) throws SQLException;
    List<Booking> findAll() throws SQLException;
    int countAll() throws SQLException;
    Booking findByReference(String bookingReference) throws SQLException;
    Booking findById(int bookingId) throws SQLException;
    boolean cancelBooking(int bookingId, int userId) throws SQLException;
    boolean updateStatus(int bookingId, String newStatus) throws SQLException;
}
