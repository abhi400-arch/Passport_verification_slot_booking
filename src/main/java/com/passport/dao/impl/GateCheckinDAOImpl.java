package com.passport.dao.impl;

import com.passport.config.DBConnection;
import com.passport.dao.GateCheckinDAO;
import com.passport.model.GateCheckin;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GateCheckinDAOImpl implements GateCheckinDAO {
    @Override
    public void create(GateCheckin checkin) throws SQLException {
        String sql = "INSERT INTO gate_checkins (booking_id, stage, status, officer_name, notes) VALUES (?, ?, ?, ?, ?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, checkin.getBookingId());
            ps.setString(2, checkin.getStage());
            ps.setString(3, checkin.getStatus());
            ps.setString(4, checkin.getOfficerName());
            ps.setString(5, checkin.getNotes());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    checkin.setCheckinId(rs.getInt(1));
                }
            }
        }
    }

    @Override
    public List<GateCheckin> findByBookingId(int bookingId) throws SQLException {
        List<GateCheckin> list = new ArrayList<>();
        String sql = "SELECT * FROM gate_checkins WHERE booking_id=? ORDER BY logged_at ASC";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    GateCheckin g = new GateCheckin();
                    g.setCheckinId(rs.getInt("checkin_id"));
                    g.setBookingId(rs.getInt("booking_id"));
                    g.setStage(rs.getString("stage"));
                    g.setStatus(rs.getString("status"));
                    g.setOfficerName(rs.getString("officer_name"));
                    g.setNotes(rs.getString("notes"));
                    Timestamp ts = rs.getTimestamp("logged_at");
                    if (ts != null) g.setLoggedAt(ts.toLocalDateTime());
                    list.add(g);
                }
            }
        }
        return list;
    }

    @Override
    public int countTodayCheckins() throws SQLException {
        String sql = "SELECT COUNT(*) FROM gate_checkins WHERE DATE(logged_at) = CURRENT_DATE";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getInt(1);
        }
    }
}
