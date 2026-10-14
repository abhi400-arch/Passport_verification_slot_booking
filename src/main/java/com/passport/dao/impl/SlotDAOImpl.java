package com.passport.dao.impl;

import com.passport.config.DBConnection;
import com.passport.dao.SlotDAO;
import com.passport.model.Slot;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SlotDAOImpl implements SlotDAO {
    private Slot map(ResultSet rs) throws SQLException {
        Slot s=new Slot();s.setSlotId(rs.getInt("slot_id"));s.setSlotDate(rs.getDate("slot_date").toLocalDate());
        s.setStartTime(rs.getTime("start_time").toLocalTime());s.setEndTime(rs.getTime("end_time").toLocalTime());
        s.setCapacity(rs.getInt("capacity"));s.setBookedCount(rs.getInt("booked_count"));s.setStatus(rs.getString("status"));return s;
    }
    @Override public List<Slot> findAvailable() throws SQLException {
        List<Slot> list=new ArrayList<>();
        String sql="SELECT * FROM slots WHERE status='AVAILABLE' AND slot_date>=CURDATE() AND booked_count<capacity ORDER BY slot_date,start_time";
        try(Connection c=DBConnection.getConnection();PreparedStatement ps=c.prepareStatement(sql);ResultSet rs=ps.executeQuery()){while(rs.next())list.add(map(rs));}return list;
    }
    @Override public int create(Slot s) throws SQLException {
        String sql="INSERT INTO slots(slot_date,start_time,end_time,capacity) VALUES(?,?,?,?)";
        try(Connection c=DBConnection.getConnection();PreparedStatement ps=c.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)){
            ps.setDate(1,Date.valueOf(s.getSlotDate()));ps.setTime(2,Time.valueOf(s.getStartTime()));ps.setTime(3,Time.valueOf(s.getEndTime()));ps.setInt(4,s.getCapacity());
            ps.executeUpdate();try(ResultSet rs=ps.getGeneratedKeys()){if(rs.next())return rs.getInt(1);}
        }return 0;
    }
    @Override public boolean delete(int slotId) throws SQLException {
        String sql="DELETE FROM slots WHERE slot_id=? AND booked_count=0";
        try(Connection c=DBConnection.getConnection();PreparedStatement ps=c.prepareStatement(sql)){ps.setInt(1,slotId);return ps.executeUpdate()>0;}
    }
    @Override public int countAvailable() throws SQLException {
        String sql="SELECT COUNT(*) FROM slots WHERE status='AVAILABLE' AND slot_date>=CURDATE() AND booked_count<capacity";
        try(Connection c=DBConnection.getConnection();PreparedStatement ps=c.prepareStatement(sql);ResultSet rs=ps.executeQuery()){rs.next();return rs.getInt(1);}
    }
}
