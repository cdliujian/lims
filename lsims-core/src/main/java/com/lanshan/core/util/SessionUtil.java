package com.lanshan.core.util;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.lanshan.core.business.IUser;
import com.lanshan.core.commons.CommonConstants;


/**
 * session工具类
 * **/
public class SessionUtil {
	
	
	
	/**
	 * 从session中获取user
	 * 
	 * @return SecurityUser
	 * **/
	public static IUser getSecurityUser(){
		ServletRequestAttributes attributes = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes());
		IUser user = (IUser) attributes.getAttribute(CommonConstants.LOGIN_USER,ServletRequestAttributes.SCOPE_SESSION);
		if(user!=null)
			return user;
		else
			return null;
		//Object o = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		//return null;
	}
	
	public static HttpServletRequest getRequest(){
		ServletRequestAttributes attributes = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes());
		return attributes.getRequest();
	}
}
