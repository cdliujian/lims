package com.lanshan.web.admin.sm.dao;

import java.util.List;

import com.lanshan.core.base.dao.BaseDao;
import com.lanshan.web.admin.model.SmRoleMember;

public interface SmRoleMemberDao extends BaseDao<SmRoleMember, Integer> {

	public List loadRoles(Integer positionId);

	/**
	 * 
	 * 根据岗位ID查询用户信息
	 * 
	 * @param positionId
	 * @return List
	 * @author 朱郑韬 2018年9月18日下午5:16:46
	 */
	public List loadUsers(Integer positionId);

	/**
	 * 
	 * 根据岗位ID查询菜单资源信息
	 * 
	 * @param positionId
	 * @return List
	 * @author 朱郑韬 2018年9月18日下午5:17:11
	 */
	public List findPositionUrlResourceView(Integer positionId);
	
	/**
	 * 根据角色id，删除角色与岗位的关联关系
	 * @param roleId
	 */
	void deleteRelationByRoleId(Integer roleId, String systemId);

}
