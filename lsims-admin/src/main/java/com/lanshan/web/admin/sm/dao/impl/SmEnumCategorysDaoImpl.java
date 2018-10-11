package com.lanshan.web.admin.sm.dao.impl;

import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.lanshan.core.base.dao.BaseDaoImpl;
import com.lanshan.web.admin.model.SmEnumCategorys;
import com.lanshan.web.admin.sm.dao.SmEnumCategorysDao;

@Repository
public class SmEnumCategorysDaoImpl extends BaseDaoImpl<SmEnumCategorys, Integer> implements SmEnumCategorysDao{
	
	@Override
	protected void setQuery(DetachedCriteria criteria, Map param) {
        Object o = param.get("id");
        //增加编码 like 的查询条件
        o = param.get("nameEq");
        if (o != null) {
            criteria.add(Restrictions.eq("name", o.toString().trim()));
        }
        //增加编码 like 的查询条件
        o = param.get("name");
        if (o != null) {
            criteria.add(Restrictions.like("name", o.toString().trim(), MatchMode.ANYWHERE));
        }
		o = param.get("systemId");
		if (o != null && !"".equals(o)) {
			criteria.add(Restrictions.eq("systemId", o.toString()));
		}
	}
}
