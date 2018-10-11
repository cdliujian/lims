package com.lanshan.web.admin.sm.dao;


import java.util.List;

import com.lanshan.core.base.dao.BaseDao;
import com.lanshan.web.admin.model.SmRole;

public interface SmRoleDao extends BaseDao<SmRole, Integer> {
	
	/**
	 * 查询默认角色
	 * @return
	 */
	SmRole getSmRole();
	
	/**
	 * 查询角色对应的菜单资源信息，以“菜单1->菜单1.1->菜单1.1.1[删除，增加]”结构显示
	 * @param roleId
	 * @return
	 */ 
	List findUrlResourceView(Integer roleId);
	
}
