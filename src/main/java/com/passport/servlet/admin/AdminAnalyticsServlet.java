package com.passport.servlet.admin;
import com.passport.service.impl.AnalyticsService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.*;
import java.io.IOException;
@WebServlet("/admin/analytics")
public class AdminAnalyticsServlet extends HttpServlet {
    private final AnalyticsService service=new AnalyticsService();
    protected void doGet(HttpServletRequest req,HttpServletResponse resp)throws IOException,ServletException{try{req.setAttribute("analytics",service.summary());req.getRequestDispatcher("/WEB-INF/views/admin/analytics.jsp").forward(req,resp);}catch(Exception e){req.setAttribute("error",e.getMessage());req.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(req,resp);}}
}
