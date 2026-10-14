package com.passport.model;

import java.util.ArrayList;
import java.util.List;

public class DocumentComplianceResult {
    private int score; // 0 - 100
    private String status; // CERTIFIED, PARTIAL, INCOMPLETE
    private int verifiedCount;
    private int totalRequired;
    private List<DocumentProof> documents = new ArrayList<>();
    private List<String> checklist = new ArrayList<>();
    private List<String> warnings = new ArrayList<>();

    public DocumentComplianceResult() {}

    public DocumentComplianceResult(int score, String status, int verifiedCount, int totalRequired,
                                    List<DocumentProof> documents, List<String> checklist, List<String> warnings) {
        this.score = score;
        this.status = status;
        this.verifiedCount = verifiedCount;
        this.totalRequired = totalRequired;
        this.documents = documents;
        this.checklist = checklist;
        this.warnings = warnings;
    }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getVerifiedCount() { return verifiedCount; }
    public void setVerifiedCount(int verifiedCount) { this.verifiedCount = verifiedCount; }

    public int getTotalRequired() { return totalRequired; }
    public void setTotalRequired(int totalRequired) { this.totalRequired = totalRequired; }

    public List<DocumentProof> getDocuments() { return documents; }
    public void setDocuments(List<DocumentProof> documents) { this.documents = documents; }

    public List<String> getChecklist() { return checklist; }
    public void setChecklist(List<String> checklist) { this.checklist = checklist; }

    public List<String> getWarnings() { return warnings; }
    public void setWarnings(List<String> warnings) { this.warnings = warnings; }

    public boolean isCounterReady() {
        return score >= 75 && warnings.isEmpty();
    }
}
