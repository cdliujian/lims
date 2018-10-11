package com.lanshan.web.admin.sm.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.lanshan.core.util.SessionUtil;
import com.lanshan.core.util.SpringUtils;
import com.lanshan.web.admin.model.SmDept;
import com.lanshan.web.admin.model.SmUser;
import com.lanshan.web.admin.sm.service.SmUserService;

public class DeptUtils {
	
	/**
	 * 根据当前用户获取smDept
	 * @return
	 */
	public static SmDept getSmDeptByCurUser(){
		SmUser user = getCurUser();
		if(user == null){
			return null;
		}else{
			return user.getDept();
		}
	}
	
	/**
	 * 根据当前用户获取SystemId
	 * @return
	 */
	public static String getSystemIdByCurUser(){
		SmDept dept = getSmDeptByCurUser();
		String systemId = dept == null ? null : dept.getSystemId();
		return systemId;
	}
	
	public static SmUser getCurUser(){
		String username = SessionUtil.getSecurityUser().getUsername();
		Map param = new HashMap();
		param.put("usernameEq", username);
		SmUserService userService = (SmUserService)SpringUtils.getBean(SmUserService.class);
		List users = userService.queryList(param);
		return users == null || users.isEmpty() ? null : (SmUser) users.get(0);
	}
}
