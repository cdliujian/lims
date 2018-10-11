package com.lanshan.web.admin.sm.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.provider.Page;
import com.lanshan.core.base.controller.BaseController;
import com.lanshan.web.admin.model.SmEnumCategorys;
import com.lanshan.web.admin.model.SmEnums;
import com.lanshan.web.admin.redis.RedisService;
import com.lanshan.web.admin.sm.service.SmEnumCategorysService;
import com.lanshan.web.admin.sm.service.SmEnumsService;
import com.lanshan.web.admin.sm.utils.DeptUtils;
import com.lanshan.web.admin.sm.utils.SmConstant;

@Controller
public class SmEnumsCotroller extends BaseController {
	
	@Autowired
    private RedisService redisService;
	
	@Autowired
	private SmEnumCategorysService smEnumCategorysService;
	
	@Autowired
	public void setService(SmEnumsService service) {
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
	
	/**
	 * @根据字典项来获得所有的字典项
	 * @param categoryId：字典编码ID
	 * @return Enums:字典项集合
	 */
	@DataProvider
	public List<SmEnums> getEnumsByCategoryId(Integer categoryId) {
		String systemId = DeptUtils.getSystemIdByCurUser();
		return ((SmEnumsService) service).getEnumsByCategoryId(categoryId, systemId);
	}
	
	/**
	 * 
	 * @param key:前台传过来的key是SmEnums的id + @@ + categoryId
	 */
	@Expose
	public void deleteEnumById(String key) {
		String[] keys = key.split("@@");
		Integer id = Integer.parseInt(keys[0]);
		Integer pid = Integer.parseInt(keys[1]);
		//SmEnums的categoryId为SmEnumCategorys的id
		SmEnumCategorys smEnumCategorys = (SmEnumCategorys) smEnumCategorysService.findById(pid);
		//数据字典存储时，key为SmEnumCategorys的name
		SmEnumCategorys categorys = (SmEnumCategorys) redisService.get(SmConstant.REDIS_DICTIONARY, smEnumCategorys.getName());
		ArrayList copyList = new ArrayList();
		if(categorys != null){
			//遍历，去掉需要删掉的SmEnums覆盖原来的redis存储
			for(Object obj: categorys.getEnumses()){
				SmEnums smEnums = (SmEnums)obj;
				if(smEnums.getId() != id){
					copyList.add(smEnums);
				}
			}
			categorys.setEnumses(copyList);
			redisService.set(SmConstant.REDIS_DICTIONARY, smEnumCategorys.getName(), categorys);
		}
		service.delete(service.findById(id));
	}
}
