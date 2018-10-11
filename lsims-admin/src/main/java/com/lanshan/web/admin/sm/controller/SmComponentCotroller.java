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
import com.lanshan.web.admin.sm.service.SmComponentService;
import com.lanshan.web.admin.sm.utils.DeptUtils;

@Controller
public class SmComponentCotroller extends BaseController{
	
	@Autowired
	public void setService(SmComponentService service){
		super.setService(service);
	}
	
	@DataProvider
	public void loadQueryResult(Page page, Map param) {
		String systemId = DeptUtils.getSystemIdByCurUser();
		param.put("systemId", systemId);
		super.loadQueryResult(page, param);
	}
	
	@DataProvider
	public List loadQueryResult(Map param) {
		String systemId = DeptUtils.getSystemIdByCurUser();
		param.put("systemId", systemId);
		return super.loadQueryResult(param);
	}
	
	@Expose
    @DataResolver
    public void delete(Integer id) { 
       ((SmComponentService) service).deleteComp(id);
    }
}
