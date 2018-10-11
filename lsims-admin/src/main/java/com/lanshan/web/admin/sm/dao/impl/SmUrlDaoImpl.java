package com.lanshan.web.admin.sm.dao.impl;

import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.lanshan.core.base.dao.BaseDaoImpl;
import com.lanshan.web.admin.model.SmUrl;
import com.lanshan.web.admin.sm.dao.SmUrlDao;

@Repository
public class SmUrlDaoImpl extends BaseDaoImpl<SmUrl, Integer> implements SmUrlDao {
	
	@Override
	protected void setQuery(DetachedCriteria criteria, Map param) {
		Object o = param.get("id");
		if( o != null ){			
			criteria.add(Restrictions.eq("id",o));
		}
		
		o = param.get("idNeq"); 
        if( o != null )
            criteria.add(Restrictions.ne("id",Integer.valueOf(o.toString())));
        
		o = param.get("parentId");
		if( o != null )		
			criteria.add(Restrictions.eq("parentId", o));
		
		o = param.get("parentIsNull");
		if( o != null && Integer.valueOf(o.toString()) == 1)		
			criteria.add(Restrictions.isNull("parentId"));
		
		o = param.get("nameEq");
		if( o != null )		
			criteria.add(Restrictions.eq("name", o));
		
		o = param.get("code");
		if( o != null )		
			criteria.add(Restrictions.eq("name", o));
		
		o = param.get("memo");
		if( o != null)
			criteria.add(Restrictions.like("memo", o));
		
		o = param.get("systemId");
		if (o != null && !"".equals(o))
			criteria.add(Restrictions.eq("systemId", o.toString()));
	}
	
	
}
