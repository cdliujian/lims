package com.lanshan.web.admin.sm.dao;

import java.util.List;

import com.lanshan.core.base.dao.BaseDao;
import com.lanshan.web.admin.model.SmPosition;

/**
 * 
 * @Description 岗位管理dao
 *
 * @author caoying
 * 2018年9月17日 上午11:52:34
 */
public interface SmPositionDao extends BaseDao<SmPosition, Integer> {
	/**
	 * 
	 * @Description 根据用户id查询岗位信息 
	 * @param userId
	 * @return
	 * List<SmPosition>
	 * @author caoying
	 * 2018年9月21日下午5:15:10
	 */
	public List<SmPosition> getSmPositionListByUserId(Integer userId);
}
