package com.lanshan.web.admin.sm.dao;

import com.lanshan.core.base.dao.BaseDao;
import com.lanshan.web.admin.model.SmRoleComponent;

public interface SmRoleComponentDao extends BaseDao<SmRoleComponent, Integer> {
	
	/**
	 * 根据roleId删除角色与菜单资源的关联
	 * @param roleId
	 */
	void deleteByRoleId(Integer roleId, String systemId);

}
