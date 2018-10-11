package com.lanshan.web.admin.sm.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.bstek.dorado.annotation.DataProvider;
import com.lanshan.core.util.SpringUtils;
import com.lanshan.web.admin.model.SmEnumCategorys;
import com.lanshan.web.admin.model.SmEnums;
import com.lanshan.web.admin.redis.RedisService;

/**
 * @ClassName: DictionaryHelper
 * @Description: 自动的帮助类，
 * @author peifg
 * @date 2017年1月4日17:05:30
 */
public class DictionaryHelper {
    //初始化实例
    private static DictionaryHelper instance;
    
    synchronized public static DictionaryHelper getInstance() {
        if (instance == null) {
            instance = new DictionaryHelper();
        }
        return instance;
    }
    
    
    public static String getEnums(String key) { 
    	List enums= getInstance().getResult(key);
    	return JsonHelper.parser(enums);
    }
    
    /**
     * @Description 根据字典的编码，来查询获得对应的字典项
     * @param key:字典编码
     * @return list:enums对象的集合
     */
    @DataProvider
    public List getResult(String key) { 
    	String systemId = DeptUtils.getSystemIdByCurUser();
    	RedisService redisTempalte = (RedisService) SpringUtils.getBean(RedisService.class);
    	SmEnumCategorys enumCategorys=(SmEnumCategorys) redisTempalte.get(SmConstant.REDIS_DICTIONARY + systemId, key);
        return enumCategorys==null?null:enumCategorys.getEnumses(); 
    }
    
    /**
     * @Description 根据字典的编码以及字典值的正则来查询获得对应的字典项
     * key：字典key;reg：正则表达式 如：^门店 以门店连个汉字开头
     * @param key:字典编码
     * @return list:enums对象的集合
     */
    @DataProvider
    public List getResultReg(String key,String reg) { 
    	String systemId = DeptUtils.getSystemIdByCurUser();
    	List<SmEnums> list = new ArrayList();
    	RedisService redisTempalte = (RedisService) SpringUtils.getBean(RedisService.class);
    	SmEnumCategorys enumCategorys=(SmEnumCategorys) redisTempalte.get(SmConstant.REDIS_DICTIONARY + systemId, key);
    	if(null != enumCategorys){
    		Pattern pattern = Pattern.compile(reg);
    	    // 忽略大小写的写法
    	    // Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
    		List<SmEnums> l = enumCategorys.getEnumses();
    		
    		for(SmEnums e:l){
    			Matcher matcher = pattern.matcher(e.getDesc());
    			
    			// 字符串是否与正则表达式相匹配
        	    if(matcher.matches()){
        	    	list.add(e);
        	    }
    		}
    	}
        return list; 
    }
    
    

    /**
     * @Description 根据字典的编码，来查询获得对应的字典项
     * @param key:字典编码
     * @param enumCode:字典项编码
     * @return list:enums对象的集合
     */
    @DataProvider
    public SmEnums getResult(String key,String enumCode) {
    	List<SmEnums> enums=getResult(key);
    	if(enums==null || enums.isEmpty()){
    		return null;
    	}
    	
    	for(SmEnums en:enums){
    		if(en.getName().equals(enumCode)){
				return en;
			}
    	} 
    	return null;
    } 
    
    /**
     * @Description 根据字典的编码，从查询出来字典列表中搜索code对应字典
     * @param List<Enums> list:字典列表
     * @param enumCode:字典项编码
     * @return Enums:字典对象
     */
    public SmEnums getResultFromList(List<SmEnums> list,String enumCode) {
    	for(SmEnums en:list){
    		if(en.getName().equals(enumCode)){
				return en;
			}
    	} 
    	return null;
    } 
    
    public String getMemo(String key,String enumCode) {
    	List<SmEnums> enums=getResult(key);
    	if(enums==null || enums.isEmpty()){
    		return null;
    	}
    	
    	for(SmEnums en:enums){
    		if(en.getName().equals(enumCode)){
				return en.getMemo();
			}
    	} 
    	return null;
    } 
}
