package com.passport.servlet;

import com.passport.model.Booking;
import com.passport.model.DocumentComplianceResult;
import com.passport.model.GateCheckin;
import com.passport.model.User;
import com.passport.service.DigitalPassService;
import com.passport.service.DocumentService;
import com.passport.service.impl.DigitalPassServiceImpl;
import com.passport.service.impl.DocumentServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/app/pass")
public class DigitalPassServlet extends HttpServlet {
    private final DigitalPassService passService = new DigitalPassServiceImpl();
    private final DocumentService documentService = new DocumentServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            User user = (User) req.getSession().getAttribute("user");
            String ref = req.getParameter("ref");
            Booking booking;

            if (ref != null && !ref.isBlank()) {
                booking = passService.getPassByReference(ref.trim());
                if (booking != null && booking.getUserId() != user.getUserId() && !"ADMIN".equals(user.getRole())) {
                    req.setAttribute("error", "Unauthorized access to appointment pass.");
                    req.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(req, resp);
                    return;
                }
            } else {
                booking = passService.getLatestActivePass(user.getUserId());
            }

            if (booking == null) {
                req.setAttribute("error", "No active booking found. Please book a verification slot first.");
                req.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(req, resp);
                return;
            }

            String checksum = passService.generateSecurityChecksum(booking);
            List<GateCheckin> history = passService.getCheckinHistory(booking.getBookingId());
            DocumentComplianceResult compliance = documentService.getComplianceResult(booking.getUserId());

            req.setAttribute("booking", booking);
            req.setAttribute("checksum", checksum);
            req.setAttribute("history", history);
            req.setAttribute("compliance", compliance);
            req.getRequestDispatcher("/WEB-INF/views/digital-pass.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(req, resp);
        }
    }
}
