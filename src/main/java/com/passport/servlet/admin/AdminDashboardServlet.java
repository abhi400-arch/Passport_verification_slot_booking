package com.passport.servlet.admin;

import com.passport.dao.impl.GateCheckinDAOImpl;
import com.passport.dao.impl.UserDAOImpl;
import com.passport.service.impl.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {
    private final UserDAOImpl users = new UserDAOImpl();
    private final SlotServiceImpl slots = new SlotServiceImpl();
    private final BookingServiceImpl bookings = new BookingServiceImpl();
    private final WaitlistServiceImpl waitlist = new WaitlistServiceImpl();
    private final DocumentServiceImpl documents = new DocumentServiceImpl();
    private final GateCheckinDAOImpl gateCheckins = new GateCheckinDAOImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        try {
            req.setAttribute("userCount", users.countAll());
            req.setAttribute("slotCount", slots.countAvailable());
            req.setAttribute("bookingCount", bookings.count());
            req.setAttribute("waitlistCount", waitlist.countActive());
            req.setAttribute("verifiedDocsCount", documents.countVerifiedDocuments());
            req.setAttribute("todayCheckins", gateCheckins.countTodayCheckins());

            req.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(req, resp);
        }
    }
}
