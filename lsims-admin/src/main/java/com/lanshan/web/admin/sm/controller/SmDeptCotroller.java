package com.lanshan.web.admin.sm.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Criterion;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.data.provider.filter.SingleValueFilterCriterion;
import com.lanshan.core.base.controller.BaseController;
import com.lanshan.core.util.SessionUtil;
import com.lanshan.web.admin.model.SmDept;
import com.lanshan.web.admin.sm.service.SmDeptService;
import com.lanshan.web.admin.sm.utils.DeptUtils;

/**
 * 
 * @Description 机构管理Cotroller
 *
 * @author caoying
 * 2018年9月3日
 * 下午2:29:37
 */
@Controller
public class SmDeptCotroller extends BaseController {
	private Map parentMap = new HashMap();
	
	@Autowired
	public void setService(SmDeptService service){
		super.setService(service);
	}

	/**
	 * 
	* @Description 查询机构信息 
	* @param page
	* @param param
	* void
	* @author caoying
	* 2018年9月3日下午2:34:50
	 */
	@DataProvider
	public void loadQueryResult(Page page, Map param) {
		// TODO Auto-generated method stub
		String systemId = DeptUtils.getSystemIdByCurUser(); // 根据登录人获取系统标识
		SmDept dept = DeptUtils.getSmDeptByCurUser(); //根据当前用户获取所属机构
		
		HashMap map = (HashMap) param;
		//如果登录人所属机构为基础系统，则查询所有机构，否则查询登录人所属机构对应系统标识的机构信息
		if(StringUtils.isNotEmpty(systemId) && !"lsims".equals(systemId)) {
			map.put("systemId", systemId);
			map.put("parentIsNull", "");
			map.put("id", dept.getId());
		}
		if((map.containsKey("name") && (map.get("name") == null || "".equals(map.get("name")))) || map.size() == 0) {
			if(StringUtils.isNotEmpty(systemId) && "lsims".equals(systemId)) {
				map.put("parentIsNull", "true");
			}
		}
		super.loadQueryResult(page, param);
	}

	/**
	 * 
	* @Description 查询机构信息 
	* @param param
	* void
	* @author caoying
	* 2018年9月6日下午3:34:50
	 */
	@DataProvider
	public List loadQueryResult(Map param) {
		String systemId = DeptUtils.getSystemIdByCurUser(); // 根据登录人获取系统标识
		SmDept dept = DeptUtils.getSmDeptByCurUser(); //根据当前用户获取所属机构
		
		HashMap map = (HashMap) param;
		//如果登录人所属机构为基础系统，则查询所有机构，否则查询登录人所属机构对应系统标识的机构信息
		if(StringUtils.isNotEmpty(systemId) && !"lsims".equals(systemId)) {
			map.put("systemId", systemId);
			map.put("parentIsNull", "");
			map.put("id", dept.getId());
		}
		if((map.containsKey("name") && (map.get("name") == null || "".equals(map.get("name")))) || map.size() == 0) {
			if(StringUtils.isNotEmpty(systemId) && "lsims".equals(systemId)) {
				map.put("parentIsNull", "true");
			}
		}
		List l = super.loadQueryResult(map);
		return l;
	}
	
