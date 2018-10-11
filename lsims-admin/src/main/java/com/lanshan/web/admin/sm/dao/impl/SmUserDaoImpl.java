package com.lanshan.web.admin.sm.dao.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Repository;

import com.lanshan.core.base.dao.BaseDaoImpl;
import com.lanshan.web.admin.model.SmComponent;
import com.lanshan.web.admin.model.SmUrl;
import com.lanshan.web.admin.model.SmUser;
import com.lanshan.web.admin.sm.dao.SmUserDao;

/**
 * 
 * @Description 用户管理Dao实现
 *
 * @author caoying 2018年9月3日 下午2:46:47
 */
@Repository
public class SmUserDaoImpl extends BaseDaoImpl<SmUser, Integer> implements SmUserDao {
	/**
	 * 
	 * @Description 封装查询条件
	 * @param criteria
	 * @param param
	 *            void
	 * @author caoying 2018年9月3日下午2:47:54
	 */
	@Override
	protected void setQuery(DetachedCriteria criteria, Map param) {
		Object o = param.get("many");
		if (o != null && !"".equals(o)) {
			criteria.add(Restrictions.or(Restrictions.like("username", o.toString(), MatchMode.ANYWHERE),
					Restrictions.or(Restrictions.like("cname", o.toString(), MatchMode.ANYWHERE),
							Restrictions.like("mobile", o.toString(), MatchMode.ANYWHERE))));
		}

		o = param.get("username");
		if (o != null && !"".equals(o)) {
			criteria.add(Restrictions.like("username", o.toString(), MatchMode.ANYWHERE));
		}
		o = param.get("cname");
		if (o != null && !"".equals(o)) {
			criteria.add(Restrictions.like("cname", o.toString(), MatchMode.ANYWHERE));
		}
		o = param.get("usernameEq");
		if (o != null && !"".equals(o)) {
			criteria.add(Restrictions.eq("username", o.toString()));
		}
		o = param.get("mobile");
		if (o != null && !"".equals(o)) {
			criteria.add(Restrictions.eq("mobile", o.toString()));
		}
		o = param.get("enabled");
		if (o != null && !"".equals(o)) {
			criteria.add(Restrictions.eq("enabled", o.toString()));
		}
		o = param.get("deptId");
		if (o != null && !"".equals(o)) {
			if (o instanceof Integer) {
				// criteria.add(Restrictions.eq("deptId", o));
				criteria.add(Restrictions.eq("b.id", o));
			} else {
				// criteria.add(Restrictions.eq("deptId",
				// Integer.valueOf(o.toString())));
				criteria.add(Restrictions.eq("b.id", Integer.valueOf(o.toString())));
			}
		}
		o = param.get("idNeq");
		if (o != null) {
			criteria.add(Restrictions.ne("id", o));
		}
		o = param.get("administrator");
		if (o != null && !"".equals(o)) {
			criteria.add(Restrictions.eq("administrator", o.toString()));
		} else {
			// criteria.add(Restrictions.eq("administrator","0"));
		}
		// 微信ID
		o = param.get("weixinOpenId");
		if (o != null && !"".equals(o)) {
			criteria.add(Restrictions.eq("weixinOpenId", o.toString()));
		}
		o = param.get("id");
		if (o != null && o != "") {
			if (o instanceof Integer) {

				criteria.add(Restrictions.eq("id", o));
			} else {
				criteria.add(Restrictions.eq("id", Integer.valueOf(o.toString())));
			}
		}
		// 用户名或者电话
		o = param.get("nameOrPhone");
		if (o != null && o != "") {
			criteria.add(Restrictions.or(Restrictions.eq("username", o.toString()),
					Restrictions.eq("mobile", o.toString())));
		}

		o = param.get("deptName");
		if (o != null && !"".equals(o)) {
			criteria.add(Restrictions.like("b.name", o.toString(), MatchMode.ANYWHERE));
		}
	}

	@Override
	protected void setOrder(DetachedCriteria criteria) {
		super.setOrder(criteria);
		criteria.addOrder(Order.desc("lastUpdateTime"));
	}

	@Override
	protected void setAssociate(DetachedCriteria criteria) {
		super.setAssociate(criteria);
		criteria.createAlias("dept", "b", DetachedCriteria.LEFT_JOIN);

	}

