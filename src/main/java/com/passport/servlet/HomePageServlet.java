package com.passport.servlet;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.*;
import java.io.IOException;
@WebServlet("/home")
public class HomePageServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req,HttpServletResponse resp)throws IOException,ServletException{
        req.getRequestDispatcher("/WEB-INF/views/home.jsp").forward(req,resp);
    }
}
