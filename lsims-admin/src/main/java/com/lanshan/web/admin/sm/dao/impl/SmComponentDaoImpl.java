package com.lanshan.web.admin.sm.dao.impl;

import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.lanshan.core.base.dao.BaseDaoImpl;
import com.lanshan.web.admin.model.SmComponent;
import com.lanshan.web.admin.sm.dao.SmComponentDao;

@Repository
public class SmComponentDaoImpl extends BaseDaoImpl<SmComponent, Integer> implements SmComponentDao {
	
	@Override
	protected void setQuery(DetachedCriteria criteria, Map param) {
		
		Object o = param.get("componentId");
		if (o != null) 
			criteria.add(Restrictions.ilike("componentId", o.toString(), MatchMode.ANYWHERE));
		
		o = param.get("componentIdEQ");
		if (o != null) 
			criteria.add(Restrictions.eq("componentId", o.toString()));
		
		o = param.get("urlId");
		if (o != null) 
			criteria.add(Restrictions.eq("urlId", Integer.parseInt(o.toString())));
		
		o = param.get("systemId");
		if (o != null) 
			criteria.add(Restrictions.eq("systemId", o.toString()));
	}

	@Override
	public void deleteByUrlId(Integer urlId) {
		 String sqlcomhql="delete FROM SmComponents where urlId=:urlId";
         this.getSessionFactory().getCurrentSession()
         		.createQuery(sqlcomhql)
                 .setParameter("urlId", urlId).executeUpdate();
	}
}
