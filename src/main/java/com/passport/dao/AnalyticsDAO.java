package com.passport.dao;

import java.sql.SQLException;
import java.util.Map;

public interface AnalyticsDAO {
    Map<String,Object> summary() throws SQLException;
}
