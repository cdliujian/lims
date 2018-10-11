package com.lanshan.web.admin.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.lanshan.core.business.IUser;
import com.lanshan.core.util.ReflectHelper;
import com.lanshan.core.util.SessionUtil;


@RestController
public class ApiController {
	@RequestMapping(value = "/api/{resource}/{postion}", method = {}) 
	public @ResponseBody Object execute(@PathVariable("resource") String resource,@PathVariable("postion") String postion,@RequestBody Map body) throws Exception {
		 
		 
 		 Object result=ReflectHelper.invokeService(resource, postion, body);
		 return  result==null?new HashMap():result;
	}
	
	@RequestMapping(value = "/api/currentUser", method = {}) 
	public @ResponseBody Object currentUser() throws Exception {
		 
		 
		IUser user = SessionUtil.getSecurityUser();
		 return  user;
	}
}
