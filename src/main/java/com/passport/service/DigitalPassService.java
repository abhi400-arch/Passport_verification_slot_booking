package com.passport.service;

import com.passport.model.Booking;
import com.passport.model.GateCheckin;
import java.util.List;

public interface DigitalPassService {
    String generateSecurityChecksum(Booking booking);
    Booking getPassByReference(String reference) throws Exception;
    Booking getLatestActivePass(int userId) throws Exception;
    void recordStageCheckin(int bookingId, String stage, String status, String officerName, String notes) throws Exception;
    List<GateCheckin> getCheckinHistory(int bookingId) throws Exception;
}
