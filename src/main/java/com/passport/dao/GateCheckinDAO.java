package com.passport.dao;

import com.passport.model.GateCheckin;
import java.sql.SQLException;
import java.util.List;

public interface GateCheckinDAO {
    void create(GateCheckin checkin) throws SQLException;
    List<GateCheckin> findByBookingId(int bookingId) throws SQLException;
    int countTodayCheckins() throws SQLException;
}
