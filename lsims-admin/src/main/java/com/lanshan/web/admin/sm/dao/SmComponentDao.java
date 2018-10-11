package com.lanshan.web.admin.sm.dao;

import com.lanshan.core.base.dao.BaseDao;
import com.lanshan.web.admin.model.SmComponent;

public interface SmComponentDao extends BaseDao<SmComponent, Integer> {

	/**
	 * 删除菜单对应的资源 
	 * @param urlId 菜单id
	 */
	void deleteByUrlId(Integer urlId);
}
