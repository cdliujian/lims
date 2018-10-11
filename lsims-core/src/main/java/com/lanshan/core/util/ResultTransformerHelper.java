package com.lanshan.core.util;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.transform.ResultTransformer;

/**
* @ClassName: ResultTransformerHelper
* @Description: TODO(这里用一句话描述这个类的作用)
* @author hlg
* @date 2012-7-3 下午5:52:56
* 
*/
public class ResultTransformerHelper {

	public static Map <Class,ResultTransformer>_rMap = new HashMap();
	public static ResultTransformer getResultTransformer(Class c)
	{
		ResultTransformer r = _rMap.get(c);
		if( r == null )
		{
			r = new AutoResultTransformer(c);
			_rMap.put(c, r);
		}
		return r;
	}
}
