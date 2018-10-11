package com.lanshan.web.admin.sm.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Page;
import com.lanshan.core.base.controller.BaseController;
import com.lanshan.core.util.SessionUtil;
import com.lanshan.web.admin.model.SmSystem;
import com.lanshan.web.admin.sm.service.SmSystemService;

/**
 * 
 * @Description 系统标识Cotroller
 *
 * @author caoying
 * 2018年9月12日 上午10:00:39
 */
@Controller
public class SmSystemCotroller extends BaseController {
	@Autowired
	public void setService(SmSystemService service){
		super.setService(service);
	}
	
	/**
	 * 
	 * @Description 查询系统标识 
	 * @param page
	 * @param param
	 * void
	 * @author caoying
	 * 2018年9月12日上午10:00:55
	 */
	@DataProvider
	public void loadQueryResult(Page page, Map param) {
		// TODO Auto-generated method stub
		super.loadQueryResult(page, param);
	}

	/**
	 * 
	 * @Description 保存系统标识 
	 * @param obj
	 * @throws Exception
	 * void
	 * @author caoying
	 * 2018年9月12日上午10:00:55
	 */
	@DataResolver
	public void save(SmSystem obj) throws Exception {
		// 判断系统标识编码是否已经存在
		HashMap amap = new HashMap();
		amap.put("systemId", obj.getSystemId());
		
		List list = service.queryList(amap);
		if (list.size() > 0) {
			throw new Exception("系统标识编码已存在！");
		}
		// 判断系统标识名称是否已经存在
		amap = new HashMap();
		amap.put("desc", obj.getDesc());
		
		list = service.queryList(amap);
		if (list.size() > 0) {
			throw new Exception("系统标识名称已存在！");
		}
		
		String userName = SessionUtil.getSecurityUser().getCname(); //获取登录用户名
		obj.setLastUpdatePerson(userName);
		obj.setLastUpdateTime(new Date());
		obj.setCreateDate(new Date());
		
		service.save(obj);
	}
}
