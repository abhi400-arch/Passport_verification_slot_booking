package com.passport.servlet;

import com.passport.model.PassportApplication;
import com.passport.model.User;
import com.passport.service.impl.PassportServiceImpl;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/api/passport")
public class PassportJsonServlet extends HttpServlet {
    private final PassportServiceImpl service=new PassportServiceImpl();
    protected void doGet(HttpServletRequest req,HttpServletResponse resp)throws IOException{
        resp.setContentType("application/json;charset=UTF-8");
        try{
            User u=(User)req.getSession().getAttribute("user");
            if(u==null){resp.setStatus(401);resp.getWriter().print("{\"error\":\"Login required\"}");return;}
            PassportApplication p=service.getByUser(u.getUserId());
            if(p==null){resp.setStatus(404);resp.getWriter().print("{\"error\":\"Passport details not found\"}");return;}
            String json="{\"applicationId\":"+p.getApplicationId()+",\"passportNumber\":"+q(p.getPassportNumber())+",\"dateOfBirth\":"+q(p.getDateOfBirth().toString())+",\"nationality\":"+q(p.getNationality())+",\"address\":"+q(p.getAddress())+",\"applicationType\":"+q(p.getApplicationType())+"}";
            resp.getWriter().print(json);
        }catch(Exception e){resp.setStatus(500);resp.getWriter().print("{\"error\":\"Unable to load passport details\"}");}
    }
    private String q(String s){return "\""+s.replace("\\","\\\\").replace("\"","\\\"")+"\"";}
}
