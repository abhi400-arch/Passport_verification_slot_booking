package com.passport.model;

import java.time.LocalDateTime;

public class GateCheckin {
    private int checkinId;
    private int bookingId;
    private String stage; // GATE_ENTRY, DOC_VERIFY, BIOMETRICS, FINAL_DECISION
    private String status; // PASS, FAIL, PENDING
    private String officerName;
    private String notes;
    private LocalDateTime loggedAt;

    public GateCheckin() {}

    public GateCheckin(int bookingId, String stage, String status, String officerName, String notes) {
        this.bookingId = bookingId;
        this.stage = stage;
        this.status = status;
        this.officerName = officerName;
        this.notes = notes;
    }

    public int getCheckinId() { return checkinId; }
    public void setCheckinId(int checkinId) { this.checkinId = checkinId; }

    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }

    public String getStage() { return stage; }
    public void setStage(String stage) { this.stage = stage; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getOfficerName() { return officerName; }
    public void setOfficerName(String officerName) { this.officerName = officerName; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDateTime getLoggedAt() { return loggedAt; }
    public void setLoggedAt(LocalDateTime loggedAt) { this.loggedAt = loggedAt; }

    public String getStageLabel() {
        if ("GATE_ENTRY".equals(stage)) return "Gate Entry Check-in";
        if ("DOC_VERIFY".equals(stage)) return "Physical Document Verification";
        if ("BIOMETRICS".equals(stage)) return "Biometrics & Photograph Capture";
        if ("FINAL_DECISION".equals(stage)) return "Final Verification Clearance";
        return stage;
    }
}
