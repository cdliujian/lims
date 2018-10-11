package com.lanshan.web.admin.sm.dao.impl;

import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.lanshan.core.base.dao.BaseDaoImpl;
import com.lanshan.web.admin.model.SmRoleComponent;
import com.lanshan.web.admin.sm.dao.SmRoleComponentDao;

@Repository
public class SmRoleComponentDaoImpl extends BaseDaoImpl<SmRoleComponent, Integer> implements SmRoleComponentDao {

	@Override
	protected void setQuery(DetachedCriteria criteria, Map param) {
		Object o = param.get("key");
		if (o != null && !"".equals(o)) {
			criteria.add(Restrictions.like("key", o.toString()));
		}
		
		o = param.get("keyEq");
		if( o != null  && !"".equals(o))
		{			
			criteria.add(Restrictions.eq("key", o));
		}
	}
	
	
	@Override
	public void deleteByRoleId(Integer roleId, String systemId) {
		String deleteRole2Components="delete from SmRoleComponent where roleId=:roleId and systemId = '" + systemId + "'";
		this.getSessionFactory().getCurrentSession()
			.createQuery(deleteRole2Components)
			.setParameter("roleId", roleId).executeUpdate();
		
	}

}
