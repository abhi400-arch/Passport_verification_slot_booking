package com.passport.filter;

import com.passport.model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebFilter(urlPatterns={"/app/*","/admin/*"})
public class AuthFilter implements Filter {
    public void doFilter(ServletRequest req,ServletResponse res,FilterChain chain)throws IOException,ServletException{
        HttpServletRequest request=(HttpServletRequest)req;
        HttpServletResponse response=(HttpServletResponse)res;
        User u=(User)request.getSession().getAttribute("user");
        String path=request.getRequestURI().substring(request.getContextPath().length());
        if(u==null){response.sendRedirect(request.getContextPath()+"/login");return;}
        if(path.startsWith("/admin") && !"ADMIN".equals(u.getRole())){response.sendError(403,"Administrator access required.");return;}
        chain.doFilter(req,res);
    }
}
