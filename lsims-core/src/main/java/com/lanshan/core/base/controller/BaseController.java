package com.lanshan.core.base.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bstek.dorado.data.provider.Page;
import com.lanshan.core.base.service.BaseService;

public abstract class BaseController {

	protected BaseService service;
	
	public void setService(BaseService service){
		this.service = service;
	}
	
	public void loadQueryResult(Page page,Map param)
	{
		param = param == null ? new HashMap() : param;
		correctQueryParam(param);
		param.put("LIMIT_ROWSTART", (page.getPageNo() - 1) * page.getPageSize() );
		param.put("LIMIT_ROWNUM", page.getPageSize() );
		
		page.setEntityCount(service.count(param));
		page.setEntities(service.queryList(param));
	}
	
	public List loadQueryResult(Map param)
	{
		param = param == null ? new HashMap() : param;
		correctQueryParam(param);		
		return service.queryList(param);
	}
	
	protected void correctQueryParam(Map param)
	{
		
	}
}
