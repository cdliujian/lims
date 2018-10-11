package com.lanshan.web.admin.d7auth;
 
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lanshan.web.admin.model.SmComponent;
import com.lanshan.web.admin.model.SmUrl;

 

public class ComponentMetadataSource {
	
	private static Map metadatas = new ConcurrentHashMap();

	static boolean init=false;
	public static Map getMetadata(String url) {
		// TODO Auto-generated method stub
		if(!init){
			init=true;
			init();
		}
		return (Map)metadatas.get(url);
	}

	public static void init()
	{
		Map m = init( UrlMetadataSource.getUrlMetadata() );
		System.out.println("=========ComponentMetadataSource==============="+m);
		metadatas.putAll(m);
	}
	public static Map init(Map urlMetadata){
		if(urlMetadata == null ){
			return null;
		}
		Iterator iter = (Iterator)urlMetadata.keySet().iterator();
		Map componentMap = new HashMap();
		while( iter.hasNext() )
		{
			String key = (String)iter.next();
			SmUrl menu = (SmUrl)urlMetadata.get(key);
			List comps = menu.getComponents();
			Iterator compsIter = comps.iterator();
			Map mcMap = new HashMap();
			while( compsIter.hasNext() )
			{
				SmComponent mc = (SmComponent)compsIter.next();
				mcMap.put(mc.getComponentId(), "1");
			}
			componentMap.put(key,mcMap);
		}
		return componentMap;
	}
	
	public static void clear()
	{
		metadatas.clear();
	}
}
