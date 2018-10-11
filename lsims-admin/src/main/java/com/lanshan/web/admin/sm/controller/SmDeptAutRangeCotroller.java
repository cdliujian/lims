package com.lanshan.web.admin.sm.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;
import com.lanshan.core.base.controller.BaseController;
import com.lanshan.core.util.SessionUtil;
import com.lanshan.web.admin.model.SmDept;
import com.lanshan.web.admin.model.SmDeptAutRange;
import com.lanshan.web.admin.sm.service.SmDeptAutRangeService;
import com.lanshan.web.admin.sm.service.SmDeptService;

/**
 * 
 * @Description 机构权限管理Controller
 *
 * @author caoying
 * 2018年9月18日 上午9:19:53
 */
@Controller
public class SmDeptAutRangeCotroller extends BaseController {
	@Autowired
	private SmDeptService deptService;
	@Autowired
	public void setService(SmDeptAutRangeService service){
		super.setService(service);
	}

	/**
	 * @Description 查询机构权限信息 
	 * @param page
	 * @param param
	 * void
	 * @author caoying
     * 2018年9月18日 上午9:19:53
	 */
	@DataProvider
	public void loadQueryResult(Page page, Map param) {
		// TODO Auto-generated method stub
		super.loadQueryResult(page, param);
	}
	
	/**
	 * @Description 查询机构权限信息 
	 * @param param
	 * void
	 * @author caoying
     * 2018年9月20日 下午4:19:53
	 */
	@DataProvider
	public List loadQueryResult(Map param) {
		List<SmDeptAutRange> l = super.loadQueryResult(param);
		for(SmDeptAutRange range : l) {
			//查找部门全路径,如：5号物流->物流公司->天天通 
			String authRangeName = "";
			List<SmDept> deptList = deptService.queryDeptParentsById(range.getRangeDeptId());
			for(SmDept dept : deptList) {
				authRangeName = authRangeName + dept.getName() + ",";
			}
			range.setAuthRangeName(authRangeName.substring(0, authRangeName.length() - 1));
		}
		
		return l;
	}
	
	/**
	 * 
	 * @Description 保存机构对应的权限范围 
	 * @param list
	 * void
	 * @author caoying
	 * 2018年9月18日上午9:27:37
	 */
	@DataResolver
	public void save(List<SmDeptAutRange> list) {
		String userName = SessionUtil.getSecurityUser().getCname(); //获取登录用户名
        for(SmDeptAutRange bean:list){
            EntityState state = EntityUtils.getState(bean);
            if(state.equals(EntityState.NEW)){
            	bean.setCreateDate(new Date());
            	bean.setLastUpdatePerson(userName);
            	bean.setLastUpdateTime(new Date());
                service.save(bean);
            }
            if(state.equals(EntityState.MODIFIED)){
            	bean.setLastUpdatePerson(userName);
            	bean.setLastUpdateTime(new Date());
                service.update(bean);
            }
        }        
    }

	/**
	 * 
	 * @Description 删除机构对应的权限范围 
	 * @param id
	 * void
	 * @author caoying
	 * 2018年9月18日上午9:27:37
	 */
    @Expose
    public void deleteById(Integer id) {
        service.delete(service.findById(id));
    }
}
