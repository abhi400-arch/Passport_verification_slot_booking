package com.passport.servlet;

import com.passport.model.User;
import com.passport.service.impl.SmartAssistantService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.*;
import java.io.IOException;

@WebServlet("/app/smart-assistant")
public class SmartAssistantServlet extends HttpServlet {
    private final SmartAssistantService service=new SmartAssistantService();
    protected void doGet(HttpServletRequest req,HttpServletResponse resp)throws IOException,ServletException{
        try{User u=(User)req.getSession().getAttribute("user");req.setAttribute("readiness",service.readiness(u.getUserId()));req.getRequestDispatcher("/WEB-INF/views/smart-assistant.jsp").forward(req,resp);}
        catch(Exception e){req.setAttribute("error",e.getMessage());req.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(req,resp);}
    }
    protected void doPost(HttpServletRequest req,HttpServletResponse resp)throws IOException,ServletException{
        try{req.setAttribute("readiness",service.readiness(((User)req.getSession().getAttribute("user")).getUserId()));req.setAttribute("recommendations",service.recommend(req.getParameter("preferredDate"),req.getParameter("preferredTime")));req.getRequestDispatcher("/WEB-INF/views/smart-assistant.jsp").forward(req,resp);}
        catch(Exception e){req.setAttribute("error",e.getMessage());doGet(req,resp);}
    }
}
