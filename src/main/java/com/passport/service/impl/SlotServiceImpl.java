package com.passport.service.impl;
import com.passport.dao.SlotDAO;
import com.passport.dao.impl.SlotDAOImpl;
import com.passport.model.Slot;
import com.passport.service.SlotService;
import com.passport.util.ValidationUtil;
import java.util.List;
public class SlotServiceImpl implements SlotService {
    private final SlotDAO dao=new SlotDAOImpl();
    public List<Slot> available()throws Exception{return dao.findAvailable();}
    public int add(Slot s)throws Exception{ValidationUtil.validateSlot(s.getSlotDate(),s.getStartTime(),s.getEndTime(),s.getCapacity());return dao.create(s);}
    public boolean delete(int id)throws Exception{return dao.delete(id);}
    public int countAvailable()throws Exception{return dao.countAvailable();}
}
