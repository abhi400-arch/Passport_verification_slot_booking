package com.passport.servlet.admin;
import com.passport.dao.impl.UserDAOImpl;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.*;
import java.io.IOException;
@WebServlet("/admin/users")
public class AdminUsersServlet extends HttpServlet {
    private final UserDAOImpl dao=new UserDAOImpl();
    protected void doGet(HttpServletRequest req,HttpServletResponse resp)throws IOException,ServletException{
        try{req.setAttribute("users",dao.findAll());req.getRequestDispatcher("/WEB-INF/views/admin/users.jsp").forward(req,resp);}
        catch(Exception e){req.setAttribute("error",e.getMessage());req.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(req,resp);}
    }
}
