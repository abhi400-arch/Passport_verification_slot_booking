package com.passport.dao.impl;

import com.passport.config.DBConnection;
import com.passport.dao.UserDAO;
import com.passport.model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {
    @Override
    public boolean existsByEmail(String email) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE email=?";
        try (Connection c=DBConnection.getConnection(); PreparedStatement ps=c.prepareStatement(sql)) {
            ps.setString(1,email); try(ResultSet rs=ps.executeQuery()){return rs.next();}
        }
    }

    @Override
    public int create(User u) throws SQLException {
        String sql="INSERT INTO users(full_name,email,phone,password_hash,role) VALUES(?,?,?,?,?)";
        try(Connection c=DBConnection.getConnection(); PreparedStatement ps=c.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1,u.getFullName()); ps.setString(2,u.getEmail()); ps.setString(3,u.getPhone());
            ps.setString(4,u.getPasswordHash()); ps.setString(5,u.getRole());
            ps.executeUpdate(); try(ResultSet rs=ps.getGeneratedKeys()){if(rs.next())return rs.getInt(1);}
        }
        return 0;
    }

    @Override
    public User findByEmail(String email) throws SQLException {
        String sql="SELECT * FROM users WHERE email=?";
        try(Connection c=DBConnection.getConnection(); PreparedStatement ps=c.prepareStatement(sql)){
            ps.setString(1,email); try(ResultSet rs=ps.executeQuery()){
                if(!rs.next())return null;
                User u=new User(); u.setUserId(rs.getInt("user_id")); u.setFullName(rs.getString("full_name"));
                u.setEmail(rs.getString("email")); u.setPhone(rs.getString("phone")); u.setPasswordHash(rs.getString("password_hash"));
                u.setRole(rs.getString("role")); return u;
            }
        }
    }

    @Override public int countAll() throws SQLException {
        try(Connection c=DBConnection.getConnection(); PreparedStatement ps=c.prepareStatement("SELECT COUNT(*) FROM users WHERE role='USER'");
            ResultSet rs=ps.executeQuery()){rs.next();return rs.getInt(1);}
    }

    @Override public List<User> findAll() throws SQLException {
        List<User> list=new ArrayList<>();
        String sql="SELECT user_id,full_name,email,phone,role,created_at FROM users ORDER BY created_at DESC";
        try(Connection c=DBConnection.getConnection(); PreparedStatement ps=c.prepareStatement(sql); ResultSet rs=ps.executeQuery()){
            while(rs.next()){User u=new User();u.setUserId(rs.getInt(1));u.setFullName(rs.getString(2));u.setEmail(rs.getString(3));u.setPhone(rs.getString(4));u.setRole(rs.getString(5));list.add(u);}
        } return list;
    }
}
