package com.passport.listener;

import com.passport.config.DBConnection;
import com.passport.dao.impl.SlotDAOImpl;
import com.passport.dao.impl.UserDAOImpl;
import com.passport.model.Slot;
import com.passport.model.User;
import com.passport.util.PasswordUtil;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;

@WebListener
public class AppStartupListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent event) {
        initSchema();
        bootstrapData();
    }

    private void initSchema() {
        try (Connection c = DBConnection.getConnection();
             Statement s = c.createStatement()) {
            try {
                s.execute("ALTER TABLE bookings MODIFY COLUMN booking_status VARCHAR(30) NOT NULL DEFAULT 'CONFIRMED'");
            } catch (Exception ignored) {}

            s.execute("CREATE TABLE IF NOT EXISTS document_proofs ("
                    + "doc_id INT PRIMARY KEY AUTO_INCREMENT, "
                    + "user_id INT NOT NULL, "
                    + "doc_type VARCHAR(50) NOT NULL, "
                    + "doc_name VARCHAR(100) NOT NULL, "
                    + "doc_identifier VARCHAR(100), "
                    + "verification_status VARCHAR(30) NOT NULL DEFAULT 'PENDING', "
                    + "remarks VARCHAR(255), "
                    + "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, "
                    + "CONSTRAINT fk_doc_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE, "
                    + "UNIQUE KEY uq_user_doctype (user_id, doc_type))");

            s.execute("CREATE TABLE IF NOT EXISTS waitlist_requests ("
                    + "waitlist_id INT PRIMARY KEY AUTO_INCREMENT, "
                    + "user_id INT NOT NULL, "
                    + "preferred_date_from DATE NOT NULL, "
                    + "preferred_date_to DATE NOT NULL, "
                    + "urgency_category VARCHAR(50) NOT NULL DEFAULT 'TATKAL_URGENT', "
                    + "reason VARCHAR(255), "
                    + "status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE', "
                    + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                    + "CONSTRAINT fk_waitlist_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE)");

            s.execute("CREATE TABLE IF NOT EXISTS notifications ("
                    + "notification_id INT PRIMARY KEY AUTO_INCREMENT, "
                    + "user_id INT NOT NULL, "
                    + "title VARCHAR(150) NOT NULL, "
                    + "message TEXT NOT NULL, "
                    + "type VARCHAR(30) NOT NULL DEFAULT 'INFO', "
                    + "is_read BOOLEAN NOT NULL DEFAULT FALSE, "
                    + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                    + "CONSTRAINT fk_notify_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE)");

            s.execute("CREATE TABLE IF NOT EXISTS gate_checkins ("
                    + "checkin_id INT PRIMARY KEY AUTO_INCREMENT, "
                    + "booking_id INT NOT NULL, "
                    + "stage VARCHAR(50) NOT NULL, "
                    + "status VARCHAR(30) NOT NULL, "
                    + "officer_name VARCHAR(100) NOT NULL, "
                    + "notes VARCHAR(255), "
                    + "logged_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                    + "CONSTRAINT fk_checkin_booking FOREIGN KEY (booking_id) REFERENCES bookings(booking_id) ON DELETE CASCADE)");
        } catch (Exception e) {
            System.err.println("Schema auto-init notice: " + e.getMessage());
        }
    }

    private void bootstrapData() {
        try {
            UserDAOImpl users = new UserDAOImpl();
            if (!users.existsByEmail("admin@passport.local")) {
                users.create(new User("System Administrator", "admin@passport.local", "9999999999",
                        PasswordUtil.hash("Admin@123"), "ADMIN"));
            }
            SlotDAOImpl slots = new SlotDAOImpl();
            if (slots.countAvailable() == 0) {
                LocalDate d = LocalDate.now().plusDays(2);
                slots.create(new Slot(d, LocalTime.of(9, 0), LocalTime.of(9, 30), 2));
                slots.create(new Slot(d, LocalTime.of(10, 0), LocalTime.of(10, 30), 2));
                slots.create(new Slot(d.plusDays(1), LocalTime.of(11, 0), LocalTime.of(11, 30), 3));
                slots.create(new Slot(d.plusDays(2), LocalTime.of(14, 0), LocalTime.of(14, 30), 2));
                slots.create(new Slot(d.plusDays(3), LocalTime.of(15, 0), LocalTime.of(15, 30), 2));
            }
        } catch (Exception e) {
            System.err.println("Application bootstrap skipped: " + e.getMessage());
        }
    }
}
