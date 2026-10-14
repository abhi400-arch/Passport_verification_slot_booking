package com.passport.servlet;

import com.passport.model.Booking;
import com.passport.model.User;
import com.passport.service.impl.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/app/dashboard")
public class DashboardServlet extends HttpServlet {
    private final PassportServiceImpl passport = new PassportServiceImpl();
    private final SlotServiceImpl slots = new SlotServiceImpl();
    private final BookingServiceImpl bookings = new BookingServiceImpl();
    private final DocumentServiceImpl documents = new DocumentServiceImpl();
    private final WaitlistServiceImpl waitlist = new WaitlistServiceImpl();
    private final NotificationServiceImpl notifications = new NotificationServiceImpl();
    private final DigitalPassServiceImpl digitalPass = new DigitalPassServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        try {
            User u = (User) req.getSession().getAttribute("user");
            req.setAttribute("passport", passport.getByUser(u.getUserId()));
            req.setAttribute("availableSlots", slots.countAvailable());
            req.setAttribute("bookings", bookings.byUser(u.getUserId()));

            // Innovation widgets
            req.setAttribute("compliance", documents.getComplianceResult(u.getUserId()));
            req.setAttribute("userWaitlist", waitlist.getWaitlistByUser(u.getUserId()));
            req.setAttribute("unreadCount", notifications.getUnreadCount(u.getUserId()));
            Booking activePass = digitalPass.getLatestActivePass(u.getUserId());
            req.setAttribute("activePass", activePass);

            req.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(req, resp);
        }
    }
}
