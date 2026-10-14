package com.passport.model;

import java.time.LocalDateTime;

public class DocumentProof {
    private int docId;
    private int userId;
    private String docType;         // IDENTITY_PROOF, DOB_PROOF, ADDRESS_PROOF, NON_ECR_PROOF
    private String docName;         // e.g., Aadhaar Card, Birth Certificate
    private String docIdentifier;   // e.g., Masked ID / Certificate No
    private String verificationStatus; // VERIFIED, NEEDS_ATTENTION, PENDING
    private String remarks;
    private LocalDateTime updatedAt;

    public DocumentProof() {}

    public DocumentProof(int userId, String docType, String docName, String docIdentifier, String verificationStatus, String remarks) {
        this.userId = userId;
        this.docType = docType;
        this.docName = docName;
        this.docIdentifier = docIdentifier;
        this.verificationStatus = verificationStatus;
        this.remarks = remarks;
    }

    public int getDocId() { return docId; }
    public void setDocId(int docId) { this.docId = docId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getDocType() { return docType; }
    public void setDocType(String docType) { this.docType = docType; }

    public String getDocName() { return docName; }
    public void setDocName(String docName) { this.docName = docName; }

    public String getDocIdentifier() { return docIdentifier; }
    public void setDocIdentifier(String docIdentifier) { this.docIdentifier = docIdentifier; }

    public String getVerificationStatus() { return verificationStatus; }
    public void setVerificationStatus(String verificationStatus) { this.verificationStatus = verificationStatus; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public String getFormattedDocType() {
        if ("IDENTITY_PROOF".equals(docType)) return "Identity Proof (Aadhaar / Voter ID)";
        if ("DOB_PROOF".equals(docType)) return "Proof of Date of Birth (Birth / 10th Cert)";
        if ("ADDRESS_PROOF".equals(docType)) return "Proof of Address (Utility / Bank)";
        if ("NON_ECR_PROOF".equals(docType)) return "Non-ECR / Education Certificate";
        return docType;
    }
}
