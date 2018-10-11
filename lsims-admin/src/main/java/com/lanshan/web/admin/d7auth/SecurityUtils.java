package com.lanshan.web.admin.d7auth;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.trans5.core.util.SpringUtils;
import com.trans5.helper.HttpHelper;
import com.trans5.sm.pojo.Bdf2Url;
import com.trans5.sm.service.MenuComponentService;

public class SecurityUtils {
	
	private static final String USER_COMPONENTS_METADATA = "USER_COMPONENTS_METADATA";
	private static final String USER_RES = "_SECURITY_USER_RES";
	
	private static MenuComponentService menuComponentService;
	
	synchronized static MenuComponentService getMenuComponentService(){
		if(null==menuComponentService){
			menuComponentService=(MenuComponentService) SpringUtils.getBean(MenuComponentService.NAME);
		}
		return menuComponentService;
	}
	synchronized private static Map getUserComponentsMetadata(String url) {
		Map comps  =getUserComponentMetadata(); 
		return (Map)comps.get(url);
	}
	
	private static Map getUserComponentMetadata()
	{
		Map comps = (Map)HttpHelper.getSessionAttr(USER_COMPONENTS_METADATA);
		if( comps == null ){
			comps = ComponentMetadataSource.init(getUserUrlMenuData());
			HttpHelper.setSessionAttr(USER_COMPONENTS_METADATA, comps);
		}		
		return comps;
	}
	
	public static Map getUserUrlMenuData()
	{
		Map res = (Map)HttpHelper.getSessionAttr(USER_RES);
		if( res == null )
		{
			res = initUserMenuData();
			HttpHelper.setSessionAttr(USER_RES, res);
		}		
		return res;
	}
	

	synchronized private static Map initUserMenuData(){
		try{
			List l = getMenuComponentService().getUserRes();
			Map m = initMenuData(l);
			return m;
		}catch(Exception m)
		{
			return null;
		}
		
	}
	private static Map initMenuData(List menus)
	{
		Map m = null;
		if( menus != null )
		{
			m = new HashMap();
			Iterator iter = menus.iterator();
			while( iter.hasNext() )
			{
				Bdf2Url menu = (Bdf2Url)iter.next();
				
				initMenuData(menu, m);
			}
		}	
		return m;
	}
	
	private static void initMenuData(Bdf2Url menu,Map m)
	{
		if( menu != null )
		{
			String s = menu.getUrl();
			if( s != null && !"".equals(s) )
			{
				int i = s.indexOf("?");
				if( i != -1 )
				{
					s = s.substring(0,i);
				}
				m.put(s,menu);
			}
			List l = menu.getChildren();
			if( l != null )
			{
				Iterator iter = l.iterator();
				while(iter.hasNext())
				{
					Bdf2Url children = (Bdf2Url)iter.next();					
					
					initMenuData(children, m);
				}
			}
		}
	}
	

	public static boolean checkComponent(AuthorityType type, String url, String componentid) {
		// TODO Auto-generated method stub
		Map m = ComponentMetadataSource.getMetadata(url);
		
		//if( AuthorityType.read.equals(type) )
		//	return true;
		Object write  = null;
		boolean flag = false;
		if( m != null )
		{
			write = m.get(componentid);			
		}
		if( write == null )
			return true;	
		//if( AuthorityType.write.equals(type) )
		//{
			Map um = getUserComponentsMetadata(url);
			if( um != null )
			{
				Object uwrite = um.get(componentid);
				if( uwrite != null )
					return true;
			}
			
		//}
		
		
		return flag;
	}

}
