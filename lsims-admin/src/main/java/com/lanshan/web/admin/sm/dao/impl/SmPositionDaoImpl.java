package com.lanshan.web.admin.sm.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Repository;

import com.lanshan.core.base.dao.BaseDaoImpl;
import com.lanshan.web.admin.model.SmPosition;
import com.lanshan.web.admin.sm.dao.SmPositionDao;

/**
 * 
 * @Description 岗位管理DaoImpl
 *
 * @author caoying 2018年9月17日 上午11:53:01
 */
@Repository
public class SmPositionDaoImpl extends BaseDaoImpl<SmPosition, Integer> implements SmPositionDao {
	/**
	 * 
	 * @Description 封装查询条件
	 * @param criteria
	 * @param param
	 *            void
	 * @author caoying 2018年9月17日 上午11:53:01
	 */
	@Override
	protected void setQuery(DetachedCriteria criteria, Map param) {
		Object o = param.get("id");
		if (o != null) {
			criteria.add(Restrictions.eq("id", o));
		}

		o = param.get("code");
		if (o != null) {
			criteria.add(Restrictions.eq("code", o));
		}
		o = param.get("name");
		if (o != null) {
			criteria.add(Restrictions.like("name", "%" + o.toString().trim() + "%"));
		}

		o = param.get("nameEq");
		if (o != null) {
			criteria.add(Restrictions.eq("name", o.toString()));
		}
		o = param.get("systemId");
		if (o != null) {
			criteria.add(Restrictions.eq("systemId", o.toString()));
		}

	}
	
	/**
	 * 
	 * @Description 根据用户id查询岗位信息 
	 * @param userId
	 * @return
	 * List<SmPosition>
	 * @author caoying
	 * 2018年9月21日下午5:15:10
	 */
	public List<SmPosition> getSmPositionListByUserId(Integer userId) {
		List<SmPosition> list = new ArrayList<SmPosition>();
		String sql = "SELECT {p.*} FROM sm_user_position up, sm_position p WHERE up.POSITION_ID_ = p.ID AND up.USER_ID = :userId";
		NativeQuery query = this.getSession().createNativeQuery(sql).addEntity("p", SmPosition.class);
		query.setParameter("userId", userId);
		list = query.list();
		
		return list;
	}
}
