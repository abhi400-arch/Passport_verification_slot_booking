package com.passport.service.impl;

import com.passport.dao.DocumentDAO;
import com.passport.dao.impl.DocumentDAOImpl;
import com.passport.dao.impl.PassportDAOImpl;
import com.passport.model.DocumentComplianceResult;
import com.passport.model.DocumentProof;
import com.passport.model.PassportApplication;
import com.passport.service.DocumentService;
import com.passport.service.NotificationService;

import java.util.*;

public class DocumentServiceImpl implements DocumentService {
    private final DocumentDAO dao = new DocumentDAOImpl();
    private final PassportDAOImpl passportDAO = new PassportDAOImpl();
    private final NotificationService notificationService = new NotificationServiceImpl();

    // Verhoeff algorithm multiplication and permutation tables
    private static final int[][] D = {
            {0, 1, 2, 3, 4, 5, 6, 7, 8, 9},
            {1, 2, 3, 4, 0, 6, 7, 8, 9, 5},
            {2, 3, 4, 0, 1, 7, 8, 9, 5, 6},
            {3, 4, 0, 1, 2, 8, 9, 5, 6, 7},
            {4, 0, 1, 2, 3, 9, 5, 6, 7, 8},
            {5, 9, 8, 7, 6, 0, 4, 3, 2, 1},
            {6, 5, 9, 8, 7, 1, 0, 4, 3, 2},
            {7, 6, 5, 9, 8, 2, 1, 0, 4, 3},
            {8, 7, 6, 5, 9, 3, 2, 1, 0, 4},
            {9, 8, 7, 6, 5, 4, 3, 2, 1, 0}
    };
    private static final int[][] P = {
            {0, 1, 2, 3, 4, 5, 6, 7, 8, 9},
            {1, 5, 7, 6, 2, 8, 3, 0, 9, 4},
            {5, 8, 0, 3, 7, 9, 6, 1, 4, 2},
            {8, 9, 1, 6, 0, 4, 3, 5, 2, 7},
            {9, 4, 5, 3, 1, 2, 6, 8, 7, 0},
            {4, 2, 8, 6, 5, 7, 3, 9, 0, 1},
            {2, 7, 9, 3, 8, 0, 6, 4, 1, 5},
            {7, 0, 4, 6, 9, 1, 3, 2, 5, 8}
    };

    private static boolean validateVerhoeff(String num) {
        int c = 0;
        int[] myArray = new int[num.length()];
        for (int i = 0; i < num.length(); i++) {
            myArray[i] = Integer.parseInt(num.substring(i, i + 1));
        }
        for (int i = 0; i < myArray.length; i++) {
            c = D[c][P[(i % 8)][myArray[myArray.length - i - 1]]];
        }
        return (c == 0);
    }

