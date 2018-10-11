package com.lanshan.web.admin.d7auth;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lanshan.web.admin.model.SmUrl;
import com.trans5.core.util.JsonHelper;
import com.trans5.core.util.SpringUtils;
import com.trans5.helper.ConfigHelper;

import com.trans5.sm.service.MenuComponentService;
 
public class UrlMetadataSource {
	private static MenuComponentService getMenuComponentService() {
		return (MenuComponentService) SpringUtils.getBean(MenuComponentService.NAME);
	}

	private static Map metadatas = new ConcurrentHashMap();
    
	static boolean init=false;
	
	synchronized public static void init() {
		System.out.println("===UrlMetadataSource===");
		List l = getMenuComponentService().getAllDisMenu();
		System.out.println("===UrlMetadataSource=== size:"+l.size());
		Map m = initMenuData(l);
		metadatas.putAll(m);
		System.out.println("=====JsonHelper.parser(metadatas)====="+JsonHelper.parser(metadatas));
	}

	public static Map getUrlMetadata() {
		if(!init){
			init();
			init=true;
		} 
		return metadatas;
	}

	public static void clear() {
		metadatas.clear();
	}

	private static Map initMenuData(List menus) {
		Map m = null;
		if (menus != null) {
			m = new HashMap();
			Iterator iter = menus.iterator();
			while (iter.hasNext()) {
				SmUrl menu = (SmUrl) iter.next();
				//DEBF.getApplicationName()
				if (ConfigHelper.getAppName().equals(menu.getSystemId()))
					initMenuData(menu, m);
			}
		}
		return m;
	}

	/**
	 * menu 其实就是一个bdf2url 见 com.de.debf.sm.dao.UserDao findAllMenu() 本方法的含义 就是
	 * 从menu中解析出url作为key，构建一个map
	 * 
	 * @param menu
	 * @param m
	 */
	private static void initMenuData(SmUrl menu, Map m) {
		if (menu != null) {
			String s = menu.getUrl();
			if (s != null && !"".equals(s)) {
				int i = s.indexOf("?");
				if (i != -1) {
					s = s.substring(0, i);
				}
				m.put(s, menu);
			}
			List l = menu.getChildren();
			if (l != null) {
				Iterator iter = l.iterator();
				while (iter.hasNext()) {
					SmUrl zm = (SmUrl) iter.next();

					initMenuData(zm, m);
				}
			}
		}
	}
}
