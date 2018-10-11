package com.lanshan.web.admin.sm.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Repository;

import com.lanshan.core.base.dao.BaseDaoImpl;
import com.lanshan.web.admin.model.SmDept;
import com.lanshan.web.admin.sm.dao.SmDeptDao;
import com.lanshan.web.admin.sm.utils.DeptUtils;

/**
 * 
 * @Description 机构管理Dao实现
 *
 * @author caoying
 * 2018年9月3日
 * 上午10:37:07
 */
@Repository
public class SmDeptDaoImpl extends BaseDaoImpl<SmDept, Integer> implements SmDeptDao {
	/**
	 * 
	 * @Description 封装查询条件
	 * @param criteria
	 * @param param
	 * void
	 * @author caoying
	 * 2018年9月3日下午2:45:54
	 */
	@Override
	protected void setQuery(DetachedCriteria criteria, Map param) {
		Object o = param.get("name");
		if (o != null && !"".equals(o)) {
			criteria.add(Restrictions.like("name", "%" + o.toString() + "%"));
		}
		o = param.get("parentId");
		if (o != null && !"".equals(o)) {
			criteria.add(Restrictions.eq("parentId", o));
		}
		o = param.get("parentIsNull");
		if (o != null && !"".equals(o)) {
			criteria.add(Restrictions.isNull("parentId"));
		}
		o = param.get("enabled");
		if ("1".equals(o)) {
			criteria.add(Restrictions.eq("enabled", o.toString()));
		} else {
			criteria.add(Restrictions.eq("enabled", "0"));
		}
		o = param.get("systemId");
		if (o != null && !"".equals(o)) {
			criteria.add(Restrictions.eq("systemId", o));
		}
		o = param.get("id");
		if (o != null && !"".equals(o)) {
			criteria.add(Restrictions.eq("id", o));
		}
		o = param.get("nameEq");
		if (o != null && !"".equals(o)) {
			criteria.add(Restrictions.eq("name", o));
		}
		o = param.get("idNeq");
		if (o != null) {
			criteria.add(Restrictions.ne("id", o));
		}
		o = param.get("ids");
		if( o != null && StringUtils.isNotEmpty(o.toString()) )	{	
			String[] ids = o.toString().split(",");
			criteria.add(Restrictions.in("id" , ids));
		}
	}
	
	/**
	 * 
	 * @Description 查找部门全路径,如：5号物流->物流公司->天天通 
	 * @param id
	 * @return
	 * List<SmDept>
	 * @author caoying
	 * 2018年9月19日上午11:17:36
	 */
	public List<SmDept> getDeptFullPath(Integer id) {
		SmDept curDept = DeptUtils.getSmDeptByCurUser(); //根据当前用户获取机构
		List<SmDept> list = new ArrayList<SmDept>();
		SmDept dept = (SmDept) this.findById(id);
		list.add(0, dept);
		if(id.intValue() != curDept.getId().intValue()) {
			//查询机构id与登录人机构id不一致，需要查询上级机构id
			list = this.getParentDeptById(id, list, new HashMap(), curDept.getId());
		}
		return list;
	}
	
	/**
	 * 
	 * @Description 根据id反向递归查询上级部门  
	 * @param id  查询机构id
	 * @param deptList
	 * @param map  根据上级机构id封装机构对象
	 * @param curDeptId  当前用户对应的机构id
	 * @return
	 * List<SmDept>
	 * @author caoying
	 * 2018年9月19日下午5:06:31
	 */
	private List<SmDept> getParentDeptById(Integer id, List<SmDept> deptList, Map map, Integer curDeptId) {
		String sql = "SELECT {parent.*} FROM sm_dept node, sm_dept parent "
				+ "WHERE node.PARENT_ID = parent.ID and node.ID = :id";
		NativeQuery query = this.getSession().createNativeQuery(sql).addEntity("parent", SmDept.class);
		query.setParameter("id", id);

		List<SmDept> list = query.list();
		if(!list.isEmpty() && map.get("parent_" + curDeptId) == null) {
			SmDept parentDept = (SmDept) list.get(0);
			if(map.get(parentDept.getId()) == null) {
				deptList.add(0, parentDept);
				map.put(parentDept.getId(), parentDept);
				map.put("parent_" + parentDept.getId(), deptList);
			}
			return getParentDeptById(parentDept.getId(), deptList, map, curDeptId);
		}
		
		return deptList;
	}
	
	/**
	 * 
	 * @Description 根据传递过来的机构列表查询所有的下级机构 
	 * @param list
	 * @param map
	 * @return
	 * List<SmDept>
	 * @author caoying
	 * 2018年9月20日上午11:33:46
	 */
	public List<SmDept> getChildrenDeptList(List<SmDept> deptList, Map map) {
		List<SmDept> list = new ArrayList<SmDept>();
		for(int i = 0; i < deptList.size(); i++) {
			SmDept dept = (SmDept) deptList.get(i);
			
			List<SmDept> parentDeptList = new ArrayList<SmDept>();
			parentDeptList.add(dept);
			map.put(dept.getId(), parentDeptList);
			
			list.add(dept);
			
			list = this.getChildrenDeptListById(dept, list, map);
		}
		
		return list;
	}
	
	/**
	 * 
	 * @Description 根据机构id递归查询下级机构 
	 * @param id
	 * @param deptList
	 * @param map
	 * @return
	 * List<SmDept>
	 * @author caoying
	 * 2018年9月20日上午11:38:20
	 */
	private List<SmDept> getChildrenDeptListById(SmDept dept, List<SmDept> deptList, Map map) {
		Map param = new HashMap();
		param.put("parentId", dept.getId());
		
		List<SmDept> childrenDeptList = this.queryList(param);
		if(!childrenDeptList.isEmpty()) {
			deptList.addAll(childrenDeptList);
			for(int i = 0; i < childrenDeptList.size(); i++) {
				SmDept childrenDept = (SmDept) childrenDeptList.get(i);
				/**************根据当前机构向上递归设置上级机构   Start*****************/
				List<SmDept> parentDeptList = new ArrayList<SmDept>();
				if(map.get(childrenDept.getParentId()) != null) {
					parentDeptList.addAll((List<SmDept>) map.get(childrenDept.getParentId()));
				}
				parentDeptList.add(parentDeptList.size(), childrenDept);
				map.put(childrenDept.getId(), parentDeptList);
				/**************根据当前机构向上递归设置上级机构   End*****************/
				this.getChildrenDeptListById(childrenDept, deptList, map);
			}
		}

		return deptList;
	}
}
