package com.passport.service.impl;
import com.passport.dao.UserDAO;
import com.passport.dao.impl.UserDAOImpl;
import com.passport.exception.ValidationException;
import com.passport.model.User;
import com.passport.service.AuthService;
import com.passport.util.PasswordUtil;
import com.passport.util.ValidationUtil;

public class AuthServiceImpl implements AuthService {
    private final UserDAO dao=new UserDAOImpl();
    public int register(String name,String email,String phone,String password)throws Exception{
        ValidationUtil.validateRegistration(name,email,phone,password);
        email=email.trim().toLowerCase();
        if(dao.existsByEmail(email))throw new ValidationException("An account with this email already exists.");
        return dao.create(new User(name.trim(),email,phone.trim(),PasswordUtil.hash(password),"USER"));
    }
    public User login(String email,String password)throws Exception{
        if(email==null||password==null||email.isBlank()||password.isBlank())throw new ValidationException("Email and password are required.");
        User u=dao.findByEmail(email.trim().toLowerCase());
        if(u==null||!PasswordUtil.matches(password,u.getPasswordHash()))throw new ValidationException("Invalid email or password.");
        return u;
    }
}
