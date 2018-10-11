package com.lanshan.web.admin.sm.dao;

import java.util.List;

import com.lanshan.core.base.dao.BaseDao;
import com.lanshan.web.admin.model.SmUrl;
import com.lanshan.web.admin.model.SmUser;

/**
 * 
 * @Description 用户管理Dao
 *
 * @author caoying 2018年9月3日 下午2:46:21
 */
public interface SmUserDao extends BaseDao<SmUser, Integer> {

	public List findUrlResourceView(Integer userId);
	
	public List<SmUrl> findAllMenu();
	
	public List<SmUrl> findMenuByUser(Integer userId, boolean isNavigation, String topMenuName,
			List<String> systemCode, Boolean makeUrl);
}
