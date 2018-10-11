package com.lanshan.web.admin.sm.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;


import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Page;
import com.lanshan.core.base.controller.BaseController;
import com.lanshan.core.util.SessionUtil;
import com.lanshan.web.admin.model.SmPreferences;
import com.lanshan.web.admin.redis.RedisService;
import com.lanshan.web.admin.sm.service.SmPreferencesService;
import com.lanshan.web.admin.sm.utils.DeptUtils;
import com.lanshan.web.admin.sm.utils.SmConstant;

@Controller
public class SmPreferencesColtroller extends BaseController {
	
	@Autowired
    private RedisService redisService;
	
	private static final String KEY_CAN_NOT_BE_EMPTY = "key不能为空!";
	
	@Autowired
	public void setService(SmPreferencesService service){
		super.setService(service);
	}
	
	@DataResolver
	public void save(SmPreferences smPreferences) throws Exception{
		String key = smPreferences.getKey();
		if(StringUtils.isEmpty(key)){
			throw new Exception(KEY_CAN_NOT_BE_EMPTY);
		}
		String systemId = DeptUtils.getSystemIdByCurUser();
		smPreferences.setSystemId(systemId);
		String username = SessionUtil.getSecurityUser().getUsername();
		Date now = new Date();
		smPreferences.setCreateDate(now);
		smPreferences.setLastUpdatePerson(username);
		smPreferences.setLastUpdateTime(now);
		Map map = new HashedMap();
		map.put("systemId", systemId);
		map.put("keyEq", key);
		List l = service.queryList(map);
		if(l == null || l.isEmpty()){
			service.save(smPreferences);
		}else{
			//key存在则修改原来的
			if(smPreferences.getId() == null){
				smPreferences.setId(((SmPreferences)l.get(0)).getId());
			}
			service.update(smPreferences);
		}
		//刷新redis中的数据  
		redisService.set(SmConstant.REDIS_PREFERENCE + systemId, smPreferences.getKey(), smPreferences);
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
	public void delete(SmPreferences smPreferences){
		Object id = smPreferences.getId();
		//删除redis中的数据
        service.delete(service.findById(id));
        redisService.del(SmConstant.REDIS_PREFERENCE + DeptUtils.getSystemIdByCurUser(), smPreferences.getKey());
	}
}
