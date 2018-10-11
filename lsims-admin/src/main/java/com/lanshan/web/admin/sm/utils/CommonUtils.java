package com.lanshan.web.admin.sm.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.lanshan.core.util.SpringUtils;
import com.lanshan.web.admin.model.SmEnums;
import com.lanshan.web.admin.sm.service.SmSystemService;

/**
 * 
 * @Description 工具类
 *
 * @author caoying
 * 2018年9月3日 下午5:57:49
 */
public class CommonUtils {
	// 初始化实例
	private static CommonUtils instance;
	private static DictionaryHelper d = DictionaryHelper.getInstance();
	private static PreferencesHelper p = PreferencesHelper.getInstance();

	private CommonUtils() {

	}

	synchronized public static CommonUtils getInstance() {
		if (instance == null) {
			instance = new CommonUtils();
		}

		return instance;
	}
	
	/**
	 * 
	 * @Description 判断手机号格式是否正确 
	 * @param mobile 手机号码
	 * @return
	 * Boolean
	 * @author caoying
	 * 2018年9月3日下午5:57:08
	 */
	public static Boolean isMobileNo(String mobile) {
		if (mobile == null) {
			return Boolean.valueOf(false);
		}

		if (!(mobile.matches("^1[3|4|5|6|7|8|9][0-9]\\d{8}$"))) {
			return Boolean.valueOf(false);
		}
		return Boolean.valueOf(true);
	}
	
	/**
	 * 
	 * @Description 获取系统标识 
	 * @return
	 * List
	 * @author caoying
	 * 2018年9月5日上午11:47:21
	 */
//	@DataProvider
	public List querySystemListAll() {
		SmSystemService service = (SmSystemService) SpringUtils.getBean(SmSystemService.class);
		String systemId = DeptUtils.getSystemIdByCurUser(); // 根据登录人获取系统标识
		//如果登录人所属机构为基础系统，则查询所有系统标识，否则查询登录人所属机构对应的系统标识
		Map map = new HashMap();
		if(StringUtils.isNotEmpty(systemId) && !"lsims".equals(systemId)) {
			map.put("systemId", systemId);
		}
		List list = service.queryList(map);
		return list;
	}

	// ------------------字典相关函数--------------begin
	/**
	 * @Description 获得字典编码相应的所有字典项
	 * @param key:字典编码
	 * @return list:enums 字典项值
	 */
	public List getDicResult(String key) {
		return d.getResult(key);
	}

	public String getDicDesc(String key, String name) {
		SmEnums en = d.getResult(key, name);
		return en.getDesc();
	}

	// ------------------字典相关函数--------------begin

	// ------------------系统参数相关函数--------------begin
	/**
	 * @Description 获得系统参数
	 * @param key:系统参数编码
	 * @return 系统参数编码对应的值
	 */
	public String getPreferenceValue(String key) {
		return p.getPreferenceValue(key);
	}

	/**
	 * @Description 获得系统参数
	 * @return 返回规定的每页记录数
	 */
	public String getPreferencePageSize() {
		return p.getPagesize() + "";
	}
	// ------------------系统参数相关函数--------------begin
}
