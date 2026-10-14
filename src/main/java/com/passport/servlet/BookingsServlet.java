package com.passport.servlet;
import com.passport.model.User;
import com.passport.service.impl.BookingServiceImpl;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.*;
import java.io.IOException;
@WebServlet("/app/bookings")
public class BookingsServlet extends HttpServlet {
    private final BookingServiceImpl service=new BookingServiceImpl();
    protected void doGet(HttpServletRequest req,HttpServletResponse resp)throws IOException,ServletException{
        try{User u=(User)req.getSession().getAttribute("user");req.setAttribute("bookings",service.byUser(u.getUserId()));req.getRequestDispatcher("/WEB-INF/views/bookings.jsp").forward(req,resp);}
        catch(Exception e){req.setAttribute("error",e.getMessage());req.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(req,resp);}
    }
}
