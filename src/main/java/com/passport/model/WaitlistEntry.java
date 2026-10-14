package com.passport.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class WaitlistEntry {
    private int waitlistId;
    private int userId;
    private LocalDate preferredDateFrom;
    private LocalDate preferredDateTo;
    private String urgencyCategory; // TATKAL_URGENT, MEDICAL_EMERGENCY, EDUCATION_STUDY, GENERAL
    private String reason;
    private String status; // ACTIVE, FULFILLED, CANCELLED
    private LocalDateTime createdAt;

    // Joined applicant info
    private String applicantName;
    private String applicantEmail;
    private String applicantPhone;
    private int queuePosition;

    public WaitlistEntry() {}

    public WaitlistEntry(int userId, LocalDate preferredDateFrom, LocalDate preferredDateTo,
                         String urgencyCategory, String reason) {
        this.userId = userId;
        this.preferredDateFrom = preferredDateFrom;
        this.preferredDateTo = preferredDateTo;
        this.urgencyCategory = urgencyCategory;
        this.reason = reason;
        this.status = "ACTIVE";
    }

    public int getWaitlistId() { return waitlistId; }
    public void setWaitlistId(int waitlistId) { this.waitlistId = waitlistId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public LocalDate getPreferredDateFrom() { return preferredDateFrom; }
    public void setPreferredDateFrom(LocalDate preferredDateFrom) { this.preferredDateFrom = preferredDateFrom; }

    public LocalDate getPreferredDateTo() { return preferredDateTo; }
    public void setPreferredDateTo(LocalDate preferredDateTo) { this.preferredDateTo = preferredDateTo; }

    public String getUrgencyCategory() { return urgencyCategory; }
    public void setUrgencyCategory(String urgencyCategory) { this.urgencyCategory = urgencyCategory; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getApplicantName() { return applicantName; }
    public void setApplicantName(String applicantName) { this.applicantName = applicantName; }

    public String getApplicantEmail() { return applicantEmail; }
    public void setApplicantEmail(String applicantEmail) { this.applicantEmail = applicantEmail; }

    public String getApplicantPhone() { return applicantPhone; }
    public void setApplicantPhone(String applicantPhone) { this.applicantPhone = applicantPhone; }

    public int getQueuePosition() { return queuePosition; }
    public void setQueuePosition(int queuePosition) { this.queuePosition = queuePosition; }

    public String getCategoryLabel() {
        if ("TATKAL_URGENT".equals(urgencyCategory)) return "⚡ Tatkal Urgent Expedited";
        if ("MEDICAL_EMERGENCY".equals(urgencyCategory)) return "🏥 Medical Emergency Priority";
        if ("EDUCATION_STUDY".equals(urgencyCategory)) return "🎓 Higher Education / Visa Deadline";
        return "Standard Priority Queue";
    }
}
