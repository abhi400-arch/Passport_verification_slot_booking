package com.passport.dao.impl;

import com.passport.config.DBConnection;
import com.passport.dao.BookingDAO;
import com.passport.model.Booking;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BookingDAOImpl implements BookingDAO {
    private Booking map(ResultSet rs) throws SQLException {
        Booking b = new Booking();
        b.setBookingId(rs.getInt("booking_id"));
        b.setUserId(rs.getInt("user_id"));
        b.setApplicationId(rs.getInt("application_id"));
        b.setSlotId(rs.getInt("slot_id"));
        b.setBookingReference(rs.getString("booking_reference"));
        b.setBookingStatus(rs.getString("booking_status"));
        b.setApplicantName(rs.getString("full_name"));
        b.setPassportNumber(rs.getString("passport_number"));
        b.setSlotDate(rs.getString("slot_date"));
        b.setStartTime(rs.getString("start_time"));
        b.setEndTime(rs.getString("end_time"));
        Timestamp ts = rs.getTimestamp("booked_at");
        if (ts != null) {
            b.setBookedAt(ts.toLocalDateTime());
        }
        return b;
    }

    @Override
    public Booking createBooking(int userId, int applicationId, int slotId) throws SQLException {
        String ref = "PSV-" + UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase();
        try (Connection c = DBConnection.getConnection()) {
            c.setAutoCommit(false);
            try {
                String lock = "SELECT slot_id, capacity, booked_count, status FROM slots WHERE slot_id=? FOR UPDATE";
                try (PreparedStatement ps = c.prepareStatement(lock)) {
                    ps.setInt(1, slotId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next() || !"AVAILABLE".equals(rs.getString("status")) || rs.getInt("booked_count") >= rs.getInt("capacity")) {
                            throw new SQLException("This slot is no longer available.");
                        }
                    }
                }
                String dup = "SELECT 1 FROM bookings WHERE user_id=? AND booking_status='CONFIRMED' LIMIT 1";
                try (PreparedStatement ps = c.prepareStatement(dup)) {
                    ps.setInt(1, userId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            throw new SQLException("You already have a confirmed booking. Cancel the existing booking before booking another slot.");
                        }
                    }
                }
                String insert = "INSERT INTO bookings(user_id, application_id, slot_id, booking_reference, booking_status) VALUES(?,?,?,?,'CONFIRMED')";
                try (PreparedStatement ps = c.prepareStatement(insert)) {
                    ps.setInt(1, userId);
                    ps.setInt(2, applicationId);
                    ps.setInt(3, slotId);
                    ps.setString(4, ref);
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = c.prepareStatement("UPDATE slots SET booked_count=booked_count+1 WHERE slot_id=?")) {
                    ps.setInt(1, slotId);
                    ps.executeUpdate();
                }
                c.commit();
            } catch (Exception e) {
                c.rollback();
                throw e;
            } finally {
                c.setAutoCommit(true);
            }
        }
        return findByReference(ref);
    }

    @Override
    public Booking findByReference(String ref) throws SQLException {
        String sql = "SELECT b.*, u.full_name, p.passport_number, "
                + "DATE_FORMAT(s.slot_date,'%Y-%m-%d') slot_date, "
                + "TIME_FORMAT(s.start_time,'%H:%i') start_time, "
                + "TIME_FORMAT(s.end_time,'%H:%i') end_time "
                + "FROM bookings b "
                + "JOIN users u ON u.user_id=b.user_id "
                + "JOIN passport_applications p ON p.application_id=b.application_id "
                + "JOIN slots s ON s.slot_id=b.slot_id "
                + "WHERE b.booking_reference=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, ref);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        }
        return null;
    }

    @Override
    public Booking findById(int bookingId) throws SQLException {
        String sql = "SELECT b.*, u.full_name, p.passport_number, "
                + "DATE_FORMAT(s.slot_date,'%Y-%m-%d') slot_date, "
                + "TIME_FORMAT(s.start_time,'%H:%i') start_time, "
                + "TIME_FORMAT(s.end_time,'%H:%i') end_time "
                + "FROM bookings b "
                + "JOIN users u ON u.user_id=b.user_id "
                + "JOIN passport_applications p ON p.application_id=b.application_id "
                + "JOIN slots s ON s.slot_id=b.slot_id "
                + "WHERE b.booking_id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        }
        return null;
    }

    @Override
    public boolean cancelBooking(int bookingId, int userId) throws SQLException {
        try (Connection c = DBConnection.getConnection()) {
            c.setAutoCommit(false);
            try {
                int slotId = -1;
                String query = (userId > 0)
                        ? "SELECT slot_id, booking_status FROM bookings WHERE booking_id=? AND user_id=? FOR UPDATE"
                        : "SELECT slot_id, booking_status FROM bookings WHERE booking_id=? FOR UPDATE";
                try (PreparedStatement ps = c.prepareStatement(query)) {
                    ps.setInt(1, bookingId);
                    if (userId > 0) ps.setInt(2, userId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) return false;
                        String status = rs.getString("booking_status");
                        if ("CANCELLED".equalsIgnoreCase(status)) return false;
                        slotId = rs.getInt("slot_id");
                    }
                }

                String updateBooking = "UPDATE bookings SET booking_status='CANCELLED' WHERE booking_id=?";
                try (PreparedStatement ps = c.prepareStatement(updateBooking)) {
                    ps.setInt(1, bookingId);
                    ps.executeUpdate();
                }

                if (slotId > 0) {
                    String updateSlot = "UPDATE slots SET booked_count=GREATEST(0, booked_count - 1) WHERE slot_id=?";
                    try (PreparedStatement ps = c.prepareStatement(updateSlot)) {
                        ps.setInt(1, slotId);
                        ps.executeUpdate();
                    }
                }

                c.commit();
                return true;
            } catch (Exception e) {
                c.rollback();
                throw e;
            } finally {
                c.setAutoCommit(true);
            }
        }
    }

    @Override
    public boolean updateStatus(int bookingId, String newStatus) throws SQLException {
        String sql = "UPDATE bookings SET booking_status=? WHERE booking_id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt(2, bookingId);
            return ps.executeUpdate() > 0;
        }
    }

    private String baseSql(String where) {
        return "SELECT b.*, u.full_name, p.passport_number, "
                + "DATE_FORMAT(s.slot_date,'%Y-%m-%d') slot_date, "
                + "TIME_FORMAT(s.start_time,'%H:%i') start_time, "
                + "TIME_FORMAT(s.end_time,'%H:%i') end_time "
                + "FROM bookings b "
                + "JOIN users u ON u.user_id=b.user_id "
                + "JOIN passport_applications p ON p.application_id=b.application_id "
                + "JOIN slots s ON s.slot_id=b.slot_id "
                + where
                + " ORDER BY s.slot_date DESC, s.start_time DESC";
    }

    @Override
    public List<Booking> findByUserId(int userId) throws SQLException {
        return find(baseSql("WHERE b.user_id=?"), userId);
    }

    @Override
    public List<Booking> findAll() throws SQLException {
        return find(baseSql(""), null);
    }

    private List<Booking> find(String sql, Integer userId) throws SQLException {
        List<Booking> list = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            if (userId != null) ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }

    @Override
    public int countAll() throws SQLException {
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) FROM bookings WHERE booking_status <> 'CANCELLED'");
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getInt(1);
        }
    }
}
