package com.lanshan.web.admin.sm.utils;

import java.util.Map;

import com.bstek.dorado.core.el.ContextVarsInitializer;

public class ViewVarsInitializer implements ContextVarsInitializer{

	@Override
	public void initializeContext(Map<String, Object> map) throws Exception {
		// TODO Auto-generated method stub
		//map.put("FworkContextHelper", FworkContextHelper.getInstance());
		
		//System.out.println(	HttpHelper.getServletContext().getContextPath() );
		//map.put("ContextPath", HttpHelper.getServletContext().getContextPath());

	    map.put("CommonUtils", CommonUtils.getInstance()); 
	    map.put("DictionaryHelper", DictionaryHelper.getInstance());
		map.put("PreferencesHelper", PreferencesHelper.getInstance());
	}


}
