package com.passport.servlet;
import com.passport.service.impl.AuthServiceImpl;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.*;
import java.io.IOException;
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private final AuthServiceImpl service=new AuthServiceImpl();
    protected void doGet(HttpServletRequest req,HttpServletResponse resp)throws IOException,ServletException{req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req,resp);}
    protected void doPost(HttpServletRequest req,HttpServletResponse resp)throws IOException,ServletException{
        try{service.register(req.getParameter("name"),req.getParameter("email"),req.getParameter("phone"),req.getParameter("password"));
            req.getSession().setAttribute("success","Registration successful. Please log in.");resp.sendRedirect(req.getContextPath()+"/login");
        }catch(Exception e){req.setAttribute("error",e.getMessage());req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req,resp);}
    }
}
