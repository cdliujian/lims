package com.lanshan.web.admin.sm.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.provider.Page;
import com.lanshan.core.base.controller.BaseController;
import com.lanshan.web.admin.model.SmRole;
import com.lanshan.web.admin.sm.service.SmRoleService;
import com.lanshan.web.admin.sm.utils.DeptUtils;

@Controller
public class SmRoleController extends BaseController {

	@Autowired
	public void setService(SmRoleService service) {
		super.setService(service);
	}
	
	@DataProvider
	public void loadQueryResult(Page page, Map params){
		params.put("systemId", DeptUtils.getSystemIdByCurUser());
		super.loadQueryResult(page, params);
	}
	
	@DataProvider
	public List loadQueryResult(Map param) {
		param.put("systemId", DeptUtils.getSystemIdByCurUser());
	    return super.loadQueryResult(param);
	}
	
	@DataResolver
	public void save(SmRole role){
		((SmRoleService)service).saveRole(role);
	}
	
	@Expose
	public void deleteRole(Integer id){
		((SmRoleService)service).deleteRole(id);
	}
	
	@Expose
	public String loadResIds(Integer roleId){
		return ((SmRoleService)service).getResourceIds(roleId);
	}
	
	@Expose
	public List loadResNames(Integer roleId){
		return ((SmRoleService)service).queryResourceName(roleId);
	}
}
