package com.passport.servlet.admin;

import com.passport.model.Slot;
import com.passport.service.impl.SlotServiceImpl;
import com.passport.service.impl.WaitlistServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

@WebServlet("/admin/slots")
public class AdminSlotsServlet extends HttpServlet {
    private final SlotServiceImpl service = new SlotServiceImpl();
    private final WaitlistServiceImpl waitlistService = new WaitlistServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        try {
            req.setAttribute("slots", service.available());
            req.getRequestDispatcher("/WEB-INF/views/admin/slots.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        try {
            LocalDate date = LocalDate.parse(req.getParameter("date"));
            LocalTime start = LocalTime.parse(req.getParameter("start"));
            LocalTime end = LocalTime.parse(req.getParameter("end"));
            int capacity = Integer.parseInt(req.getParameter("capacity"));

            service.add(new Slot(date, start, end, capacity));

            // Notify waitlisted applicants
            waitlistService.notifyMatchesForAvailableSlot(date, start.toString(), end.toString());

            req.getSession().setAttribute("success", "Slot added successfully. Matching waitlisted applicants have been notified.");
            resp.sendRedirect(req.getContextPath() + "/admin/slots");
        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
            doGet(req, resp);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            boolean ok = service.delete(Integer.parseInt(req.getParameter("id")));
            resp.setStatus(ok ? 200 : 409);
            resp.getWriter().print(ok ? "Deleted" : "Cannot delete a slot that already has a booking.");
        } catch (Exception e) {
            resp.setStatus(500);
            resp.getWriter().print(e.getMessage());
        }
    }
}
