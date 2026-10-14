package com.passport.service;
import com.passport.model.PassportApplication;
public interface PassportService {
    PassportApplication getByUser(int userId) throws Exception;
    void save(PassportApplication application) throws Exception;
}