	@Override
	public List findUrlResourceView(Integer userId) {
		// 查询菜单
		String sql = "SELECT DISTINCT a.ID,a.NAME_,PARENT_ID_ from SM_URL a,SM_URL_COMPONENT b,SM_ROLE_MEMBER c,SM_USER_POSITION d "
				+ "where a.ID=b.URL_ID_ and B.ROLE_ID_ = C.ROLE_ID_ and c.POSITION_ID_ =d.POSITION_ID_ and d.USER_ID=:userId "
				+ "and not EXISTS (select 'Y' from SM_URL  u where u.PARENT_ID_=a.ID)";
		NativeQuery query = this.getSessionFactory().getCurrentSession().createNativeQuery(sql);
		query.setParameter("userId", userId);
		List rets = new ArrayList();
		List<Object[]> urls = query.list();
		// 查询资源
		sql = "SELECT d.URL_ID,d.ID,d.DESC_ from sm_component d WHERE d.ID in(SELECT b.COMPONENT_ID_ from sm_role_component b WHERE b.ROLE_ID_ in(SELECT a.ROLE_ID_ from sm_role_member a WHERE a.POSITION_ID_=(SELECT u.POSITION_ID_ from sm_user_position u WHERE u.USER_ID=:userIdd)))";
		query = this.getSessionFactory().getCurrentSession().createNativeQuery(sql);
		query.setParameter("userIdd", userId);
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
				if (urlId.equals(url[0])) {
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

	@Override
	public List<SmUrl> findAllMenu() {
		// TODO Auto-generated method stub
		String sql = "select distinct e.* from SM_URL e,SM_URL_COMPONENT  d,"
				+ "SM_ROLE_MEMBER c where e.ID_=d.URL_ID_ and " + "d.ROLE_ID_=c.ROLE_ID_  order by e.ORDER_";
		SQLQuery query = this.getSession().createSQLQuery(sql);
		query.addEntity(SmUrl.class);
		List<SmUrl> menus = query.list();
		for (int i = 0; i < menus.size(); i++) {
			SmUrl m = menus.get(i);
			SmUrl url = new SmUrl();
			url.setDesc(m.getDesc());
			//url.setForNavigation(m.getForNavigation());
			url.setIcon(m.getIcon());
			url.setId(m.getId());
			url.setName(m.getName());
			url.setOrder(m.getOrder());
			url.setParentId(m.getParentId());
			url.setSystemId(m.getSystemId());
			url.setUrl(m.getUrl());
			menus.set(i, url);
		}
		return menus;
	}
	
	/**
	 * 根据用户查询菜单资源
	 * 
	 * @param userId
	 *            用户ID
	 * @param isNavigation
	 *            是否用于导航
	 * @param topMenuName
	 *            项级菜单名
	 * @param systemCode
	 *            系统标识
	 * @param makeUrl
	 *            是否需要组装全路径URL
	 * @return
	 */
	public List<SmUrl> findMenuByUser(Integer userId, boolean isNavigation, String topMenuName,
			List<String> systemCode, Boolean makeUrl) {
		SmUser user = (SmUser) this.findById(userId);

		String sql = null;
		if (user.getAdministrator() != null && Integer.valueOf(user.getAdministrator()) == 1) {
			sql = "select e.* from  SM_URL e " +
					 " order by e.ORDER_";
		} else {
			sql = "select x.* from (select distinct e.* from BDF2_USER a,BDF2_USER_POSITION  b,"
					+ "BDF2_ROLE_MEMBER c, BDF2_ROLE_RESOURCE d,BDF2_URL e "
					+ "where a.ID=b.USER_ID and b.POSITION_ID_ = c.POSITION_ID_ "
					+ "and c.ROLE_ID_=d.ROLE_ID_ and d.URL_ID_=e.ID_ " + (isNavigation ? "and e.FOR_NAVIGATION_=1" : "")
					+ " and a.ID=:userId " + "and e.URL_ is not null and e.URL_<>'') x order by x.order_";
		}
		SQLQuery query = this.getSession().createSQLQuery(sql);
		query.addEntity(SmUrl.class);
		if (user.getAdministrator() == null || Integer.valueOf(user.getAdministrator()) != 1) {
			query.setInteger("userId", userId);
		}
		List<SmUrl> menus = query.list();

		// 完善父结点
		for (int i = 0; i < menus.size(); i++) {
			SmUrl m = menus.get(i);
			SmUrl url = new SmUrl();
			url.setDesc(m.getDesc());
			//url.setForNavigation(m.getForNavigation());
			url.setIcon(m.getIcon());
			url.setId(m.getId());
			url.setName(m.getName());
			url.setOrder(m.getOrder());
			url.setParentId(m.getParentId());
			url.setSystemId(m.getSystemId());
			url.setCode(m.getCode());

			if (makeUrl) {
				if (StringUtils.isNotBlank(m.getUrl())) {
					
					String _url = m.getUrl();
					if (_url.startsWith("/")) {
						_url = _url.substring(1);
					}
					url.setUrl( _url);
				} else {
					url.setUrl("");
				}
			} else {
				url.setUrl(m.getUrl());
			}

			menus.set(i, url);
			m = url;
			if (m.getParentId()==null)
				continue;
			boolean parented = false;
			for (int k = 0; k < menus.size(); k++) {
				SmUrl mk = menus.get(k);
				if (mk.getId().equals(m.getParentId())) { // mk是m的父节点
					parented = true;
					break;
				}
			}
			if (!parented) {
				SmUrl purl = (SmUrl) this.getSession().load(SmUrl.class, m.getParentId());
				SmUrl purl2 = new SmUrl();
				purl2.setDesc(purl.getDesc());
				//purl2.setForNavigation(purl.getForNavigation());
				purl2.setIcon(purl.getIcon());
				purl2.setId(purl.getId());
				purl2.setName(purl.getName());
				purl2.setOrder(purl.getOrder());
				purl2.setParentId(purl.getParentId());
				purl2.setSystemId(purl.getSystemId());
				purl2.setUrl(purl.getUrl());
				menus.add(purl2);
			}
		}
		List<SmUrl> res = new ArrayList();
		for (SmUrl m : menus) {
			if (topMenuName != null) {
				if (m.getName().equals(topMenuName)) {
					res.add(m);
				}
			} else if (m.getParentId() == null)
				res.add(m);
		}

		// 查询菜单资源
		String sqlRes = null;
		if (user.getAdministrator() != null && Integer.valueOf(user.getAdministrator()) == 1) {
			sqlRes = "select e.* from BDF2_COMPONENT e";
		} else {
			sqlRes = "select distinct e.* " + "  from  BDF2_USER         a," + "        BDF2_USER_POSITION b,"
					+ "        BDF2_ROLE_MEMBER   c," + "        BDF2_URL_COMPONENT d,"
					+ "        BDF2_COMPONENT     e " + " where a.ID = b.USER_ID and b.POSITION_ID_ = c.POSITION_ID_"
					+ "   and c.ROLE_ID_  = d.ROLE_ID_ " + "   and e.ID_ = d.COMPONENT_ID_ " + "   and a.ID = "
					+ userId;
		}
		//query = this.getSession().createSQLQuery(sqlRes);
		//query.addEntity(Bdf2Component.class);
		//List<Bdf2Component> comps = query.list();

		this.setChild(menus, res, isNavigation, null);
		if (topMenuName != null)
			res = res.get(0).getChildren();
		// 再按系统标识过滤
		// if(systemCode!=null)
		// leafJudge(res,systemCode);
		java.util.Collections.sort(res, new Comparator() {
			@Override
			public int compare(Object o1, Object o2) {
				if (((SmUrl) o1).getOrder() == null || ((SmUrl) o2).getOrder() == null) {
					return 1;
				}
				return ((SmUrl) o1).getOrder().compareTo(((SmUrl) o2).getOrder());
			}
		});
		return res;
	}
	
	/**
	 * 迭代处理树形结构
	 * 
	 * @param menus
	 *            查出的URL集合
	 * @param rets
	 *            要返回的结果
	 * @param isNavigation
	 * @param comps
	 *            按鈕集合
	 */
	private void setChild(List<SmUrl> menus, List<SmUrl> rets, boolean isNavigation, List<SmComponent> comps) {
		for (SmUrl r : rets) {
			List child = new ArrayList();
			for (SmUrl m : menus) {
				if (r.getId().equals(m.getParentId())) {
					child.add(m);
				}
			}
			if (child.size() == 0) {// 如果当前节点没有子节点，则为页面节点，添加按钮资源
				if(comps!=null)
				for (SmComponent comp : comps) {
					if (r.getId().equals(comp.getUrlId())) {

						SmComponent c = new SmComponent();

						//c.setCname(comp.getCname());
						c.setComponentId(comp.getComponentId());
						c.setDesc(comp.getDesc());
						c.setId(comp.getId());
						c.setUrlId(comp.getUrlId());
						//r.getBdf2ComponentSet().add(c);
					}
				}
			} else { // 有子节点，不能有按钮，添加子节点
				//r.getBdf2ComponentSet().clear();
				r.setChildren(child);
				this.setChild(menus, child, isNavigation, comps);
			}
		}
	}

}
