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

@WebServlet(urlPatterns = {"/admin/verify-pass", "/verify-pass"})
public class VerifyPassServlet extends HttpServlet {
    private final DigitalPassService passService = new DigitalPassServiceImpl();
    private final DocumentService documentService = new DocumentServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String ref = req.getParameter("ref");
        if (ref != null && !ref.isBlank()) {
            try {
                Booking booking = passService.getPassByReference(ref.trim());
                if (booking != null) {
                    List<GateCheckin> history = passService.getCheckinHistory(booking.getBookingId());
                    DocumentComplianceResult compliance = documentService.getComplianceResult(booking.getUserId());
                    String checksum = passService.generateSecurityChecksum(booking);

                    req.setAttribute("booking", booking);
                    req.setAttribute("history", history);
                    req.setAttribute("compliance", compliance);
                    req.setAttribute("checksum", checksum);
                } else {
                    req.setAttribute("searchError", "Invalid or expired appointment reference: " + ref);
                }
            } catch (Exception e) {
                req.setAttribute("searchError", "Error looking up pass: " + e.getMessage());
            }
        }
        req.getRequestDispatcher("/WEB-INF/views/verify-pass.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            resp.sendError(403, "Administrator access required to perform counter verification.");
            return;
        }

        try {
            int bookingId = Integer.parseInt(req.getParameter("bookingId"));
            String stage = req.getParameter("stage");
            String status = req.getParameter("status");
            String notes = req.getParameter("notes");
            String officerName = user.getFullName();

            passService.recordStageCheckin(bookingId, stage, status, officerName, notes);

            String ref = req.getParameter("ref");
            req.getSession().setAttribute("successMsg", "Stage " + stage + " marked as " + status + " successfully.");
            resp.sendRedirect(req.getContextPath() + "/admin/verify-pass?ref=" + (ref != null ? ref : ""));
        } catch (Exception e) {
            req.setAttribute("searchError", "Verification action failed: " + e.getMessage());
            doGet(req, resp);
        }
    }
}
