package com.lanshan.web.admin.sm.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanshan.core.base.service.BaseServiceImpl;
import com.lanshan.core.util.SessionUtil;
import com.lanshan.web.admin.model.SmDept;
import com.lanshan.web.admin.model.SmRegion;
import com.lanshan.web.admin.sm.dao.SmDeptDao;
import com.lanshan.web.admin.sm.utils.SmConstant;

/**
 * 
 * @Description 机构管理Service实现
 *
 * @author caoying
 * 2018年9月3日
 * 上午10:45:17
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SmDeptService extends BaseServiceImpl {
	private SmRegionService regionServ;
	
	@Autowired
	public void setRegionServ(SmRegionService regionServ) {
		this.regionServ = regionServ;
	}

	@Autowired
	public void setDao( SmDeptDao dao){
		super.setDao(dao);
	}

	/**
	 * 
	 * @Description 保存机构信息 
	 * @param dept
	 * @throws Exception
	 * void
	 * @author caoying
	 * 2018年9月4日下午3:38:18
	 */
	public void saveDept(SmDept dept) throws Exception {
		String userName = SessionUtil.getSecurityUser().getCname(); //获取登录用户名

		if(dept.getAreaId() != null) {
			//区域ID不为空
			SmRegion region = (SmRegion)regionServ.findById(dept.getAreaId());
			if(SmConstant.REGION_TYPE_AREA.equals(region.getRegionType())) {
				//区域类型为区时，向上查询市与省信息
				SmRegion city = (SmRegion)regionServ.findById(region.getParentId()); //根据上级ID查询市
				dept.setCityId(city.getId());
				SmRegion province = (SmRegion)regionServ.findById(city.getParentId()); //根据上级ID查询省
				dept.setProvinceId(province.getId());
			} else if(SmConstant.REGION_TYPE_CITY.equals(region.getRegionType())) {
				//区域类型为市时，向上查询省信息
				SmRegion province = (SmRegion)regionServ.findById(region.getParentId()); //根据上级ID查询省
				dept.setProvinceId(province.getId());
			} else if(SmConstant.REGION_TYPE_PROVINCE.equals(region.getRegionType())) {
				//区域类型为省，省ID与区域ID相同
				dept.setProvinceId(region.getId());
			}
		}
		
		if (dept.getId() == null) {
			dept.setCreateTime(new Date());
			dept.setLastUpdateTime(new Date());
			dept.setLastUpdatePerson(userName);
			dao.save(dept);
		} else {
			// 更新之前判断最后更新日期是否一致，防止并发操作
			SmDept oldDept = (SmDept) dao.findById(dept.getId());
			SimpleDateFormat formatter;
			formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String lastUpdateDateTime = formatter.format(dept.getLastUpdateTime());
			String oldlastUpdateDateTime = formatter.format(oldDept.getLastUpdateTime());
			if (oldlastUpdateDateTime.equals(lastUpdateDateTime)) {
				BeanUtils.copyProperties(dept, oldDept);
				oldDept.setLastUpdateTime(new Date());
				oldDept.setLastUpdatePerson(userName);
				dao.update(oldDept);
			} else {
				throw new Exception("保存失败,此数据可能已经被修改,请刷新后重新操作！");
			}
		}
	}

	/**
	 * 
	 * @Description 根据id查询上级机构 
	 * @param id
	 * @return
	 * List<SmDept>
	 * @author caoying
	 * 2018年9月19日上午11:09:03
	 */
	public List<SmDept> queryDeptParentsById(Integer id) {
		return ((SmDeptDao) dao).getDeptFullPath(id);
	}
	
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
	public List<SmDept> getChildrenDeptList(List<SmDept> deptList, Map map) {
		return ((SmDeptDao) dao).getChildrenDeptList(deptList, map);
	}
}
