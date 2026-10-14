package com.passport.servlet;

import com.passport.model.User;
import com.passport.service.NotificationService;
import com.passport.service.impl.NotificationServiceImpl;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/api/notifications/count")
public class NotificationJsonServlet extends HttpServlet {
    private final NotificationService service = new NotificationServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.getWriter().write("{\"unreadCount\":0}");
            return;
        }

        try {
            int count = service.getUnreadCount(user.getUserId());
            resp.getWriter().write("{\"unreadCount\":" + count + "}");
        } catch (Exception e) {
            resp.getWriter().write("{\"unreadCount\":0,\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}
