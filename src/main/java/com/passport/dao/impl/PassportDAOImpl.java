package com.passport.dao.impl;

import com.passport.config.DBConnection;
import com.passport.dao.PassportDAO;
import com.passport.model.PassportApplication;
import java.sql.*;

public class PassportDAOImpl implements PassportDAO {
    @Override public PassportApplication findByUserId(int userId) throws SQLException {
        String sql="SELECT * FROM passport_applications WHERE user_id=?";
        try(Connection c=DBConnection.getConnection();PreparedStatement ps=c.prepareStatement(sql)){
            ps.setInt(1,userId);try(ResultSet rs=ps.executeQuery()){
                if(!rs.next())return null;
                PassportApplication p=new PassportApplication();
                p.setApplicationId(rs.getInt("application_id"));p.setUserId(userId);p.setPassportNumber(rs.getString("passport_number"));
                p.setDateOfBirth(rs.getDate("date_of_birth").toLocalDate());p.setNationality(rs.getString("nationality"));
                p.setAddress(rs.getString("address"));p.setApplicationType(rs.getString("application_type"));return p;
            }
        }
    }

    @Override public void saveOrUpdate(PassportApplication p) throws SQLException {
        String sql="INSERT INTO passport_applications(user_id,passport_number,date_of_birth,nationality,address,application_type) VALUES(?,?,?,?,?,?) "+
                "ON DUPLICATE KEY UPDATE passport_number=VALUES(passport_number),date_of_birth=VALUES(date_of_birth),nationality=VALUES(nationality),address=VALUES(address),application_type=VALUES(application_type)";
        try(Connection c=DBConnection.getConnection();PreparedStatement ps=c.prepareStatement(sql)){
            ps.setInt(1,p.getUserId());ps.setString(2,p.getPassportNumber());ps.setDate(3,Date.valueOf(p.getDateOfBirth()));
            ps.setString(4,p.getNationality());ps.setString(5,p.getAddress());ps.setString(6,p.getApplicationType());ps.executeUpdate();
        }
    }
}
