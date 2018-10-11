package com.lanshan.core.util;

import java.util.List;

import org.hibernate.transform.ResultTransformer;

/**
* @ClassName: AutoResultTransformer
* @Description: TODO(这里用一句话描述这个类的作用)
* @author hlg
* @date 2012-7-3 下午5:52:45
* 
*/
public class AutoResultTransformer implements ResultTransformer{

	private Class _objClass;
	public AutoResultTransformer(Class objClass)
	{
		_objClass = objClass;
	}
	public List transformList(List list) {
		// TODO Auto-generated method stub
		return list;
	}

	public Object transformTuple(Object[] aobj, String[] as) {
		// TODO Auto-generated method stub
		
		try {
			return DetachedCriteriaUtils.toObjectNested(_objClass, aobj, as);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
