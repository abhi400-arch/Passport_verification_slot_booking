package com.passport.servlet;
import com.passport.model.*;
import com.passport.service.impl.PassportServiceImpl;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.*;
import java.io.IOException;
import java.time.LocalDate;
@WebServlet("/app/passport")
public class PassportServlet extends HttpServlet {
    private final PassportServiceImpl service=new PassportServiceImpl();
    protected void doGet(HttpServletRequest req,HttpServletResponse resp)throws IOException,ServletException{
        try{User u=(User)req.getSession().getAttribute("user");req.setAttribute("passport",service.getByUser(u.getUserId()));req.getRequestDispatcher("/WEB-INF/views/passport.jsp").forward(req,resp);}
        catch(Exception e){req.setAttribute("error",e.getMessage());req.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(req,resp);}
    }
    protected void doPost(HttpServletRequest req,HttpServletResponse resp)throws IOException,ServletException{
        try{
            User u=(User)req.getSession().getAttribute("user");
            PassportApplication p=new PassportApplication(u.getUserId(),req.getParameter("passportNumber"),
                LocalDate.parse(req.getParameter("dob")),req.getParameter("nationality"),req.getParameter("address"),req.getParameter("applicationType"));
            service.save(p);req.getSession().setAttribute("success","Passport details saved successfully.");resp.sendRedirect(req.getContextPath()+"/app/passport");
        }catch(Exception e){req.setAttribute("error",e.getMessage());doGet(req,resp);}
    }
}
