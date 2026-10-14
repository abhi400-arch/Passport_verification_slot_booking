package com.passport.servlet;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.*;
import java.io.IOException;
@WebServlet("")
public class HomeServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req,HttpServletResponse resp)throws IOException,ServletException{
        resp.sendRedirect(req.getContextPath()+"/home");
    }
}
