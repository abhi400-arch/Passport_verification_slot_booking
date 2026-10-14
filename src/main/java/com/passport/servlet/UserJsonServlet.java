package com.passport.servlet;

import com.passport.model.User;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/api/user")
public class UserJsonServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        User u = (User) req.getSession().getAttribute("user");
        if (u == null) {
            resp.setStatus(401);
            resp.getWriter().print("{\"error\":\"Login required\"}");
            return;
        }

        String json = "{"
                + "\"userId\":" + u.getUserId() + ","
                + "\"fullName\":\"" + escape(u.getFullName()) + "\","
                + "\"email\":\"" + escape(u.getEmail()) + "\","
                + "\"phone\":\"" + escape(u.getPhone()) + "\","
                + "\"role\":\"" + escape(u.getRole()) + "\""
                + "}";
        resp.getWriter().print(json);
    }

    private String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
