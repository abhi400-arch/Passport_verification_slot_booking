package com.passport.dao.impl;

import com.passport.config.DBConnection;
import com.passport.dao.AnalyticsDAO;
import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class AnalyticsDAOImpl implements AnalyticsDAO {
    private int scalar(Connection c, String sql) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getInt(1);
        }
    }

    @Override
    public Map<String, Object> summary() throws SQLException {
        Map<String, Object> m = new LinkedHashMap<>();
        try (Connection c = DBConnection.getConnection()) {
            m.put("users", scalar(c, "SELECT COUNT(*) FROM users WHERE role='USER'"));
            m.put("applications", scalar(c, "SELECT COUNT(*) FROM passport_applications"));
            m.put("slots", scalar(c, "SELECT COUNT(*) FROM slots WHERE slot_date>=CURDATE()"));
            m.put("availableSlots", scalar(c, "SELECT COUNT(*) FROM slots WHERE status='AVAILABLE' AND slot_date>=CURDATE() AND booked_count<capacity"));
            m.put("bookings", scalar(c, "SELECT COUNT(*) FROM bookings WHERE booking_status <> 'CANCELLED'"));
            m.put("cancelled", scalar(c, "SELECT COUNT(*) FROM bookings WHERE booking_status='CANCELLED'"));
            m.put("utilized", scalar(c, "SELECT COALESCE(SUM(booked_count),0) FROM slots WHERE slot_date>=CURDATE()"));
            m.put("capacity", scalar(c, "SELECT COALESCE(SUM(capacity),0) FROM slots WHERE slot_date>=CURDATE()"));

            // New innovation metrics
            try {
                m.put("waitlistActive", scalar(c, "SELECT COUNT(*) FROM waitlist_requests WHERE status='ACTIVE'"));
                m.put("verifiedDocs", scalar(c, "SELECT COUNT(*) FROM document_proofs WHERE verification_status='VERIFIED'"));
                m.put("gateCheckins", scalar(c, "SELECT COUNT(*) FROM gate_checkins WHERE DATE(logged_at)=CURDATE()"));
            } catch (Exception e) {
                m.put("waitlistActive", 0);
                m.put("verifiedDocs", 0);
                m.put("gateCheckins", 0);
            }

            String peak = "No bookings yet";
            try (PreparedStatement ps = c.prepareStatement("SELECT DATE_FORMAT(s.start_time,'%h:%i %p') FROM bookings b JOIN slots s ON b.slot_id=s.slot_id WHERE b.booking_status <> 'CANCELLED' GROUP BY DATE_FORMAT(s.start_time,'%h:%i %p') ORDER BY COUNT(*) DESC LIMIT 1");
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) peak = rs.getString(1);
            }
            m.put("peakTime", peak);

            String day = "No bookings yet";
            try (PreparedStatement ps = c.prepareStatement("SELECT DAYNAME(s.slot_date) FROM bookings b JOIN slots s ON b.slot_id=s.slot_id WHERE b.booking_status <> 'CANCELLED' GROUP BY DAYNAME(s.slot_date) ORDER BY COUNT(*) DESC LIMIT 1");
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) day = rs.getString(1);
            }
            m.put("popularDay", day);
        }
        return m;
    }
}
