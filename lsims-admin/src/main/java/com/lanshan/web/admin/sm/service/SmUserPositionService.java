package com.lanshan.web.admin.sm.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanshan.core.base.service.BaseServiceImpl;
import com.lanshan.web.admin.model.SmPosition;
import com.lanshan.web.admin.model.SmUserPosition;
import com.lanshan.web.admin.sm.dao.SmUserPositionDao;

/**
 * 
 * @Description 用户岗位关联Service
 *
 * @author caoying
 * 2018年9月21日 下午4:42:55
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SmUserPositionService extends BaseServiceImpl {
	@Autowired
	public void setDao( SmUserPositionDao dao){
		super.setDao(dao);
	}
	
	/**
	 * 
	 * @Description 保存用户岗位关联信息 
	 * @param positions 岗位列表
	 * @param userId 用户id
	 * @param userName 用户名
	 * @param cname 用户中文名
	 * void
	 * @author caoying
	 * 2018年9月21日下午4:44:22
	 */
	public void saveUserPosition(List<SmPosition> positions, Integer userId, String userName, String cname) {
		if (positions == null) {
			return;
		}
		
		//清理原有岗位
		Map param = new HashMap();
		param.put("userId", userId);
		List<SmUserPosition> userPositionList = this.dao.queryList(param);
		this.dao.deleteAll(userPositionList);
		
		// 重新维护用户与岗位的对应关系
		Iterator itr = positions.iterator();
		while (itr.hasNext()) {
			SmPosition position = (SmPosition) itr.next();
			SmUserPosition up = new SmUserPosition();
			up.setPositionId(position.getId());
			up.setUserId(userId);
			up.setUsername(userName);
			up.setCreateDate(new Date());
			up.setLastUpdateTime(new Date());
			up.setLastUpdatePerson(cname);
			
			this.dao.save(up);
		}
	}
}
