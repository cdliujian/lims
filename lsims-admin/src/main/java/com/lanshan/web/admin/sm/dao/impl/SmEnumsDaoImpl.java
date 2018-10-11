package com.lanshan.web.admin.sm.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.lanshan.core.base.dao.BaseDaoImpl;
import com.lanshan.web.admin.model.SmEnums;
import com.lanshan.web.admin.sm.dao.SmEnumsDao;
import com.lanshan.web.admin.sm.utils.DeptUtils;

@Repository
public class SmEnumsDaoImpl extends BaseDaoImpl<SmEnums, Integer> implements SmEnumsDao{
	
	@Override
	protected void setQuery(DetachedCriteria criteria, Map param) {
		Object o = param.get("id");
		if( o != null ){			
			criteria.add(Restrictions.eq("id",o));
		}
		o = param.get("categoryId");
		if( o != null ){			
			criteria.add(Restrictions.eq("categoryId", o));
		}		
		o = param.get("nameEq");
		if( o != null ){			
			criteria.add(Restrictions.eq("name", o));
		}
		o = param.get("memo");
		if( o != null){
			criteria.add(Restrictions.like("memo", o));
		}
		o = param.get("systemId");
		if (o != null && !"".equals(o)) {
			criteria.add(Restrictions.eq("systemId", o.toString()));
		}
	}
	
	/**
     * @根据字典项来获得所有的字典项
     * @param categoryId：字典编码ID
     * @return  SmEnums:字典项集合
     */
    public List<SmEnums> getEnumsByCategoryId(Integer categoryId, String systemId){
        HashMap amap = new HashMap();
        amap.put("categoryId", categoryId);
        amap.put("systemId", systemId);
        return super.queryList(amap);
    }
}
