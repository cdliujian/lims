package com.lanshan.web.admin.sm.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.lanshan.core.base.dao.BaseDaoImpl;
import com.lanshan.web.admin.model.SmRole;
import com.lanshan.web.admin.model.SmUrl;
import com.lanshan.web.admin.sm.dao.SmRoleDao;
import com.lanshan.web.admin.sm.utils.SmConstant;

@Repository
public class SmRoleDaoImpl extends BaseDaoImpl<SmRole, Integer> implements SmRoleDao {
	
	@Override
	protected void setQuery(DetachedCriteria criteria, Map param) {
		
		Object o = param.get("systemId");
		if (o != null && !"".equals(o))
			criteria.add(Restrictions.eq("systemId", o.toString()));
	}
	
	public SmRole getSmRole() {
		String hql = "from SmRole where name=:name";
		SmRole role = (SmRole) this
				.getSessionFactory().getCurrentSession().createQuery(hql)
				.setParameter("name", SmConstant.DEFAULT_ROLE_NAME)
				.uniqueResult();
		return role;
	}
	
	public List findUrlResourceView(Integer roleId){
		//查询角色关联的菜单
		String sql = "SELECT a.ID, a.NAME_, PARENT_ID_ from SM_URL a, SM_URL_COMPONENT b "
				+ "where a.ID = b.URL_ID_ and b.ROLE_ID_= ?";
		SQLQuery query = this.getSession().createSQLQuery(sql);
		query.setInteger(0, roleId);
		List rets = new ArrayList();
		List<Object[]> urls = query.list();
		//查询角色关联菜单对应的资源
		sql = "select a.URL_ID_, b.DESC_ from sm_role_component a, "
				+ "SM_COMPONENT b where a.ROLE_ID_ = ? and a.COMPONENT_ID_ = b.ID;";
		query = this.getSession().createSQLQuery(sql);
		query.setInteger(0, roleId);
		List<Object[]> comps = query.list();
		for(Object[] url:urls){
			String view=(String)url[1];
			Integer parentId = (Integer)url[2];
			while(parentId!=null){
				String hql = "from SmUrl u where u.id=?";
				Query hquery = this.getSession().createQuery(hql);
				hquery.setInteger(0, parentId);
				List list = hquery.list();
				if(list.size()>0){
					SmUrl u = (SmUrl)list.get(0);
					view = u.getName()+" -> "+view;
					parentId=u.getParentId();
				}else
					break;
			}
			String res=null;
			for(Object[] comp:comps){
				Integer urlId = (Integer)comp[0];
				String resName = (String)comp[1];
				if(urlId == url[0]){
					if(res==null)
						res=resName;
					else
					    res+=","+resName;
				}
			}
			if(res!=null)
				view+="["+res+"]";
			rets.add(view);
		}
		java.util.Collections.sort(rets);
		return rets;
	}
}
