package com.passport.service.impl;

import com.passport.dao.BookingDAO;
import com.passport.dao.GateCheckinDAO;
import com.passport.dao.impl.BookingDAOImpl;
import com.passport.dao.impl.GateCheckinDAOImpl;
import com.passport.model.Booking;
import com.passport.model.GateCheckin;
import com.passport.service.DigitalPassService;
import com.passport.service.NotificationService;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;

public class DigitalPassServiceImpl implements DigitalPassService {
    private final BookingDAO bookingDAO = new BookingDAOImpl();
    private final GateCheckinDAO gateCheckinDAO = new GateCheckinDAOImpl();
    private final NotificationService notificationService = new NotificationServiceImpl();

    @Override
    public String generateSecurityChecksum(Booking b) {
        if (b == null) return "";
        try {
            String raw = b.getBookingReference() + "|" + b.getUserId() + "|" + b.getSlotDate() + "|PSK-SECURE-KEY";
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(raw.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 8; i++) {
                sb.append(String.format("%02X", hash[i]));
            }
            return sb.toString();
        } catch (Exception e) {
            return "PSK-HASH-" + Math.abs(b.getBookingReference().hashCode());
        }
    }

    @Override
    public Booking getPassByReference(String reference) throws Exception {
        if (reference == null || reference.isBlank()) return null;
        return bookingDAO.findByReference(reference.trim());
    }

    @Override
    public Booking getLatestActivePass(int userId) throws Exception {
        List<Booking> list = bookingDAO.findByUserId(userId);
        for (Booking b : list) {
            if (!"CANCELLED".equalsIgnoreCase(b.getBookingStatus())) {
                return b;
            }
        }
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public void recordStageCheckin(int bookingId, String stage, String status, String officerName, String notes) throws Exception {
        Booking b = bookingDAO.findById(bookingId);
        if (b == null) throw new IllegalArgumentException("Booking not found.");

        GateCheckin checkin = new GateCheckin(bookingId, stage, status, officerName, notes);
        gateCheckinDAO.create(checkin);

        String newBookingStatus;
        String notifTitle;
        String notifMsg;
        String notifType = "INFO";

        if ("GATE_ENTRY".equals(stage)) {
            newBookingStatus = "CHECKED_IN";
            notifTitle = "🏢 Check-in at Passport Center";
            notifMsg = "Your entry pass has been scanned and verified at the PSK entry gate by Officer " + officerName + ". Proceed to Counter A.";
        } else if ("DOC_VERIFY".equals(stage)) {
            newBookingStatus = "DOCS_VERIFIED";
            notifTitle = "📄 Physical Documents Verified";
            notifMsg = "All physical identity documents have been inspected and cleared by Officer " + officerName + ".";
            notifType = "SUCCESS";
        } else if ("BIOMETRICS".equals(stage)) {
            newBookingStatus = "BIOMETRICS_DONE";
            notifTitle = "👁 Biometrics Captured";
            notifMsg = "Fingerprint and iris scanning completed successfully. Please wait in the granting lounge.";
            notifType = "SUCCESS";
        } else if ("FINAL_DECISION".equals(stage)) {
            if ("PASS".equalsIgnoreCase(status) || "APPROVED".equalsIgnoreCase(status)) {
                newBookingStatus = "APPROVED";
                notifTitle = "🎉 Passport Verification Approved!";
                notifMsg = "Your passport application has received official clearance! Passport printing and dispatch initiated.";
                notifType = "SUCCESS";
            } else {
                newBookingStatus = "REJECTED";
                notifTitle = "⚠ Passport Verification Needs Review";
                notifMsg = "Verification on hold. Remarks: " + (notes != null && !notes.isBlank() ? notes : "Deficiency in documents.");
                notifType = "WARNING";
            }
        } else {
            newBookingStatus = status;
            notifTitle = "Verification Status Update";
            notifMsg = "Status updated to " + status + " by Officer " + officerName + ".";
        }

        bookingDAO.updateStatus(bookingId, newBookingStatus);
        notificationService.notify(b.getUserId(), notifTitle, notifMsg, notifType);
    }

    @Override
    public List<GateCheckin> getCheckinHistory(int bookingId) throws Exception {
        return gateCheckinDAO.findByBookingId(bookingId);
    }
}
