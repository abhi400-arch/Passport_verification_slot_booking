package com.passport.servlet;
import com.passport.model.User;
import com.passport.service.impl.AuthServiceImpl;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.*;
import java.io.IOException;
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private final AuthServiceImpl service=new AuthServiceImpl();
    protected void doGet(HttpServletRequest req,HttpServletResponse resp)throws IOException,ServletException{
        Object s=req.getSession().getAttribute("success");if(s!=null){req.setAttribute("success",s);req.getSession().removeAttribute("success");}
        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req,resp);
    }
    protected void doPost(HttpServletRequest req,HttpServletResponse resp)throws IOException,ServletException{
        try{User u=service.login(req.getParameter("email"),req.getParameter("password"));req.getSession().setAttribute("user",u);
            if("ADMIN".equals(u.getRole()))resp.sendRedirect(req.getContextPath()+"/admin/dashboard");
            else resp.sendRedirect(req.getContextPath()+"/app/dashboard");
        }catch(Exception e){req.setAttribute("error",e.getMessage());req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req,resp);}
    }
}
