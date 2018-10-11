package com.lanshan.web.admin.sm.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.data.provider.Page;
import com.lanshan.core.base.controller.BaseController;
import com.lanshan.web.admin.model.SmRegion;
import com.lanshan.web.admin.sm.service.SmRegionService;

@Controller
public class SmRegionCotroller extends BaseController{
	
	@Autowired
	public void setService(SmRegionService service){
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
	
	/**
	 * @Description 根据ID返回上级区域信息
	 * @param id
	 * @return Region
	 */
	@DataProvider
	public SmRegion getParentRegionById(Integer id) {
		return ((SmRegionCotroller) service).getParentRegionById(id);

	}

	/**
	 * @Description 查询指定区域的下级区域
	 * @param id
	 * @return List
	 */
	@DataProvider
	public List getChildsById(Integer id) {
		HashMap amap = new HashMap();
		amap = new HashMap();
		amap.put("parentId", id);
		return service.queryList(amap);
	}

	@DataProvider
	public SmRegion getRegionById(Map map) {
		return (SmRegion) service.findById(map.get("id"));
	}
}
