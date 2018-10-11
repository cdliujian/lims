package com.lanshan.web.admin.sm.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanshan.core.base.service.BaseServiceImpl;
import com.lanshan.core.util.SessionUtil;
import com.lanshan.web.admin.model.SmPosition;
import com.lanshan.web.admin.model.SmRole;
import com.lanshan.web.admin.model.SmRoleMember;
import com.lanshan.web.admin.sm.dao.SmPositionDao;
import com.lanshan.web.admin.sm.dao.SmRoleMemberDao;
import com.lanshan.web.admin.sm.dao.SmUrlComponentDao;
import com.lanshan.web.admin.sm.dao.SmUserPositionDao;
import com.lanshan.web.admin.sm.utils.DeptUtils;

/**
 * 
 * @Description 岗位管理Service实现
 *
 * @author caoying 2018年9月17日 上午11:55:33
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SmPositionService extends BaseServiceImpl {
	@Autowired
	private SmUserPositionDao smUserPositionDao;
	@Autowired
	private SmRoleMemberDao roleMemberDao;
	@Autowired
	private SmUrlComponentDao smUrlComponentDao;

	@Autowired
	public void setDao(SmPositionDao dao) {
		super.setDao(dao);
	}

	/**
	 * 
	 * 查询岗位对应的角色集合
	 * 
	 * @param positionId
	 * @return List
	 * @author 朱郑韬 2018年9月20日上午10:49:44
	 */
	public List loadRoles(Integer positionId) {
		return roleMemberDao.loadRoles(positionId);
	}

	/**
	 * 
	 * 查询岗位对应的用户集合
	 * 
	 * @param positionId
	 * @return List
	 * @author 朱郑韬 2018年9月20日上午10:50:16
	 */
	public List loadUsers(Integer positionId) {
		return roleMemberDao.loadUsers(positionId);
	}

	/**
	 * 
	 * 查询岗位对应的菜单资源
	 * 
	 * @param positionId
	 * @return List
	 * @author 朱郑韬 2018年9月20日上午10:50:37
	 */
	public List loadResources(Integer positionId) {
		return roleMemberDao.findPositionUrlResourceView(positionId);
	}

	@Override
	public void save(Object obj) {
		SmPosition position = (SmPosition) obj;
		super.save(obj);

		// 保存岗位和角色对应关系
		List<SmRole> list = position.getRoles();
		if (null != list && list.size() > 0) {
			for (SmRole bean : list) {
				SmRoleMember smRoleMember = new SmRoleMember();
				smRoleMember.setRoleId(bean.getId());
				smRoleMember.setPositionId(position.getId());
				smRoleMember.setCreateDate(new Date());
				smRoleMember.setLastUpdatePerson(SessionUtil.getSecurityUser().getUsername());
				smRoleMember.setLastUpdateTime(new Date());
				smRoleMember.setSystemId(DeptUtils.getSystemIdByCurUser());
				roleMemberDao.save(smRoleMember);
			}
		}
	}

	@Override
	public void update(Object obj) {
		SmPosition position = (SmPosition) obj;
		super.update(obj);
		// 删除岗位和角色的对应关系
		HashMap amap = new HashMap();
		amap.put("positionId", position.getId());
		roleMemberDao.deleteAll(roleMemberDao.queryList(amap));

		// 保存岗位和角色对应关系
		List<SmRole> list = position.getRoles();
		if (null != list && list.size() > 0) {
			for (SmRole bean : list) {
				SmRoleMember smRoleMember = new SmRoleMember();
				smRoleMember.setRoleId(bean.getId());
				smRoleMember.setPositionId(position.getId());
				smRoleMember.setCreateDate(new Date());
				smRoleMember.setLastUpdatePerson(SessionUtil.getSecurityUser().getUsername());
				smRoleMember.setLastUpdateTime(new Date());
				smRoleMember.setSystemId(DeptUtils.getSystemIdByCurUser());
				roleMemberDao.save(smRoleMember);
			}
		}
	}

	@Override
	public void delete(Object obj) {
		SmPosition position = (SmPosition) obj;

		// 删除岗位和角色的对应关系
		HashMap amap = new HashMap();
		amap.put("positionId", position.getId());
		SmRoleMember rm = (SmRoleMember) roleMemberDao.query(amap);
		roleMemberDao.deleteAll(roleMemberDao.queryList(amap));

		// 删除用户和岗位的对应关系
		amap = new HashMap();
		amap.put("positionId", position.getId());
		smUserPositionDao.deleteAll(smUserPositionDao.queryList(amap));

		// 删除菜单资源
		amap = new HashMap();
		amap.put("roleId", rm.getRoleId());
		smUrlComponentDao.deleteAll(smUrlComponentDao.queryList(amap));

		// 删除本身岗位信息
		super.delete(obj);
	}

	/**
	 * 
	 * @Description 根据用户id查询岗位信息
	 * @param userId
	 * @return List<SmPosition>
	 * @author caoying 2018年9月21日下午5:15:10
	 */
	public List<SmPosition> getSmPositionListByUserId(Integer userId) {
		return ((SmPositionDao) dao).getSmPositionListByUserId(userId);
	}
}
