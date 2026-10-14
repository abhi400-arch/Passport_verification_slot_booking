package com.passport.service.impl;

import com.passport.dao.impl.PassportDAOImpl;
import com.passport.dao.impl.SlotDAOImpl;
import com.passport.model.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class SmartAssistantService {
    private final PassportDAOImpl passportDAO=new PassportDAOImpl();
    private final SlotDAOImpl slotDAO=new SlotDAOImpl();

    public ReadinessResult readiness(int userId) throws Exception {
        PassportApplication p=passportDAO.findByUserId(userId);
        List<String> done=new ArrayList<>(), missing=new ArrayList<>();
        if(p==null) { missing.add("Passport profile"); return new ReadinessResult(0,done,missing); }
        check(p.getPassportNumber(),"Passport number",done,missing);
        check(p.getDateOfBirth(),"Date of birth",done,missing);
        check(p.getNationality(),"Nationality",done,missing);
        check(p.getAddress(),"Address",done,missing);
        check(p.getApplicationType(),"Application type",done,missing);
        return new ReadinessResult(done.size()*20,done,missing);
    }
    private void check(Object value,String label,List<String> done,List<String> missing){
        if(value!=null && (! (value instanceof String) || !((String)value).isBlank())) done.add(label); else missing.add(label);
    }

    public List<SlotRecommendation> recommend(String preferredDate,String preferredTime) throws Exception {
        LocalDate date=null; LocalTime time=null;
        if(preferredDate!=null && !preferredDate.isBlank()) date=LocalDate.parse(preferredDate);
        if(preferredTime!=null && !preferredTime.isBlank()) time=LocalTime.parse(preferredTime);
        final LocalDate d=date; final LocalTime t=time;
        return slotDAO.findAvailable().stream().map(s -> {
            int score=50; List<String> reasons=new ArrayList<>();
            if(d!=null){ long diff=Math.abs(java.time.temporal.ChronoUnit.DAYS.between(d,s.getSlotDate())); score += Math.max(0,25-(int)Math.min(25,diff*5)); if(diff==0) reasons.add("matches your preferred date"); }
            if(t!=null){ long mins=Math.abs(java.time.Duration.between(t,s.getStartTime()).toMinutes()); score += Math.max(0,20-(int)Math.min(20,mins/30*5)); if(mins<=30) reasons.add("matches your preferred time"); }
            int remaining=s.getCapacity()-s.getBookedCount();
            score += Math.min(5,remaining); if(remaining>=3) reasons.add("good availability");
            if(reasons.isEmpty()) reasons.add("best available option based on current slots");
            return new SlotRecommendation(s,Math.min(100,score),String.join(", ",reasons));
        }).sorted(Comparator.comparingInt(SlotRecommendation::getScore).reversed()).limit(3).collect(Collectors.toList());
    }
}
