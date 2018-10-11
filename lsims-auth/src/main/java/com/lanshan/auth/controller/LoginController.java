package com.lanshan.auth.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.WebUtils;

import com.lanshan.core.commons.CommonConstants;


/**
 * 登录退出控制器
 * @author qxs
 * @date 2017-06-12
 * **/
@Controller
public class LoginController {
	
	/**
	 * 登录
	 * **/
	@RequestMapping({"/login"})
	public String login(HttpServletRequest request){
		Cookie cookie = WebUtils.getCookie(request, CommonConstants.LOGIN_SYSTEM_NAME);
		if(cookie != null){
			String suffix = cookie.getValue();
			if(StringUtils.hasLength(suffix)){
				return "login/login" + suffix.substring(0,1).toUpperCase() + suffix.substring(1);
			}
		}
		
		return "login/login";
	}
}
