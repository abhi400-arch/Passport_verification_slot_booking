package com.passport.dao;

import com.passport.model.Slot;
import java.sql.SQLException;
import java.util.List;

public interface SlotDAO {
    List<Slot> findAvailable() throws SQLException;
    int create(Slot slot) throws SQLException;
    boolean delete(int slotId) throws SQLException;
    int countAvailable() throws SQLException;
}
