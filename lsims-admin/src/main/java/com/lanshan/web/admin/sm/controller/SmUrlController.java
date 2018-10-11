package com.lanshan.web.admin.sm.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.provider.Page;
import com.lanshan.core.base.controller.BaseController;
import com.lanshan.web.admin.model.SmUrl;
import com.lanshan.web.admin.sm.service.SmUrlService;
import com.lanshan.web.admin.sm.utils.DeptUtils;

@Controller
public class SmUrlController extends BaseController {

	@Autowired
	public void setService(SmUrlService service) {
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
	public void save(SmUrl smUrl){
		if(StringUtils.isBlank(smUrl.getCode())){
        	throw new RuntimeException("菜单编码不能为空!");
        }
		if(smUrl.getOrder() == null){
			throw new RuntimeException("请指定该菜单的排序!");
        }
		if(StringUtils.isBlank(smUrl.getUrl())){
        	if(smUrl.getComponents() != null && !smUrl.getComponents().isEmpty()){
        		throw new RuntimeException("菜单【"+smUrl.getName()+"】似乎不是页面资源，请删除按钮资源后提交");
        		
        	}
        }
		String systemId = DeptUtils.getSystemIdByCurUser();
        //-------------判断菜单名称和编码不能重复-------------------
        String s = judgeExist(smUrl.getName(), smUrl.getId(), systemId);
        if(StringUtils.isNotEmpty(s)){
        	throw new RuntimeException(s);
        }
        
        s = judgeExistCode(smUrl.getCode(), smUrl.getId(), systemId);
        if(StringUtils.isNotEmpty(s)){
        	throw new RuntimeException(s); 
        }
        //-------------判断菜单名称和编码不能重复-------------------
        smUrl.setSystemId(systemId);
        //id为空新增，否则更新
        if(smUrl.getId() == null){
            service.save(smUrl);                
        }else{
            service.update(smUrl);
        }
	}
	
	@Expose
	public void delete(Integer id){
		if(id != null){
			service.delete( service.findById(id));
		}
	}
	
	@DataProvider
	public String judgeExistCode(String code, Integer urlId, String systemId){
		if(StringUtils.isEmpty(code))
			return null;
		Map term = new HashMap();
		term.put("systemId", systemId);
		term.put("code", code);	
		//在更新操作时判断重复时要去掉本身
		if(urlId != null){
            term.put("idNeq", urlId);
        }
		if(service.queryList(term).size()>0)
			return "此菜单编码["+code+"]已经存在！";
		else
			return null;
	}
	
	@Expose
	public String judgeExist(String name, Integer urlId, String systemId){
		Map term = new HashMap();
		term.put("systemId", systemId);
		term.put("nameEq", name);
		//在更新操作时判断重复时要去掉本身
		if(urlId != null){
		    term.put("idNeq", urlId);
		}
		if(service.queryList(term).size()>0)
			return "菜单名称["+name+"]已经存在！";
		else
			return null;
	}
	
	@DataProvider
	public List<SmUrl> loadUrlsEx(Integer parentId) throws Exception {
		Map term = new HashMap();
		term.put("systemId", DeptUtils.getSystemIdByCurUser());
		if (parentId == null)
			term.put("parentIsNull", "1"); 	
		else
			term.put("parentId", parentId); 
		return service.queryList(term);
	}
}

