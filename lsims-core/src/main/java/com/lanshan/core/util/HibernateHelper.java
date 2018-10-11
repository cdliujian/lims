package com.lanshan.core.util;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.metadata.CollectionMetadata;
import org.hibernate.type.Type;


/**
* @ClassName: HibernateHelper
* @Description: TODO(这里用一句话描述这个类的作用)
* @author hlg
* @date 2012-7-3 下午5:52:54
* 
*/
public class HibernateHelper {

	public static SessionFactory _sessionFactory;
	private static Map <Class,String[]>_basicPropertyNamesMap = new HashMap();
	public static SessionFactory getSessionFactory()
	{
		if( _sessionFactory ==  null )
		{
			_sessionFactory = (SessionFactory)SpringUtils.getBean("sessionFactory");
		}
		return _sessionFactory;
	}
	public static String[] getBasicPropertyNames(Class c)
	{
		String pn[] = _basicPropertyNamesMap.get(c);
		String pns[] = getPropertyNames(c);
		Type ts[] = getPropertyTypes(c);
		pn = new String[pns.length+1];
		pn[0] = getClassMetadata(c).getIdentifierPropertyName();
		int j = 1;
		for( int i=0;i<ts.length;i++ )
		{
			Type t = ts[i];	
			if( !(t.isEntityType() || t.isCollectionType()) )
			{
				pn[j++] = pns[i];
			}
		}		
		String[] npn = new String[j];
		System.arraycopy(pn, 0, npn, 0, j);
		_basicPropertyNamesMap.put(c,npn);
		return npn;
	}
	public static String[] getPropertyNames(Class c)
	{
		return getClassMetadata(c).getPropertyNames();
	}
	public static Type[] getPropertyTypes(Class c)
	{
		return getClassMetadata(c).getPropertyTypes();
	}
	public static ClassMetadata getClassMetadata(Class c)
	{
		return getSessionFactory().getClassMetadata(c);
	}
	public static CollectionMetadata getCollectionMetadata(Class c,String p)
	{
		return getSessionFactory().getCollectionMetadata( c.getName() +"." + p );
	}
	public static Class getReturnedClass(Class c,String pn)
	{
		return getClassMetadata(c).getPropertyType(pn).getReturnedClass();
	}
	public static Class getCollectionReturnedClass(Class c,String p)
	{
		return getCollectionMetadata(c, p).getElementType().getReturnedClass();
	}
	public static String getIdentifierPropertyName(Class c)
	{
		return getClassMetadata(c).getIdentifierPropertyName();
	}
}
