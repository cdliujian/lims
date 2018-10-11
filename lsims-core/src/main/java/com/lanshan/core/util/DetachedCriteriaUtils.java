package com.lanshan.core.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;

/**
* @ClassName: DetachedCriteriaUtils
* @Description: TODO(这里用一句话描述这个类的作用)
* @author hlg
* @date 2012-7-3 下午5:52:51
* 
*/
public class DetachedCriteriaUtils {
	
	private static String POINT = ".";
	private static String EPKEY = "__";
	
	public static List Projections(Class o)
	{
		return Projections(o,null);
	}
	
	public static List Projections(Class c,String oalias)
	{
		return Projections(c,oalias,null);
	}
	public static List Projections(Class c,String oalias,String pname)
	{
		oalias = oalias != null ? oalias + POINT : "";
		pname = pname != null ? pname + POINT : "";
		List list = new ArrayList();
		String fs[] = HibernateHelper.getBasicPropertyNames(c);
		for(String f : fs)
		{
			list.add( Projections.property(oalias +f).as(pname + f) );
		}		
		return list;
	}
	
	public static List setProjections(Class o,String oalias,ProjectionList projectionList)
	{
		return setProjections(o,oalias,null,projectionList);
	} 
	public static List setProjections(Class o,String oalias,String pname,ProjectionList projectionList)
	{
		List<Projection> list = Projections(o,oalias,pname);
		for( Projection p : list )
		{
			projectionList.add(p);
		}
		return list;
	} 
	public static Object toObjectNested(Class tClass,Object[] aobj, String[] as) throws Exception
	{
		return toObjectNested(tClass, toObjectMap(aobj,as));
	}
	
	private static Map toObjectMap(Object[] aobj, String[] as)
	{
		Map m = new HashMap();
		for(int i=0;i<as.length;i++)
		{
			String a = as[i];
			Object v = aobj[i];
			try {
				put(m,a,v);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return m;
	}
	
/*	private static void put(Map map,String path,Object obj)
	{
		String paths[] = path.split("\\"+POINT);		
		Map um = map;
		for( int i=0;i<paths.length-1;i++ )
		{
			Map m = (Map)um.get(paths[i]);
			if( m == null )
			{
				m = new HashMap();
				um.put(paths[i], m);
			}
			um = m;			
		}
		um.put(paths[paths.length-1], obj);
	}*/
	
	private static void put(Object collObj,String path,Object obj) throws Exception
	{
		String paths[] = path.split("\\"+POINT);		
		Object uo = collObj;
		for( int i=0;i<paths.length-1;i++ )
		{
			Object o = PropertyUtils.getProperty(uo,paths[i]);
			if( o == null )
			{
				o = new HashMap();				
				PropertyUtils.setProperty(uo, paths[i], o);				
			}
			uo = o;			
		}
		PropertyUtils.setProperty(uo, paths[paths.length-1], obj);
	}
	
	private static Object toObjectNested(Class c,Map m) throws Exception
	{
		Object obj = c.newInstance();
		Iterator iter = m.keySet().iterator();
		while( iter.hasNext() )
		{
			String key = (String)iter.next();
			Object pv = m.get(key);
			boolean isCollection = false;
			if( pv instanceof Map )
			{
				Class pc = getProClass(c, key);
				if( isCollection(pc) )
				{
					isCollection = true;
					pc = HibernateHelper.getCollectionReturnedClass(c, key);
				}
				pv = toObjectNested(pc, (Map)pv);
			}
			if( isCollection )
			{
				((Collection)PropertyUtils.getProperty(obj, key)).add(pv);
			}
			else
			{
				PropertyUtils.setProperty(obj, key, pv);			
			}
		}		
		return obj;
	}
	private static Class getProClass(Class c,String p) throws Exception
	{
		try{
			c = HibernateHelper.getReturnedClass(c, p);

		}catch(Exception e)
		{
			//e.printStackTrace();
			c = c.getDeclaredField(p).getType();
		}		
		return c;
	}
	private static boolean isCollection(Class c)
	{
		return Collection.class.isAssignableFrom(c);
	}
	
}