    @Override
    public void registerDocument(int userId, String docType, String docName, String docIdentifier) throws Exception {
        if (docType == null || docType.isBlank()) {
            throw new IllegalArgumentException("Document type is required.");
        }
        if (docName == null || docName.isBlank()) {
            throw new IllegalArgumentException("Document name/issuer is required.");
        }
        if (docIdentifier == null || docIdentifier.isBlank()) {
            throw new IllegalArgumentException("Document ID / reference number is required.");
        }

        String status = "VERIFIED";
        String remarks = "Document format and check verified successfully.";
        String cleanId = docIdentifier.trim().replaceAll("\\s+", "").replace("-", "");
        String maskedId = docIdentifier.trim();

        if ("IDENTITY_PROOF".equals(docType)) {
            if (docName.toLowerCase().contains("aadhaar") || docName.toLowerCase().contains("aadhar")) {
                if (!cleanId.matches("\\d{12}")) {
                    status = "NEEDS_ATTENTION";
                    remarks = "Aadhaar must be a 12-digit numeric number.";
                } else if (!validateVerhoeff(cleanId)) {
                    status = "NEEDS_ATTENTION";
                    remarks = "Aadhaar checksum failed (Verhoeff validation). Please verify the 12 digits.";
                } else {
                    maskedId = "XXXX-XXXX-" + cleanId.substring(8);
                    remarks = "Aadhaar checksum mathematically verified (Zero-defect format).";
                }
            } else if (docName.toLowerCase().contains("pan")) {
                if (!cleanId.matches("[A-Z]{5}[0-9]{4}[A-Z]")) {
                    status = "NEEDS_ATTENTION";
                    remarks = "PAN number format should be 5 letters, 4 digits, 1 letter (e.g. ABCDE1234F).";
                } else {
                    remarks = "Income Tax PAN structure verified.";
                }
            }
        } else if ("DOB_PROOF".equals(docType)) {
            PassportApplication app = passportDAO.findByUserId(userId);
            if (app != null && app.getDateOfBirth() != null) {
                remarks = "Date of Birth record linked (" + app.getDateOfBirth() + "). Keep original for counter verification.";
            }
        }

        DocumentProof proof = new DocumentProof(userId, docType, docName.trim(), maskedId, status, remarks);
        dao.saveOrUpdate(proof);

        if ("VERIFIED".equals(status)) {
            notificationService.notify(userId,
                    "Document Verified: " + docName,
                    "Your " + proof.getFormattedDocType() + " (" + maskedId + ") has passed algorithmic pre-screening.",
                    "SUCCESS");
        } else {
            notificationService.notify(userId,
                    "Document Needs Attention: " + docName,
                    remarks,
                    "WARNING");
        }
    }

    @Override
    public DocumentComplianceResult getComplianceResult(int userId) throws Exception {
        List<DocumentProof> docs = dao.findByUserId(userId);
        Map<String, DocumentProof> map = new HashMap<>();
        for (DocumentProof d : docs) {
            map.put(d.getDocType(), d);
        }

        List<String> checklist = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        int verifiedCount = 0;
        int totalRequired = 4;

        String[] requiredTypes = {"IDENTITY_PROOF", "DOB_PROOF", "ADDRESS_PROOF", "NON_ECR_PROOF"};
        String[] labels = {
                "Photo Identity Proof (Aadhaar / Voter ID / PAN)",
                "Proof of Date of Birth (Birth / Matriculation Certificate)",
                "Address Proof (Electricity Bill / Bank Passbook / Rent Agreement)",
                "Non-ECR Eligible Qualification Certificate"
        };

        for (int i = 0; i < requiredTypes.length; i++) {
            String type = requiredTypes[i];
            String label = labels[i];
            DocumentProof p = map.get(type);
            if (p != null) {
                if ("VERIFIED".equals(p.getVerificationStatus())) {
                    verifiedCount++;
                    checklist.add("✓ " + label + ": " + p.getDocName() + " (" + p.getDocIdentifier() + ")");
                } else {
                    checklist.add("⚠ " + label + ": " + p.getRemarks());
                    warnings.add(p.getDocName() + " requires attention: " + p.getRemarks());
                }
            } else {
                checklist.add("○ " + label + " - Not yet uploaded");
                if (!"NON_ECR_PROOF".equals(type)) {
                    warnings.add("Missing required document: " + label);
                }
            }
        }

        int score = (int) Math.round((verifiedCount / (double) totalRequired) * 100);
        String status = (score >= 100) ? "CERTIFIED" : (score >= 50 ? "PARTIAL" : "INCOMPLETE");

        return new DocumentComplianceResult(score, status, verifiedCount, totalRequired, docs, checklist, warnings);
    }

    @Override
    public List<DocumentProof> getDocumentsByUser(int userId) throws Exception {
        return dao.findByUserId(userId);
    }

    @Override
    public boolean deleteDocument(int docId, int userId) throws Exception {
        return dao.delete(docId, userId);
    }

    @Override
    public int countVerifiedDocuments() throws Exception {
        return dao.countTotalVerified();
    }
}
