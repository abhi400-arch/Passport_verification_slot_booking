package com.passport.dao.impl;

import com.passport.config.DBConnection;
import com.passport.dao.DocumentDAO;
import com.passport.model.DocumentProof;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DocumentDAOImpl implements DocumentDAO {
    private DocumentProof map(ResultSet rs) throws SQLException {
        DocumentProof d = new DocumentProof();
        d.setDocId(rs.getInt("doc_id"));
        d.setUserId(rs.getInt("user_id"));
        d.setDocType(rs.getString("doc_type"));
        d.setDocName(rs.getString("doc_name"));
        d.setDocIdentifier(rs.getString("doc_identifier"));
        d.setVerificationStatus(rs.getString("verification_status"));
        d.setRemarks(rs.getString("remarks"));
        Timestamp ts = rs.getTimestamp("updated_at");
        if (ts != null) d.setUpdatedAt(ts.toLocalDateTime());
        return d;
    }

    @Override
    public void saveOrUpdate(DocumentProof doc) throws SQLException {
        String sql = "INSERT INTO document_proofs (user_id, doc_type, doc_name, doc_identifier, verification_status, remarks) "
                + "VALUES (?, ?, ?, ?, ?, ?) "
                + "ON DUPLICATE KEY UPDATE doc_name=VALUES(doc_name), doc_identifier=VALUES(doc_identifier), "
                + "verification_status=VALUES(verification_status), remarks=VALUES(remarks), updated_at=CURRENT_TIMESTAMP";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, doc.getUserId());
            ps.setString(2, doc.getDocType());
            ps.setString(3, doc.getDocName());
            ps.setString(4, doc.getDocIdentifier());
            ps.setString(5, doc.getVerificationStatus());
            ps.setString(6, doc.getRemarks());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    doc.setDocId(rs.getInt(1));
                }
            }
        }
    }

    @Override
    public List<DocumentProof> findByUserId(int userId) throws SQLException {
        List<DocumentProof> list = new ArrayList<>();
        String sql = "SELECT * FROM document_proofs WHERE user_id=? ORDER BY doc_id ASC";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }

    @Override
    public DocumentProof findByUserAndType(int userId, String docType) throws SQLException {
        String sql = "SELECT * FROM document_proofs WHERE user_id=? AND doc_type=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, docType);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        }
        return null;
    }

    @Override
    public boolean delete(int docId, int userId) throws SQLException {
        String sql = "DELETE FROM document_proofs WHERE doc_id=? AND user_id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, docId);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public int countTotalVerified() throws SQLException {
        String sql = "SELECT COUNT(*) FROM document_proofs WHERE verification_status='VERIFIED'";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getInt(1);
        }
    }
}
