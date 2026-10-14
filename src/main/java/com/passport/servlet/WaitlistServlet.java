package com.passport.servlet;

import com.passport.model.User;
import com.passport.model.WaitlistEntry;
import com.passport.service.WaitlistService;
import com.passport.service.impl.WaitlistServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/app/waitlist")
public class WaitlistServlet extends HttpServlet {
    private final WaitlistService service = new WaitlistServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            User user = (User) req.getSession().getAttribute("user");
            List<WaitlistEntry> list = service.getWaitlistByUser(user.getUserId());
            req.setAttribute("waitlist", list);
            req.setAttribute("activeCount", service.countActive());
            req.getRequestDispatcher("/WEB-INF/views/waitlist.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        String action = req.getParameter("action");

        try {
            if ("cancel".equalsIgnoreCase(action)) {
                int waitlistId = Integer.parseInt(req.getParameter("waitlistId"));
                service.cancelWaitlist(waitlistId);
                req.getSession().setAttribute("success", "Waitlist request cancelled.");
            } else {
                String fromStr = req.getParameter("fromDate");
                String toStr = req.getParameter("toDate");
                String urgency = req.getParameter("urgencyCategory");
                String reason = req.getParameter("reason");

                if (fromStr == null || fromStr.isBlank() || toStr == null || toStr.isBlank()) {
                    throw new IllegalArgumentException("Both preferred start and end dates are required.");
                }

                LocalDate fromDate = LocalDate.parse(fromStr.trim());
                LocalDate toDate = LocalDate.parse(toStr.trim());

                service.joinWaitlist(user.getUserId(), fromDate, toDate, urgency, reason);
                req.getSession().setAttribute("success", "Priority Waitlist request registered successfully!");
            }
            resp.sendRedirect(req.getContextPath() + "/app/waitlist");
        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
            doGet(req, resp);
        }
    }
}
