package com.lanshan.core.wrapper; 

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.Assert;

import com.lanshan.core.json.AjaxResult;
 

/**
 * 封装返回结果
 * 
 */
public class WrapperResultHandler extends MappingJackson2HttpMessageConverter {  
	static final int SUCC_CODE=1000;
	
	private List<String> excludes = Collections.emptyList();
	
	public void setExcludes(List<String> excludes) {
		Assert.notEmpty(excludes, "'excludes' must not be empty");
		this.excludes = new ArrayList<String>(excludes);
	}
	@Override
	protected void writeInternal(Object object,Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {  
		if(needHandle(object,type)){ 
		   object=warpSuccess(object); 
		}
		super.writeInternal(object,type, outputMessage);
	}
	protected boolean canWrite(MediaType mediaType) {  
		return super.canWrite(mediaType);
	} 
	
	private AjaxResult warpSuccess(Object o){
		Map map=new HashMap();
		map.put("code", SUCC_CODE);
		map.put("data", o);
		AjaxResult ar = new AjaxResult().success(o);
		return ar; 
	}
	
	private boolean needHandle(Object object, Type type){
		for(String exItem:excludes){
			if(type.toString().indexOf(exItem)>0 || object.getClass().getName().indexOf(exItem)>0){
				return false;
			} 
		} 
		
		/**
		 * 以下这段逻辑 为了识别统一异常处理返回的map
		 */
		if(type==Map.class ||object instanceof Map ){ 
			if(((Map)object).containsKey("code") && ((Map)object).containsKey("message")){ 
				String code=((Map)object).get("code")+"";
				if(StringUtils.isNumeric(code) && code.length()==4){
					return false;
				} 
			}
		}  
		return true; 
	}
}
