package com.lanshan.web.admin.sm.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanshan.core.base.service.BaseServiceImpl;
import com.lanshan.core.util.Md5PasswordEncoder;
import com.lanshan.core.util.SessionUtil;
import com.lanshan.web.admin.model.SmDept;
import com.lanshan.web.admin.model.SmUser;
import com.lanshan.web.admin.sm.dao.SmUserDao;

/**
 * 
 * @Description 用户管理Service实现
 *
 * @author caoying 2018年9月4日 上午9:21:26
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SmUserService extends BaseServiceImpl {

	@Autowired
	private SmUserDao smUserDao;

	@Autowired
	private SmDeptService deptServ;

	@Autowired
	private SmUserPositionService userPositionService;

	@Autowired
	public void setDao(SmUserDao dao) {
		super.setDao(dao);
	}

	/**
	 * 
	 * @Description 保存用户信息
	 * @param user
	 *            void
	 * @author caoying 2018年9月4日上午9:21:15
	 */
	public void saveUser(SmUser user) {
		String userName = SessionUtil.getSecurityUser().getCname(); // 获取登录用户名
		SmDept dept = (SmDept) deptServ.findById(user.getDept().getId());
		user.setDept(dept);

		// 保存用户信息,ID为空则新增否则修改
		if (null == user.getId()) {
			// 新增
			// 根据用户输入密码，采用MD5的加密方式，随机生成密文与盐值
			String password = user.getPassword();
			String[] pwdInfo = createPassword(password);
			user.setSalt(pwdInfo[1]);
			user.setPassword(pwdInfo[0]);
			// 系统不能创建系统管理员，创建的用户都是非管理员用户
			user.setAdministrator("0");
			user.setCreateDate(new Date());
			user.setLastUpdatePerson(userName);
			user.setLastUpdateTime(new Date());

			dao.save(user);
		} else {
			// 修改
			user.setLastUpdatePerson(userName);
			user.setLastUpdateTime(new Date());

			dao.update(user);
		}

		
		// 保存用户岗位关联信息
		userPositionService.saveUserPosition(user.getPositions(), user.getId(), user.getUsername(), user.getCname());
	}

	/**
	 * 
	 * @Description 修改密码
	 * @param pwdData
	 *            void
	 * @author caoying 2018年9月4日上午10:20:18
	 */
	public void savePassword(SmUser user) throws Exception {
		Integer userId = user.getId();
		String orginPwd = user.getOldPassword();
		String newPwd = user.getNewPassword();
		String againPwd = user.getAgainPassword();
		// if (StringUtils.isBlank(orginPwd)) {
		// throw new Exception("请输入原始密码！");
		// }
		if (StringUtils.isBlank(newPwd)) {
			throw new Exception("请输入新的密码！");
		}
		if (StringUtils.isBlank(againPwd)) {
			throw new Exception("请重复输入新的密码！");
		}
		if (!newPwd.equals(againPwd)) {
			throw new Exception("两次新密码不一致，请重新输入！");
		}

		// if (newPwd.equals(orginPwd)) {
		// throw new Exception("修改的新密码和原密码一样，请重新输入！");
		// }
		// 查找用户信息
		SmUser oldUser = (SmUser) dao.findById(userId);
		// 验证原密码是否正确
		Md5PasswordEncoder passwordEncoder = new Md5PasswordEncoder();
		// String password = passwordEncoder.encodePassword(orginPwd,
		// oldUser.getSalt());
		// if (!password.equals(oldUser.getPassword())) {
		// throw new Exception("您输入的原密码不正确，密码修改失败！");
		// }

		String newPassword = passwordEncoder.encodePassword(newPwd, oldUser.getSalt());
		oldUser.setPassword(newPassword);
		dao.update(oldUser);
	}

	/**
	 * 
	 * @Description 删除用户信息
	 * @param userId
	 *            void
	 * @author caoying 2018年9月4日上午10:56:53
	 */
	public void deleteUser(Integer userId) throws Exception {
		SmUser user = (SmUser) dao.findById(userId);

		if (user == null) {
			throw new Exception("用户未注册或已失效！");
		}

		if (StringUtils.isBlank(user.getEnabled()) || user.getEnabled().equals("0")) {
			throw new Exception("用户已经删除，请不要重复操作！");
		}
		user.setEnabled("0");
		dao.update(user);
	}

	/**
	 * 
	 * @Description 启用用户
	 * @param userId
	 * @throws Exception
	 *             void
	 * @author caoying 2018年9月4日上午11:03:47
	 */
	public void saveResume(Integer userId) throws Exception {
		SmUser u = (SmUser) this.findById(userId);

		if (u == null) {
			throw new Exception("用户未注册或已失效！");
		}

		if (!StringUtils.isBlank(u.getEnabled()) && u.getEnabled().equals("1")) {
			throw new Exception("用户为启用状态，无需操作");
		}
		u.setEnabled("1");
		dao.update(u);
	}

	/**
	 * 
	 * @Description 生成新密码和盐值
	 * @return String[]
	 * @author caoying 2018年9月4日上午9:26:45
	 */
	private String[] createPassword(String plain) {
		Md5PasswordEncoder passwordEncoder = new Md5PasswordEncoder();
		int intSalt = RandomUtils.nextInt(1000);
		String salt = intSalt + "";
		String password = passwordEncoder.encodePassword(plain, salt);
		return new String[] { password, salt, plain };
	}

	public List findUrlResourceView(Integer userId) {
		return smUserDao.findUrlResourceView(userId);
	}
}