	/**
	 * 
	 * @Description 用于过滤的分页查询 
	 * @param page
	 * @param param
	 * @param criteria
	 * void
	 * @author caoying
	 * 2018年9月19日上午9:09:29
	 */
	@DataProvider
	public void queryResult(Page page, Map param, Criteria criteria) {
		// TODO Auto-generated method stub
		param = param == null ? new HashMap() : param;
		String systemId = DeptUtils.getSystemIdByCurUser(); // 根据登录人获取系统标识
		SmDept dept = DeptUtils.getSmDeptByCurUser(); //根据当前用户获取所属机构
		if (criteria != null) {
			for (Criterion c : criteria.getCriterions()) {
				if (c instanceof SingleValueFilterCriterion) {
					SingleValueFilterCriterion fc = (SingleValueFilterCriterion) c;
					String property = fc.getProperty();// 获取查询的属性名
					if ("name".equals(property)) {
						param.put("name", fc.getValue());
					}

				}
			}
		}
		
		//如果登录人所属机构为基础系统，则查询所有机构，否则查询登录人所属机构对应系统标识的机构信息
		if(StringUtils.isNotEmpty(systemId) && !"lsims".equals(systemId)) {
			param.put("systemId", systemId);
			param.put("parentIsNull", "");
			param.put("id", dept.getId());
		}
		if((param.containsKey("name") && (param.get("name") == null || "".equals(param.get("name")))) || param.size() == 0) {
			if(StringUtils.isNotEmpty(systemId) && "lsims".equals(systemId)) {
				param.put("parentIsNull", "true");
			}
		}
		
		super.loadQueryResult(page, param);
		
		parentMap = new HashMap();
		List list = new ArrayList();
		list.addAll(page.getEntities());
		
		page.setEntities(((SmDeptService) service).getChildrenDeptList(list, parentMap));
	}

	/**
	 * 
	 * @Description 根据ID查询机构信息 
	 * @param id
	 * @return
	 * SmDept
	 * @author caoying
	 * 2018年9月18日下午4:25:49
	 */
	@DataProvider
	public SmDept queryDeptInfoById(Integer id) {
		return (SmDept) service.findById(id);
	}
	
	/**
	 * 
	 * @Description 根据查询条件来查询下级部门信息，所以出入参数中一定需要带一个parentId的查询条件 
	 * @param param
	 * @return
	 * List<SmDept>
	 * @author caoying
	 * 2018年9月5日上午11:47:14
	 */
	@DataProvider
	public List<SmDept> queryDeptChildren(Map param) {
		List<SmDept> list = service.queryList(param);
		return list;
	}

	/**
	 * 
	 * @Description 根据id查询上级机构 
	 * @param id
	 * @return
	 * List<SmDept>
	 * @author caoying
	 * 2018年9月19日上午11:08:04
	 */
	@DataProvider
	public List<SmDept> queryDeptParentsById(Integer id) {
//		return ((SmDeptService) service).queryDeptParentsById(id);
		List<SmDept> list = (List<SmDept>) parentMap.get(id);
		return list;
	}
	
	/**
	 * 
	* @Description 保存机构信息 
	* @param dept
	* @throws Exception
	* void
	* @author caoying
	* 2018年9月3日下午2:10:50
	 */
	@DataResolver
	public void save(SmDept dept) throws Exception {
		// 判断用户名是否已经存在
		HashMap amap = new HashMap();
		amap.put("nameEq", dept.getName());

		// 如果是更新操作，则判断用户名已经存在要去掉本身
		if (null != dept.getId()) {
			amap.put("idNeq", dept.getId());
		}
		List list = service.queryList(amap);
		if (list.size() > 0) {
			throw new Exception("机构名称已经存在！");
		}
		((SmDeptService)service).saveDept(dept);
	}
	
	/**
	 * 
	* @Description 删除，将机构禁用 
	* @param dept
	* @throws Exception
	* void
	* @author caoying
	* 2018年9月3日下午2:26:54
	 */
	@DataResolver
	public void delete(SmDept dept) throws Exception {
		String userName = SessionUtil.getSecurityUser().getCname(); //获取登录用户名

		// 删除之前判断最后更新日期是否一致，防止并发操作
		SmDept oldDept = (SmDept) service.findById(dept.getId());
		SimpleDateFormat formatter;
		formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String lastUpdateDateTime = formatter.format(dept.getLastUpdateTime());
		String oldlastUpdateDateTime = formatter.format(oldDept.getLastUpdateTime());
		if (oldlastUpdateDateTime.equals(lastUpdateDateTime)) {
			BeanUtils.copyProperties(dept, oldDept);
			oldDept.setLastUpdateTime(new Date());
			oldDept.setLastUpdatePerson(userName);
			oldDept.setEnabled("1");
			service.update(oldDept);
		} else {
			throw new Exception("保存失败,此数据可能已经被修改,请刷新后重新操作!");
		}
	}
}
