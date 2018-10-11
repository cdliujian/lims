package com.lanshan.web.admin.sm.dao.impl;

import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.lanshan.core.base.dao.BaseDaoImpl;
import com.lanshan.web.admin.model.SmSystem;
import com.lanshan.web.admin.sm.dao.SmSystemDao;

@Repository
public class SmSystemDaoImpl extends BaseDaoImpl<SmSystem, String> implements SmSystemDao {
	@Override
	protected void setQuery(DetachedCriteria criteria, Map param) {
		Object o = param.get("systemId");
		if (o != null && !"".equals(o)) {
			criteria.add(Restrictions.eq("systemId", o.toString()));
		}
		
		o = param.get("desc");
		if (o != null && !"".equals(o)) {
			criteria.add(Restrictions.eq("desc", o.toString()));
		}
	}

}
