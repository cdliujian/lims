package com.lanshan.web.admin.sm.dao.impl;

import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.lanshan.core.base.dao.BaseDaoImpl;
import com.lanshan.web.admin.model.SmUserPosition;
import com.lanshan.web.admin.sm.dao.SmUserPositionDao;

/**
 * 
 * @Description 用户岗位关联dao实现
 *
 * @author caoying
 * 2018年9月17日 上午11:59:33
 */
@Repository
public class SmUserPositionDaoImpl extends BaseDaoImpl<SmUserPosition, Integer> implements SmUserPositionDao {
	/**
	 * 
	 * @Description 封装查询条件
	 * @param criteria
	 * @param param
	 * void
	 * @author caoying
	 * 2018年9月21日下午16:47:54
	 */
	protected void setQuery(DetachedCriteria criteria, Map param) {
        // TODO Auto-generated method stub
        Object o = param.get("userId");
        if (o != null) {
            criteria.add(Restrictions.eq("userId", o));
        }
        o = param.get("username");
        if (o != null) {
            criteria.add(Restrictions.eq("username", o.toString()));
        }
        o = param.get("positionId");
        if (o != null) {
            criteria.add(Restrictions.eq("positionId", o));
        }
    }
}
