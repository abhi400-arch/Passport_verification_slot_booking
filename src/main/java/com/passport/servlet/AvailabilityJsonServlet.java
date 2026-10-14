package com.passport.servlet;
import com.passport.service.impl.SlotServiceImpl;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
@WebServlet("/api/slots")
public class AvailabilityJsonServlet extends HttpServlet {
    private final SlotServiceImpl service=new SlotServiceImpl();
    protected void doGet(HttpServletRequest req,HttpServletResponse resp)throws IOException{
        resp.setContentType("application/json;charset=UTF-8");
        try{
            var slots=service.available();
            StringBuilder json=new StringBuilder("[");
            for(int i=0;i<slots.size();i++){
                var s=slots.get(i);
                if(i>0)json.append(",");
                json.append("{\"id\":").append(s.getSlotId()).append(",\"date\":\"").append(s.getSlotDate())
                    .append("\",\"start\":\"").append(s.getStartTime()).append("\",\"end\":\"").append(s.getEndTime())
                    .append("\",\"remaining\":").append(s.getCapacity()-s.getBookedCount()).append("}");
            }
            json.append("]");resp.getWriter().print(json);
        }catch(Exception e){resp.setStatus(500);resp.getWriter().print("{\"error\":\"Unable to load slots\"}");}
    }
}
