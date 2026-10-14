package com.passport.servlet;

import com.passport.model.User;
import com.passport.service.BookingService;
import com.passport.service.impl.BookingServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/app/cancel-booking")
public class CancelBookingServlet extends HttpServlet {
    private final BookingService bookingService = new BookingServiceImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            User user = (User) req.getSession().getAttribute("user");
            int bookingId = Integer.parseInt(req.getParameter("bookingId"));

            boolean ok = bookingService.cancel(bookingId, user.getUserId());
            if (ok) {
                req.getSession().setAttribute("success", "Appointment cancelled successfully. Slot has been released.");
            } else {
                req.getSession().setAttribute("error", "Unable to cancel booking. It may have already been cancelled or processed.");
            }
            resp.sendRedirect(req.getContextPath() + "/app/bookings");
        } catch (Exception e) {
            req.getSession().setAttribute("error", "Cancellation failed: " + e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/app/bookings");
        }
    }
}
