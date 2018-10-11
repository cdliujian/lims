package com.lanshan.web.admin.sm.dao;

import java.util.List;
import java.util.Map;

import com.lanshan.core.base.dao.BaseDao;
import com.lanshan.web.admin.model.SmDept;

/**
 * 
 * @Description 机构管理Dao
 *
 * @author caoying
 * 2018年9月3日
 * 上午10:34:59
 */
public interface SmDeptDao extends BaseDao<SmDept, Integer> {
	/**
	 * 
	 * @Description 查找部门全路径,如：5号物流->物流公司->天天通 
	 * @param id
	 * @return
	 * List<SmDept>
	 * @author caoying
	 * 2018年9月19日上午11:17:36
	 */
	public List<SmDept> getDeptFullPath(Integer id);
	
	/**
	 * 
	 * @Description 根据传递过来的机构列表查询所有的下级机构 
	 * @param list
	 * @param map
	 * @return
	 * List<SmDept>
	 * @author caoying
	 * 2018年9月20日上午11:33:46
	 */
	public List<SmDept> getChildrenDeptList(List<SmDept> deptList, Map map);
}
