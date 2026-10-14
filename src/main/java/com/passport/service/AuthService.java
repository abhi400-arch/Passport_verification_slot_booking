package com.passport.service;

import com.passport.exception.ValidationException;
import com.passport.model.User;

public interface AuthService {
    int register(String name,String email,String phone,String password) throws Exception;
    User login(String email,String password) throws Exception;
}
