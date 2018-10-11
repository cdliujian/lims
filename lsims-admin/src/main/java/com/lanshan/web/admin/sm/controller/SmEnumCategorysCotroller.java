package com.lanshan.web.admin.sm.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Page;
import com.lanshan.core.base.controller.BaseController;
import com.lanshan.core.util.SessionUtil;
import com.lanshan.web.admin.model.SmEnumCategorys;
import com.lanshan.web.admin.model.SmEnums;
import com.lanshan.web.admin.redis.RedisService;
import com.lanshan.web.admin.sm.service.SmEnumCategorysService;
import com.lanshan.web.admin.sm.utils.DeptUtils;
import com.lanshan.web.admin.sm.utils.SmConstant;

@Controller
public class SmEnumCategorysCotroller extends BaseController {

	@Autowired
    private RedisService redisService;
	
	@Autowired
	public void setService(SmEnumCategorysService service) {
		super.setService(service);
	}
	
	@DataProvider
	public void loadQueryResult(Page page, Map param) {
		param.put("systemId", DeptUtils.getSystemIdByCurUser());
		super.loadQueryResult(page, param);
	}
	
	@DataProvider
	public List loadQueryResult(Map param) {
		param.put("systemId", DeptUtils.getSystemIdByCurUser());
		return super.loadQueryResult(param);
	}
	
	@DataResolver
    public void save(SmEnumCategorys obj) throws Exception {
        // 首先判断编码是否已经存在，如果存在则不允许保存
        String name = obj.getName();
        //由于com.bstek.dorado.data.entity.EntityList序列化原因，重新生成一个对象用于保存
    	ArrayList ens =new ArrayList();
    	
    	List list = obj.getEnumses();
    	
    	Set enIdSet=new HashSet();
    	
    	if(null!=list && !list.isEmpty()){
    		for(int i2=0;i2<list.size();i2++){
    			SmEnums enumItem=(SmEnums) list.get(i2);
        		enIdSet.add(enumItem.getName());
        		ens.add(enumItem);
        	} 
    		if(enIdSet.size() != list.size())
        		throw new Exception("同一字典下的字典项编码不能重复");
    	}
    	
    	obj.setEnumses(ens);
    	
    	String systemId = DeptUtils.getSystemIdByCurUser();
        Map param = new HashMap();
        param.put("nameEq", name);
        param.put("systemId", systemId);
        obj.setSystemId(systemId);
        obj.setLastUpdatePerson(SessionUtil.getSecurityUser().getUsername());
        Date now = new Date();
        obj.setLastUpdateTime(now);
        //如果是更新操作时，判断编码重复需要去掉本身        
        List l = service.queryList(param);
        if(l == null || l.isEmpty()){
        	obj.setCreateDate(now);
            service.save(obj);
        }else{
        	if(obj.getId() == null || obj.getId() == 0){
        		throw new RuntimeException("字典名称:" + obj.getName() + "已存在，请直接修改!");
        	}
        	obj.setId(((SmEnumCategorys)(l.get(0))).getId());
        	obj.setLastUpdatePerson(SessionUtil.getSecurityUser().getUsername());
        	obj.setLastUpdateTime(new Date());
            service.update(obj);
        } 
        obj.setEnumses(ens);
        redisService.set(SmConstant.REDIS_DICTIONARY + systemId, obj.getName(), obj);
    }
	
	@DataResolver
    public void delete(SmEnumCategorys o) {
		String systemId = DeptUtils.getSystemIdByCurUser();
		redisService.del(SmConstant.REDIS_DICTIONARY + systemId, o.getName());
        service.delete(o);
    }
}
