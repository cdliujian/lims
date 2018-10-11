package com.lanshan.web.admin.sm.utils;

import com.lanshan.core.util.SpringUtils;
import com.lanshan.web.admin.model.SmPreferences;
import com.lanshan.web.admin.redis.RedisService;

public class PreferencesHelper {	
    private static PreferencesHelper _instance = null;
    synchronized public static PreferencesHelper getInstance() {
        if (_instance == null) {
            _instance = new PreferencesHelper();
        }
        return _instance;
    }

    /**
     * @根据系统参数key值来获得对于value
     * @param key
     * @return
     */
    public static String getPreferenceValue(String key) {
    	String systemId = DeptUtils.getSystemIdByCurUser();
    	RedisService redisTempalte = (RedisService) SpringUtils.getBean(RedisService.class);
    	SmPreferences p = (SmPreferences) redisTempalte.get(SmConstant.REDIS_PREFERENCE + systemId, key); 
    	
    	return p==null?null:p.getValue(); 
    }

    public static int getPagesize() {
    	String s =getPreferenceValue("SYS_RECORDPERPAGE");
    	return s==null?SmConstant.DEFAULT_PAGE_SIZE:Integer.valueOf(s);
    }
    
    public static int getPagesizeApp() {
    	String s =getPreferenceValue("SYS_RECORDPERPAGE_APP");
    	return s==null?SmConstant.DEFAULT_PAGE_SIZE_APP:Integer.valueOf(s);
    }
    
    public static int getDeptId()
    {
    	return DeptUtils.getSmDeptByCurUser().getId();
    }
}
