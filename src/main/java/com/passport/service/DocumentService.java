package com.passport.service;

import com.passport.model.DocumentComplianceResult;
import com.passport.model.DocumentProof;
import java.util.List;

public interface DocumentService {
    void registerDocument(int userId, String docType, String docName, String docIdentifier) throws Exception;
    DocumentComplianceResult getComplianceResult(int userId) throws Exception;
    List<DocumentProof> getDocumentsByUser(int userId) throws Exception;
    boolean deleteDocument(int docId, int userId) throws Exception;
    int countVerifiedDocuments() throws Exception;
}
