package com.lanshan.web.admin.sm.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Criterion;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.data.provider.filter.SingleValueFilterCriterion;
import com.lanshan.core.base.controller.BaseController;
import com.lanshan.core.util.SessionUtil;
import com.lanshan.web.admin.model.SmPosition;
import com.lanshan.web.admin.sm.service.SmPositionService;
import com.lanshan.web.admin.sm.utils.DeptUtils;

/**
 * 
 * @Description: 岗位
 * @author 朱郑韬 2018年9月18日 下午5:12:24
 */
@Expose
@Controller
public class SmPositionCotrller extends BaseController {

	@Autowired
	public void setService(SmPositionService service) {
		super.setService(service);
	}

	@DataProvider
	public void loadQueryResult(Page page, Map params) {
		params.put("systemId", DeptUtils.getSystemIdByCurUser());
		super.loadQueryResult(page, params);
	}

	@DataProvider
	public List loadQueryResult(Map param) {
		param.put("systemId", DeptUtils.getSystemIdByCurUser());
		return super.loadQueryResult(param);
	}

	@DataProvider
	public List loadRoles(Integer positionId) {
		return ((SmPositionService) service).loadRoles(positionId);
	}

	@DataProvider
	public void queryResult(Page page, Map param, Criteria criteria) throws Exception {
		param = param == null ? new HashMap() : param;
		if (criteria != null) {
			for (Criterion c : criteria.getCriterions()) {
				if (c instanceof SingleValueFilterCriterion) {
					SingleValueFilterCriterion fc = (SingleValueFilterCriterion) c;
					param.put(fc.getProperty(), fc.getValue());
				}
			}
		}
		String systemId = DeptUtils.getSystemIdByCurUser();
		if (null == systemId) {
			throw new Exception("无法获得系统标识，请重新登录");
		} else {

			param.put("systemId", systemId);
			this.loadQueryResult(page, param);
		}
	}

	/**
	 * 
	 * 通过岗位加载用户
	 * 
	 * @param positionId
	 * @return List
	 * @author 朱郑韬 2018年9月20日下午4:41:16
	 */
	@Expose
	public List loadUsers(Integer positionId) {
		return ((SmPositionService) service).loadUsers(positionId);
	}

	/**
	 * 
	 * 通过岗位加载角色
	 * 
	 * @param positionId
	 * @return List
	 * @author 朱郑韬 2018年9月20日下午4:41:38
	 */
	@Expose
	public List loadResources(Integer positionId) {
		return ((SmPositionService) service).loadResources(positionId);
	}

	/**
	 * 
	 * 保存
	 * 
	 * @param obj
	 * @throws Exception
	 *             void
	 * @author 朱郑韬 2018年9月20日下午4:42:00
	 */
	@DataResolver
	public void save(SmPosition obj) throws Exception {
		Map map = new HashMap<>();
		map.put("nameEq", obj.getName());
		SmPosition old = (SmPosition) service.query(map);
		obj.setLastUpdatePerson(SessionUtil.getSecurityUser().getCname());
		obj.setLastUpdateTime(new Date());
		obj.setSystemId(DeptUtils.getSystemIdByCurUser());
		if (null == obj.getId()) {
			if (null == old) {
				obj.setCreateDate(new Date());
				service.save(obj);
			} else {
				throw new Exception("岗位【" + obj.getName() + "】重复");
			}
		} else {
			if (null != old || (null != old && old.getId() == obj.getId())) {

				service.update(obj);
			} else {
				throw new Exception("岗位【" + obj.getName() + "】重复");
			}
		}
	}

	/**
	 * 
	 * 通过ID删除
	 * 
	 * @param id
	 *            void
	 * @author 朱郑韬 2018年9月20日下午4:42:12
	 */
	@Expose
	public void deleteById(Integer id) {
		service.delete(service.findById(id));
	}

	/**
	 * 
	 * @Description 根据用户id查询岗位信息
	 * @param userId
	 * @return List<SmPosition>
	 * @author caoying 2018年9月21日下午5:15:10
	 */
	@DataProvider
	public List<SmPosition> getSmPositionListByUserId(Integer userId) {
		return ((SmPositionService) service).getSmPositionListByUserId(userId);
	}

}
