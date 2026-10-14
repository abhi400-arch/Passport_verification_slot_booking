package com.passport.servlet.admin;
import com.passport.service.impl.BookingServiceImpl;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.*;
import java.io.IOException;
@WebServlet("/admin/bookings")
public class AdminBookingsServlet extends HttpServlet {
    private final BookingServiceImpl service=new BookingServiceImpl();
    protected void doGet(HttpServletRequest req,HttpServletResponse resp)throws IOException,ServletException{
        try{req.setAttribute("bookings",service.all());req.getRequestDispatcher("/WEB-INF/views/admin/bookings.jsp").forward(req,resp);}
        catch(Exception e){req.setAttribute("error",e.getMessage());req.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(req,resp);}
    }
}
