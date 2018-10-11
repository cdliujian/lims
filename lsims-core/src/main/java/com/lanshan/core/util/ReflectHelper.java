package com.lanshan.core.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

 

public class ReflectHelper {
	
	public static Object executeStaticMethod(String clas,String method,Class[] paramType,Object[] param)
	{
		Class c = null;
		try {
			c = Class.forName(clas);
			return executeStaticMethod(c, method, paramType, param);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return null;
	}
	public static Object executeStaticMethod(Class c,String method,Class[] paramType,Object[] param)
	{
		try
		{
			Method m = c.getMethod(method, paramType);
			return m.invoke(null, param);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return null;
	}
	
	/**
	 * 调用spring
	 * @throws Exception 
	 */
	public static Object invokeService(Object  beanOrBeanName,String methodName,Object ...params) throws Exception{
		Object service=null; 
		if(beanOrBeanName instanceof String){
			service=(Object) SpringUtils.getBean((String) beanOrBeanName); 
			if(service==null){
				throw new Exception("没有找到bean:"+beanOrBeanName);
			}
		}else{
			service = beanOrBeanName;
		}   
	    Method m =getMethod(service.getClass(),methodName,params); 
	    
		Object result = m.invoke(service, params);
		return result; 
	} 

	/**
	 * 获取被调用的方法
	 * @throws Exception 
	 */
	public static Method getMethod(Class clazz,String method,Object ... params) throws Exception{
	   Method m=null; 
       try {   
			@SuppressWarnings("rawtypes")
			Class[] paramsClass = new Class[params.length];
			for(int i=0;i<params.length;i++){
				paramsClass[i]=params[i].getClass();
			}  
			
			if(params==null || params.length==0){  //无参数
				m=clazz.getMethod(method); 
			}else{
				m=clazz.getMethod(method, paramsClass); 
			} 
			 
		} catch (Exception e) {
			// TODO Auto-generated catch block 
			Method[] allMethods=clazz.getMethods();
			for(Method mRec:allMethods){
//				System.out.println("method name:"+mRec.getName());
				if(mRec.getName().equals(method)){
					Class<?>[] methodParamsTypes = mRec.getParameterTypes();  //找到同名函数的参数类型
					if(methodParamsTypes.length==params.length){
						boolean flag=true;
						if(params.length>0){
							for(int i=0;i<methodParamsTypes.length;i++){
								if(null!=params[i]){
									if(!match(params[i],methodParamsTypes[i])){
										flag=false;
										break;
									}
								}
							}
						} 
						if(flag){
							m=mRec;
							break;
						}
					}
				}
			} 
		}
       
       if(null==m){
    	   throw new Exception(String.format("未找[%s]到的[%s]方法完成任务.",clazz.toString(),method));
       }
		return m;
	}
	
	/**
	 * 获取对象的属性值
	 * @param entity
	 * @param propertyName
	 * @return
	 */
	public static Object getProperty(Object entity,String propertyName){
		try {    
	           String firstLetter = propertyName.substring(0, 1).toUpperCase();    
	           String getter = "get" + firstLetter + propertyName.substring(1);    
	           Method method = entity.getClass().getMethod(getter, new Class[] {});    
	           Object value = method.invoke(entity, new Object[] {});    
	           return value;    
	       } catch (Exception e) {     
	           return null;    
	       }    
	}
	
	
	public static Map getPropertiesInfo(Object o){  
		 Field[] fields=o.getClass().getDeclaredFields();  
	     String[] fieldNames=new String[fields.length];  
		 Map infos=new HashMap();
		 Map infoMap=null;  
		 for(int i=0;i<fields.length;i++){  
		    infoMap = new HashMap();  
		    infoMap.put("type", fields[i].getType().toString());   
		    infoMap.put("value", getProperty( o,fields[i].getName()));  
		    infos.put(fields[i].getName(), infoMap);
		  }  
		 return infos;  
	 } 
	  
	
	/**
	 * 判断参数类型是否匹配
	 * @param c  参数类型
	 * @param pc  方法参数类型
	 * @return
	 */
	private static boolean match(Object p,Class pc){
		boolean isMatch=true;
		if(pc.isPrimitive()){    //方法参数是基本类型 
			if(!pc.equals(getPrimitiveClass(p.getClass()))){
				isMatch=false;
			}
		}else{
			if(!pc.isInstance(p)){
				isMatch=false;
			}
		}
		return isMatch;
	}
	
	private static Class getPrimitiveClass(Class c)
	{
		Class pc = null;
		if( c.equals(Long.class) )
		{
			pc = long.class;
		}
		else if( c.equals(Integer.class) )
		{
			pc = int.class;
		}
		else if( c.equals(Double.class) )
		{
			pc = double.class;
		}
		else if( c.equals(Float.class) )
		{
			pc = float.class;
		}
		else if( c.equals(Short.class) )
		{
			pc = short.class;
		}
		else if( c.equals(Byte.class) )
		{
			pc = byte.class;
		}
		else if( c.equals(Character.class) )
		{
			pc = char.class;
		}
		else if( c.equals(Boolean.class) )
		{
			pc = boolean.class;
		}
		return pc;
	} 
}
