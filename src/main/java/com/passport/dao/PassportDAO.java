package com.passport.dao;

import com.passport.model.PassportApplication;
import java.sql.SQLException;

public interface PassportDAO {
    PassportApplication findByUserId(int userId) throws SQLException;
    void saveOrUpdate(PassportApplication application) throws SQLException;
}
