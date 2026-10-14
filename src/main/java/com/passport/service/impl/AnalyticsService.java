package com.passport.service.impl;
import com.passport.dao.impl.AnalyticsDAOImpl;
import java.util.Map;
public class AnalyticsService { public Map<String,Object> summary() throws Exception { return new AnalyticsDAOImpl().summary(); } }
