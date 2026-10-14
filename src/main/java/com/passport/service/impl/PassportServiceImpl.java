package com.passport.service.impl;
import com.passport.dao.PassportDAO;
import com.passport.dao.impl.PassportDAOImpl;
import com.passport.model.PassportApplication;
import com.passport.service.PassportService;
import com.passport.util.ValidationUtil;
public class PassportServiceImpl implements PassportService {
    private final PassportDAO dao=new PassportDAOImpl();
    public PassportApplication getByUser(int userId)throws Exception{return dao.findByUserId(userId);}
    public void save(PassportApplication p)throws Exception{
        ValidationUtil.validatePassport(p.getPassportNumber(),p.getDateOfBirth(),p.getNationality(),p.getAddress(),p.getApplicationType());
        p.setPassportNumber(p.getPassportNumber().trim().toUpperCase());
        dao.saveOrUpdate(p);
    }
}
