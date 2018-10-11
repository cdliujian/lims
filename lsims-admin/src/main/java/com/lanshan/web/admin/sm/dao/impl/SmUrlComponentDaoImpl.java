package com.lanshan.web.admin.sm.dao.impl;

import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.lanshan.core.base.dao.BaseDaoImpl;
import com.lanshan.web.admin.model.SmUrlComponent;
import com.lanshan.web.admin.sm.dao.SmUrlComponentDao;

@Repository
public class SmUrlComponentDaoImpl extends BaseDaoImpl<SmUrlComponent, Integer> implements SmUrlComponentDao {
	
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

	@Override
	public void deleteByUrlId(Integer urlId, String systemId) {
		String sqlcomhql="delete FROM SmUrlComponent where urlId=:urlId and systemId = '" + systemId + "'";
        this.getSessionFactory().getCurrentSession()
        		.createQuery(sqlcomhql)
                .setParameter("urlId", urlId).executeUpdate();
	}

	@Override
	public void deleteByRoleId(Integer roleId, String systemId) {
		String deleteRole2Resource="delete from SmUrlComponent where roleId=:roleId and systemId = '" + systemId + "'";
		this.getSessionFactory().getCurrentSession()
			.createQuery(deleteRole2Resource)
			.setParameter("roleId", roleId).executeUpdate();
	}
}
