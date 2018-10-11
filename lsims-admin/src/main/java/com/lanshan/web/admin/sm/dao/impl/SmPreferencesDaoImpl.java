package com.lanshan.web.admin.sm.dao.impl;

import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.lanshan.core.base.dao.BaseDaoImpl;
import com.lanshan.web.admin.model.SmPreferences;
import com.lanshan.web.admin.sm.dao.SmPreferencesDao;

@Repository
public class SmPreferencesDaoImpl extends BaseDaoImpl<SmPreferences, Integer> implements SmPreferencesDao{
	
	@Override
	protected void setQuery(DetachedCriteria criteria, Map param) {
		Object o = param.get("key");
		if (o != null && !"".equals(o)) {
			criteria.add(Restrictions.like("key", o.toString(), MatchMode.ANYWHERE));
		}
		
		o = param.get("keyEq");
		if( o != null  && !"".equals(o))
		{			
			criteria.add(Restrictions.eq("key", o));
		}
		
		o = param.get("systemId");
		if (o != null && !"".equals(o)) {
			criteria.add(Restrictions.eq("systemId", o.toString()));
		}
	}
}
