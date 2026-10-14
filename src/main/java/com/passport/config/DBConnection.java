package com.passport.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBConnection {
    private static final String URL =
            "jdbc:mysql://localhost:3306/passport_booking?useSSL=false&serverTimezone=Asia/Kolkata&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = "Abhishekt@2006";

    private DBConnection() {}

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError("MySQL JDBC Driver not found.");
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
