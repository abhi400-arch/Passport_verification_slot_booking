package com.passport.servlet;
import com.passport.model.User;
import com.passport.service.impl.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.*;
import java.io.IOException;
@WebServlet("/app/book")
public class BookingServlet extends HttpServlet {
    private final BookingServiceImpl bookings=new BookingServiceImpl();
    private final PassportServiceImpl passports=new PassportServiceImpl();
    protected void doPost(HttpServletRequest req,HttpServletResponse resp)throws IOException,ServletException{
        try{
            User u=(User)req.getSession().getAttribute("user");
            var p=passports.getByUser(u.getUserId());
            if(p==null)throw new IllegalStateException("Please save your passport details before booking a slot.");
            var b=bookings.book(u.getUserId(),p.getApplicationId(),Integer.parseInt(req.getParameter("slotId")));
            req.setAttribute("booking",b);req.getRequestDispatcher("/WEB-INF/views/confirmation.jsp").forward(req,resp);
        }catch(Exception e){req.setAttribute("error",e.getMessage());req.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(req,resp);}
    }
}
