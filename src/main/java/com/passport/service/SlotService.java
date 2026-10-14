package com.passport.service;
import com.passport.model.Slot;
import java.util.List;
public interface SlotService {
    List<Slot> available() throws Exception;
    int add(Slot slot) throws Exception;
    boolean delete(int slotId) throws Exception;
    int countAvailable() throws Exception;
}
