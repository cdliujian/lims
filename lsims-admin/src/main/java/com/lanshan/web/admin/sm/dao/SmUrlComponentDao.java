package com.lanshan.web.admin.sm.dao;

import com.lanshan.core.base.dao.BaseDao;
import com.lanshan.web.admin.model.SmUrlComponent;

public interface SmUrlComponentDao extends BaseDao<SmUrlComponent, Integer> {

	/**
	 * 根据urlId删除菜单和角色的对应关系
	 * @param urlId
	 */
	void deleteByUrlId(Integer urlId, String systemId);
	
	/**
	 * 根据roleId删除角色与菜单的对应关系
	 * @param roleId
	 */
	void deleteByRoleId(Integer roleId, String systemId);
}
