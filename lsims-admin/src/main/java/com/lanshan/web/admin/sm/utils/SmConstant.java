package com.lanshan.web.admin.sm.utils;

/**
 * 
 * @Description 数据常量类
 *
 * @author caoying
 * 2018年9月6日 上午10:28:30
 */
public class SmConstant {
	//性别
	public static String GENDER_MALE = "0"; // 男
	public static String GENDER_FEMALE = "1"; // 女
	
	//区域类别
	public static String REGION_TYPE_COUNTRY = "0"; //国家
	public static String REGION_TYPE_PROVINCE = "1"; //省份
	public static String REGION_TYPE_CITY = "2"; //市
	public static String REGION_TYPE_AREA = "3"; //区
	
	//数据字典存入redis的key
	public static final String REDIS_DICTIONARY = "DICT#";
	//系统参数存入redis的key
	public static final String REDIS_PREFERENCE = "PREFERENCE#";
	//角色信息存入redis的key
	public static final String REDIS_ROLE = "ROLE#";
	
	//每页默认行数
	public static final int DEFAULT_PAGE_SIZE=15;
	//手机端的每页记录数
	public static final int DEFAULT_PAGE_SIZE_APP=10;  
	
	public static final String DATA_SCOPE_CURRENT="1";      //数据范围：本级
	public static final String DATA_SCOPE_CASCADE="2";      //数据范围：本级及以下
	
	//默认角色名称
	public static final String DEFAULT_ROLE_NAME = "defaultRole";
}
