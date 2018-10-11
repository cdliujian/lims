package com.lanshan.web.admin.sm.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.lanshan.core.base.service.BaseServiceImpl;
import com.lanshan.core.util.SessionUtil;
import com.lanshan.web.admin.model.SmRole;
import com.lanshan.web.admin.model.SmRoleComponent;
import com.lanshan.web.admin.model.SmRoleMember;
import com.lanshan.web.admin.model.SmUrlComponent;
import com.lanshan.web.admin.redis.RedisService;
import com.lanshan.web.admin.sm.dao.SmRoleComponentDao;
import com.lanshan.web.admin.sm.dao.SmRoleDao;
import com.lanshan.web.admin.sm.dao.SmRoleMemberDao;
import com.lanshan.web.admin.sm.dao.SmUrlComponentDao;
import com.lanshan.web.admin.sm.utils.DeptUtils;
import com.lanshan.web.admin.sm.utils.SmConstant;

@Service
@Transactional(rollbackFor = Exception.class)
public class SmRoleService extends BaseServiceImpl{
	
	@Resource
	private SmRoleMemberDao smRoleMemberDao;
	
	@Resource
	private SmUrlComponentDao smUrlComponentDao;
	
	@Resource
	private SmRoleComponentDao smRoleComponentDao;
	
	@Resource
	private RedisService redisService;
	
	@Autowired
	public void setDao(SmRoleDao dao) {
		super.setDao(dao);
	}

	public void saveRole(SmRole role) {
		if(SmConstant.DEFAULT_ROLE_NAME.equals(role.getName())){
			throw new RuntimeException("角色名称不允许为【"+role.getName()+"】");
		}
		String systemId = DeptUtils.getSystemIdByCurUser();
		Date now = new Date();
		role.setSystemId(systemId);
		role.setLastUpdatePerson(SessionUtil.getSecurityUser().getUsername());
		role.setLastUpdateTime(now);
		EntityState state = EntityUtils.getState(role);
		if (state.equals(EntityState.NEW)){
			role.setCreateDate(now);
			this.save(role);
		}
		if (state.equals(EntityState.MODIFIED)||state.equals(EntityState.MOVED))
			this.update(role);
		doPositionLink(role.getRolePosiIds(),role.getId(), systemId, now);
		doResourceLink(role.getRoleResIds(), role.getRoleResNames(), role.getId(), systemId, now);
	}

	public void deleteRole(Integer id) {
		//删除角色与岗位的关联
		String systemId = DeptUtils.getSystemIdByCurUser();
		smRoleMemberDao.deleteRelationByRoleId(id, systemId);
		//删除角色与菜单的关联
		smUrlComponentDao.deleteByRoleId(id, systemId);
		//删除角色与菜单资源的关联
		smRoleComponentDao.deleteByRoleId(id, systemId);
		//删除本身
		SmRole role = (SmRole) this.findById(id);
		this.delete(role);
		//删除redis中的角色数据 TODO
		redisService.del(SmConstant.REDIS_ROLE + systemId, id + "");
	}

	/**
	 * 查询角色对应
	 * @param roleId
	 * @return
	 */
	public String getResourceIds(Integer roleId) {
		return (String) redisService.get(SmConstant.REDIS_ROLE + DeptUtils.getSystemIdByCurUser(), roleId + "");
	}

	/**
	 * 查询角色所有的菜单和按钮名称
	 * @param roleId
	 * @return
	 */
	public List queryResourceName(Integer roleId) {
		return ((SmRoleDao) dao).findUrlResourceView(roleId);
	}

	/**
	 * 维护角色和岗位之间的关系
	 */
	private void doPositionLink(String posiIds, Integer roleId, String systemId, Date now){
		if(StringUtils.isBlank(posiIds))
			return;
		
		if(roleId==null)
			throw new RuntimeException("数据异常，无法完成操作");
		
		smRoleMemberDao.deleteRelationByRoleId(roleId, systemId);
		
		String[] posiIdArr= posiIds.split(";");
		
		String username = SessionUtil.getSecurityUser().getUsername();
		
		for(String posi:posiIdArr){
			if(StringUtils.isNotBlank(posi)){
				SmRoleMember r2m=new SmRoleMember();
				r2m.setCreateDate(now);
				r2m.setPositionId(Integer.valueOf(posi));
				r2m.setRoleId(roleId);
				r2m.setLastUpdatePerson(username);
				r2m.setLastUpdateTime(now);
				r2m.setSystemId(systemId);
				super.dao.save(r2m);
			}
		}
	}
	/**
	 * 维护角色和资源（菜单、URL）之间的关系
	 */
	private void doResourceLink(String resIds,String resNames,int roleId, String systemId, Date now){ 
		//从redis中获取角色的菜单资源
		String orgnIds = getResourceIds(roleId); 
		if(!StringUtils.isBlank(orgnIds)){
			if(orgnIds.equals(resIds)){    //权限串没变，不需要维护重新资源权限
				return;
			}
		}
		
		//先删除所有的关系，重新加入
		smUrlComponentDao.deleteByRoleId(roleId, systemId);
		smRoleComponentDao.deleteByRoleId(roleId, systemId);
		
		//如果当前操作是角色删除与菜单资源的关联
		if(!StringUtils.isBlank(resIds)){
			String[] arr = resIds.split(";");
			String username = SessionUtil.getSecurityUser().getUsername();
			for(String s:arr){
				SmUrlComponent r2r=new SmUrlComponent();
				r2r.setRoleId(roleId);
				r2r.setCreateDate(now);
				r2r.setLastUpdateTime(now);
				r2r.setLastUpdatePerson(username);
				r2r.setSystemId(systemId);
				//有指定菜单的某些资源
				if(s.indexOf("(")>-1 && s.indexOf(")")>-1){   //按钮资源
					String[] ids = s.split("\\(");
					r2r.setUrlId(Integer.valueOf(ids[0]));
					String[] componentIds = ids[1].substring(0, ids[1].indexOf(")")).split(",");
					for(String str: componentIds){
						SmRoleComponent src = new SmRoleComponent();
						src.setCreateDate(now);
						src.setComponentId(Integer.valueOf(str));
						src.setRoleId(roleId);
						src.setSystemId(systemId);
						src.setUrlId(Integer.valueOf(ids[0]));
						smRoleComponentDao.save(src);
					}
				}else
					r2r.setUrlId(Integer.valueOf(s));
				smUrlComponentDao.save(r2r);
			}
		}
		//存储到redis
		redisService.set(SmConstant.REDIS_ROLE + systemId, roleId + "", resIds);
	}
}
