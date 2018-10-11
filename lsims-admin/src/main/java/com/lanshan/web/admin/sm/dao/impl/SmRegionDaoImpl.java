package com.lanshan.web.admin.sm.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.lanshan.core.base.dao.BaseDaoImpl;
import com.lanshan.web.admin.model.SmRegion;
import com.lanshan.web.admin.sm.dao.SmRegionDao;

@Repository
public class SmRegionDaoImpl  extends BaseDaoImpl<SmRegion, Integer> implements SmRegionDao {
		
	@Override
	protected void setQuery(DetachedCriteria criteria, Map param) {
		Object o = param.get("parentId");
        if (o != null) {
            criteria.add(Restrictions.eq("parentId", Integer.valueOf(o.toString())));
        }
        o = param.get("regionType");
        if (o != null) {
            criteria.add(Restrictions.eq("regionType", Integer.valueOf(o.toString())));
        }
        o = param.get("parentIdIsNull");
        if (o != null) {
            criteria.add(Restrictions.isNull("parentId"));
        }
        o = param.get("id");
        if (o != null) {
            criteria.add(Restrictions.eq("id", o));
        }
        
        o = param.get("regionName");
        if (o != null && StringUtils.isNotEmpty(o.toString())) {
        	criteria.add(Restrictions.eq("regionName", o));
        }
        
		o = param.get("ids");
		if (o != null) {
			String[] s = o.toString().split(",");
			if (s.length == 1) {
				criteria.add(Restrictions.eq("id", new Integer(o.toString()).intValue()));
			}
			if (s.length > 1) {
				List<Integer> list = new ArrayList<Integer>();
				for (int i = 0; i < s.length; i++) {
					list.add(new Integer(s[i]));
				}
				criteria.add(Restrictions.in("id", list));
			}
		}
        
        o = param.get("regionNames");
		if (o != null) {
			String[] s = o.toString().split(",");
			if (s.length == 1) {
				criteria.add(Restrictions.eq("regionName", o));
			}
			if (s.length > 1) {
				List<String> list = new ArrayList<String>();
				for (int i = 0; i < s.length; i++) {
					list.add(s[i]);
				}
				criteria.add(Restrictions.in("regionName", list));
			}
		}
	}
}
