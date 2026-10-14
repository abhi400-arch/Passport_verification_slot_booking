package com.passport.servlet;
import com.passport.service.impl.SlotServiceImpl;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.*;
import java.io.IOException;
@WebServlet("/app/slots")
public class SlotsServlet extends HttpServlet {
    private final SlotServiceImpl service=new SlotServiceImpl();
    protected void doGet(HttpServletRequest req,HttpServletResponse resp)throws IOException,ServletException{
        try{req.setAttribute("slots",service.available());req.getRequestDispatcher("/WEB-INF/views/slots.jsp").forward(req,resp);}
        catch(Exception e){req.setAttribute("error",e.getMessage());req.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(req,resp);}
    }
}
