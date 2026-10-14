package com.passport.dao;

import com.passport.model.User;
import java.sql.SQLException;
import java.util.List;

public interface UserDAO {
    boolean existsByEmail(String email) throws SQLException;
    int create(User user) throws SQLException;
    User findByEmail(String email) throws SQLException;
    int countAll() throws SQLException;
    List<User> findAll() throws SQLException;
}
