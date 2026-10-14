package com.passport.servlet;

import com.passport.model.Notification;
import com.passport.model.User;
import com.passport.service.NotificationService;
import com.passport.service.impl.NotificationServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/app/notifications")
public class NotificationServlet extends HttpServlet {
    private final NotificationService service = new NotificationServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            User user = (User) req.getSession().getAttribute("user");
            List<Notification> list = service.getNotifications(user.getUserId());
            int unread = service.getUnreadCount(user.getUserId());

            req.setAttribute("notifications", list);
            req.setAttribute("unreadCount", unread);
            req.getRequestDispatcher("/WEB-INF/views/notifications.jsp").forward(req, resp);
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
            if ("readAll".equalsIgnoreCase(action)) {
                service.markAllAsRead(user.getUserId());
            } else if (req.getParameter("notificationId") != null) {
                int nid = Integer.parseInt(req.getParameter("notificationId"));
                service.markAsRead(nid, user.getUserId());
            }
            resp.sendRedirect(req.getContextPath() + "/app/notifications");
        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
            doGet(req, resp);
        }
    }
}
