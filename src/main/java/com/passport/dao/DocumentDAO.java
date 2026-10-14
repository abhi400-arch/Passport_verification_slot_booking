package com.passport.dao;

import com.passport.model.DocumentProof;
import java.sql.SQLException;
import java.util.List;

public interface DocumentDAO {
    void saveOrUpdate(DocumentProof doc) throws SQLException;
    List<DocumentProof> findByUserId(int userId) throws SQLException;
    DocumentProof findByUserAndType(int userId, String docType) throws SQLException;
    boolean delete(int docId, int userId) throws SQLException;
    int countTotalVerified() throws SQLException;
}
