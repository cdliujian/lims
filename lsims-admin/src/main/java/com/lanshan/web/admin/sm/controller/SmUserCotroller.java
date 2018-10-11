package com.lanshan.web.admin.sm.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.provider.Page;
import com.lanshan.core.base.controller.BaseController;
import com.lanshan.core.business.IUser;
import com.lanshan.core.util.SessionUtil;
import com.lanshan.core.business.IUser;
import com.lanshan.core.util.SessionUtil;
import com.lanshan.web.admin.model.SmUser;
import com.lanshan.web.admin.sm.service.SmUserService;

/**
 * 
 * @Description 用户管理Cotroller
 *
 * @author caoying 2018年9月3日 下午3:47:04
 */
@Controller
public class SmUserCotroller extends BaseController {
	@Autowired
	public void setService(SmUserService service) {
		super.setService(service);
	}

	/**
	 * 
	 * @Description 查询用户信息
	 * @param page
	 * @param param
	 *            void
	 * @author caoying 2018年9月3日下午5:18:50
	 */
	@DataProvider
	public void loadQueryResult(Page page, Map param) {
		// TODO Auto-generated method stub
		IUser user = SessionUtil.getSecurityUser();
		System.out.println(user.getUsername());
		super.loadQueryResult(page, param);
		
	}

	/**
	 * 
	 * @Description 保存用户信息
	 * @param obj
	 * @throws Exception
	 *             void
	 * @author caoying 2018年9月4日上午9:22:06
	 */
	@DataResolver
	public void save(SmUser obj) throws Exception {
		IUser user = SessionUtil.getSecurityUser();
		if (user == null) {
			throw new Exception("请先登录系统后操作！");
		}
		if (obj.getDept() == null || obj.getDept().getId() == null) {
			throw new Exception("请选择用户的所属机构！");
		}
		// 判断手机号格式是否正确
		// if (!CommonUtils.isMobileNo(obj.getMobile())) {
		// throw new Exception("请输入正确的手机号码！");
		// }

		if (StringUtils.isBlank(obj.getEnabled()) || obj.getEnabled().equals("0")) {
			throw new Exception("该用户已经被删除，不能修改！");
		}

		// 判断用户名是否已经存在
		HashMap amap = new HashMap();
		amap.put("usernameEq", obj.getUsername());

		// 如果是更新操作，则判断用户名已经存在要去掉本身
		if (null != obj.getId()) {
			amap.put("idNeq", obj.getId());
		}
		List list = service.queryList(amap);
		if (list.size() > 0) {
			throw new Exception("用户账号已经存在！");
		}

		((SmUserService) service).saveUser(obj);
	}

	/**
	 * 
	 * @Description 删除用户信息
	 * @param userId
	 *            void
	 * @author caoying 2018年9月4日上午10:56:17
	 */
	@Expose
	public void deleteById(Integer userId) throws Exception {
		((SmUserService) service).deleteUser(userId);
	}

	/**
	 * 
	 * @Description 启用用户
	 * @param userId
	 * @throws Exception
	 *             void
	 * @author caoying 2018年9月4日上午11:02:53
	 */
	@Expose
	public void resume(Integer userId) throws Exception {
		((SmUserService) service).saveResume(userId);
	}

	/**
	 * 
	 * @Description 修改密码
	 * @param params
	 * @throws Exception
	 *             void
	 * @author caoying 2018年9月4日上午10:18:09
	 */
	@DataResolver
	public void savePassword(SmUser obj) throws Exception {
		((SmUserService) service).savePassword(obj);
	}

	/**
	 * 
	 * 通过用户查看权限
	 * 
	 * @param userId
	 * @return List<Bdf2Url>
	 * @author 朱郑韬 2018年9月21日下午5:17:55
	 */
	@DataProvider
	public List viewPepdom(Integer userId) {
		return ((SmUserService) service).findUrlResourceView(userId);
	}
}
