package com.lanshan.web.admin.sm.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.data.provider.Page;
import com.lanshan.core.base.controller.BaseController;
import com.lanshan.web.admin.sm.service.SmRoleService;

@Controller
public class SmRoleCotroller extends BaseController {

	@Autowired
	public void setService(SmRoleService service) {
		super.setService(service);
	}

	@DataProvider
	public void loadQueryResult(Page page, Map param) {
		super.loadQueryResult(page, param);
	}

	@DataProvider
	public List loadQueryResult(Map param) {
		return super.loadQueryResult(param);
	}

}
