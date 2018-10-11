package com.lanshan.web.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lanshan.core.business.IUser;
import com.lanshan.core.util.SessionUtil;


/***
 * 首页控制器
 * **/
@Controller
public class IndexController {
	
	@RequestMapping({"/","/index"})
	public String index(Model model){
		IUser user = SessionUtil.getSecurityUser();
		model.addAttribute("user", user);
		
		return "index";
	}
	
	@RequestMapping("/user/index")
	public String userIndex(Model model){
		IUser user = SessionUtil.getSecurityUser();
		model.addAttribute("user", user);
		
		return "userIndex";
	}
	
	@RequestMapping("/template/{resource}")
	public String page (@PathVariable("resource") String resource){
		return resource;
	}
}
