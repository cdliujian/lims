package com.lanshan.web.admin.sm.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Repository;

import com.lanshan.core.base.dao.BaseDaoImpl;
import com.lanshan.web.admin.model.SmRoleMember;
import com.lanshan.web.admin.model.SmUrl;
import com.lanshan.web.admin.sm.dao.SmRoleMemberDao;

@Repository
public class SmRoleMemberDaoImpl extends BaseDaoImpl<SmRoleMember, Integer> implements SmRoleMemberDao {

	static final String QUERY_ROLE = "FROM SmRole r where EXISTS (SELECT 'Y' FROM SmRoleMember rm WHERE rm.positionId=:positionId AND  rm.roleId=r.id)";

	public List loadRoles(Integer positionId) {
		return this.getSessionFactory().getCurrentSession().createQuery(QUERY_ROLE)
				.setParameter("positionId", positionId).list();
	}

	static final String QUERY_USER_SQL = "SELECT DISTINCT D.NAME_,U.CNAME_,U.USERNAME_,U.DEPT_ID FROM SM_USER_POSITION UP,SM_USER U,SM_DEPT D WHERE U.ID=UP.USER_ID AND U.DEPT_ID=D.ID AND UP.POSITION_ID_=:positionId and U.ENABLED_=1";

	@Override
	public List loadUsers(Integer positionId) {
		List<Object[]> queryList = this.getSessionFactory().getCurrentSession().createNativeQuery(QUERY_USER_SQL)
				.setParameter("positionId", positionId).list();
		List retList = new ArrayList();
		for (Object[] rec : queryList) {
			String data = rec[0] + "->" + rec[1] + "(" + rec[2] + ")";
			retList.add(data);
		}
		return retList;
	}

	/**
	 * 查询角色对应的菜单资源信息，以“菜单1->菜单1.1->菜单1.1.1[删除，增加]”结构显示
	 * 
	 * @param roleId
	 * @return
	 */
	@Override
	public List findPositionUrlResourceView(Integer positionId) {
		// 查询菜单
		String sql = "SELECT DISTINCT a.ID,a.NAME_,PARENT_ID_ from SM_URL a,SM_URL_COMPONENT b  "
				+ "where a.ID=b.URL_ID_ and EXISTS (select 'Y' from SM_ROLE_MEMBER rm where rm.ROLE_ID_ =b.ROLE_ID_ AND rm.POSITION_ID_=:positionId) "
				+ "and not EXISTS (select 'Y' from SM_URL  u where u.PARENT_ID_=a.ID)";
		NativeQuery query = this.getSessionFactory().getCurrentSession().createNativeQuery(sql);
		query.setParameter("positionId", positionId);
		List rets = new ArrayList();
		List<Object[]> urls = query.list();
		// 查询资源
		sql = "SELECT d.URL_ID,d.ID,d.DESC_ from sm_component d WHERE d.ID in(SELECT b.COMPONENT_ID_ from sm_role_component b WHERE b.ROLE_ID_ in(SELECT a.ROLE_ID_ from sm_role_member a WHERE a.POSITION_ID_=:positionIdd))";
		query = this.getSessionFactory().getCurrentSession().createNativeQuery(sql);
		query.setParameter("positionIdd", positionId);
		List<Object[]> comps = query.list();
		for (Object[] url : urls) {
			String view = (String) url[1];
			Integer parentId = (Integer) url[2];
			while (parentId != null) {
				String hql = "SELECT * from SM_URL u where u.id=:parentId";
				NativeQuery hquery = this.getSessionFactory().getCurrentSession().createNativeQuery(hql)
						.addEntity(SmUrl.class);
				hquery.setParameter("parentId", parentId);
				List list = hquery.list();
				if (list.size() > 0) {
					SmUrl u = (SmUrl) list.get(0);
					view = u.getName() + " -> " + view;
					parentId = u.getParentId();
				} else
					break;
			}
			String res = null;
			for (Object[] comp : comps) {
				Integer urlId = (Integer) comp[0];
				String resName = (String) comp[2];
				if (urlId == (Integer) url[0]) {
					if (res == null)
						res = resName;
					else
						res += "," + resName;
				}
			}
			if (res != null)
				view += "[" + res + "]";
			rets.add(view);
		}
		java.util.Collections.sort(rets);
		return rets;
	}

	private static final String DELETE_RELATION_BY_ROLEID_SQL = "delete from SmRoleMember WHERE roleId=:roleId and systemId = '";

	@Override
	public void deleteRelationByRoleId(Integer roleId, String systemId) {
		String hql = DELETE_RELATION_BY_ROLEID_SQL + systemId + "'";
		this.getSessionFactory().getCurrentSession().createQuery(hql).setParameter("roleId", roleId).executeUpdate();
	}
}
