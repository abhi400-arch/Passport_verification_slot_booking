package com.passport.dao.impl;

import com.passport.config.DBConnection;
import com.passport.dao.WaitlistDAO;
import com.passport.model.WaitlistEntry;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class WaitlistDAOImpl implements WaitlistDAO {
    private WaitlistEntry map(ResultSet rs) throws SQLException {
        WaitlistEntry w = new WaitlistEntry();
        w.setWaitlistId(rs.getInt("waitlist_id"));
        w.setUserId(rs.getInt("user_id"));
        Date d1 = rs.getDate("preferred_date_from");
        if (d1 != null) w.setPreferredDateFrom(d1.toLocalDate());
        Date d2 = rs.getDate("preferred_date_to");
        if (d2 != null) w.setPreferredDateTo(d2.toLocalDate());
        w.setUrgencyCategory(rs.getString("urgency_category"));
        w.setReason(rs.getString("reason"));
        w.setStatus(rs.getString("status"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) w.setCreatedAt(ts.toLocalDateTime());
        w.setApplicantName(rs.getString("full_name"));
        w.setApplicantEmail(rs.getString("email"));
        w.setApplicantPhone(rs.getString("phone"));
        return w;
    }

    @Override
    public void create(WaitlistEntry entry) throws SQLException {
        String sql = "INSERT INTO waitlist_requests (user_id, preferred_date_from, preferred_date_to, urgency_category, reason, status) "
                + "VALUES (?, ?, ?, ?, ?, 'ACTIVE')";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, entry.getUserId());
            ps.setDate(2, Date.valueOf(entry.getPreferredDateFrom()));
            ps.setDate(3, Date.valueOf(entry.getPreferredDateTo()));
            ps.setString(4, entry.getUrgencyCategory());
            ps.setString(5, entry.getReason());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    entry.setWaitlistId(rs.getInt(1));
                }
            }
        }
    }

    private String baseSql(String where) {
        return "SELECT w.*, u.full_name, u.email, u.phone "
                + "FROM waitlist_requests w "
                + "JOIN users u ON u.user_id = w.user_id "
                + where
                + " ORDER BY "
                + "   CASE w.urgency_category "
                + "       WHEN 'TATKAL_URGENT' THEN 1 "
                + "       WHEN 'MEDICAL_EMERGENCY' THEN 2 "
                + "       WHEN 'EDUCATION_STUDY' THEN 3 "
                + "       ELSE 4 END ASC, "
                + "   w.created_at ASC";
    }

    @Override
    public List<WaitlistEntry> findByUserId(int userId) throws SQLException {
        List<WaitlistEntry> list = new ArrayList<>();
        String sql = baseSql("WHERE w.user_id=?");
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    WaitlistEntry entry = map(rs);
                    entry.setQueuePosition(getQueuePosition(entry.getWaitlistId()));
                    list.add(entry);
                }
            }
        }
        return list;
    }

    @Override
    public List<WaitlistEntry> findAllActive() throws SQLException {
        List<WaitlistEntry> list = new ArrayList<>();
        String sql = baseSql("WHERE w.status='ACTIVE'");
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            int pos = 1;
            while (rs.next()) {
                WaitlistEntry entry = map(rs);
                entry.setQueuePosition(pos++);
                list.add(entry);
            }
        }
        return list;
    }

    @Override
    public List<WaitlistEntry> findActiveMatchingDate(LocalDate date) throws SQLException {
        List<WaitlistEntry> list = new ArrayList<>();
        String sql = baseSql("WHERE w.status='ACTIVE' AND ? BETWEEN w.preferred_date_from AND w.preferred_date_to");
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(date));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }

    @Override
    public boolean updateStatus(int waitlistId, String status) throws SQLException {
        String sql = "UPDATE waitlist_requests SET status=? WHERE waitlist_id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, waitlistId);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public int countActive() throws SQLException {
        String sql = "SELECT COUNT(*) FROM waitlist_requests WHERE status='ACTIVE'";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getInt(1);
        }
    }

    @Override
    public int getQueuePosition(int waitlistId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM waitlist_requests w1 "
                + "WHERE w1.status='ACTIVE' AND ( "
                + "   (CASE w1.urgency_category WHEN 'TATKAL_URGENT' THEN 1 WHEN 'MEDICAL_EMERGENCY' THEN 2 WHEN 'EDUCATION_STUDY' THEN 3 ELSE 4 END) "
                + "   < (SELECT CASE w2.urgency_category WHEN 'TATKAL_URGENT' THEN 1 WHEN 'MEDICAL_EMERGENCY' THEN 2 WHEN 'EDUCATION_STUDY' THEN 3 ELSE 4 END FROM waitlist_requests w2 WHERE w2.waitlist_id=?) "
                + "   OR ("
                + "       (SELECT CASE w2.urgency_category WHEN 'TATKAL_URGENT' THEN 1 WHEN 'MEDICAL_EMERGENCY' THEN 2 WHEN 'EDUCATION_STUDY' THEN 3 ELSE 4 END FROM waitlist_requests w2 WHERE w2.waitlist_id=?) "
                + "       = (CASE w1.urgency_category WHEN 'TATKAL_URGENT' THEN 1 WHEN 'MEDICAL_EMERGENCY' THEN 2 WHEN 'EDUCATION_STUDY' THEN 3 ELSE 4 END) "
                + "       AND w1.created_at <= (SELECT w2.created_at FROM waitlist_requests w2 WHERE w2.waitlist_id=?) "
                + "   )"
                + ")";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, waitlistId);
            ps.setInt(2, waitlistId);
            ps.setInt(3, waitlistId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Math.max(1, rs.getInt(1));
            }
        }
        return 1;
    }
}
