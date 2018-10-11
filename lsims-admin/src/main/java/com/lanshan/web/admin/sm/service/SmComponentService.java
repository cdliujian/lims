package com.lanshan.web.admin.sm.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanshan.core.base.service.BaseServiceImpl;
import com.lanshan.core.business.IUser;
import com.lanshan.core.util.SessionUtil;
import com.lanshan.web.admin.model.SmUrl;
import com.lanshan.web.admin.sm.dao.SmComponentDao;
import com.lanshan.web.admin.sm.dao.SmUserDao;

@Service
@Transactional(rollbackFor = Exception.class)
public class SmComponentService extends BaseServiceImpl {
	
	final static Log log = LogFactory.getLog("loginLogger");
	
	@Autowired
	public void setDao( SmComponentDao dao){
		super.setDao(dao);
	}
	
	@Autowired
	private SmUserDao smUserDao;
	
	public void deleteComp(Integer id){
    	delete(dao.findById(id));
    }
	
	/**
	 * 根据用户查询菜单资源
	 * 
	 * @param userId
	 *            用户ID
	 * @param isNavigation
	 *            是否用于导航
	 * @param topMenuName
	 *            项级菜单名
	 * @param systemCode
	 *            系统标识
	 * @param makeUrl
	 *            是否需要组装全路径URL
	 * @return
	 */
	public List<SmUrl> findMenuByUser(Integer userId,boolean isNavigation,String topMenuName,List<String> systemCode){
		return smUserDao.findMenuByUser(userId, isNavigation, topMenuName,systemCode,true);
	}
	
	/**
	 * 供前端获取菜单资源
	 * @return
	 */
	public List userRes(Map map){ 
		long start = System.currentTimeMillis();
		String systemCode=(String) map.get("sysCodes");
		List sysCode=null;
		if(StringUtils.isNotBlank(systemCode)){
			String[] arr=systemCode.split(",");
			sysCode=new ArrayList();
			for(String s:arr){
				if(StringUtils.isNotBlank(s)){
					sysCode.add(s);
				}
			}
		} 
		IUser user = SessionUtil.getSecurityUser();
		List list= findMenuByUser(user==null?1:user.getId(),true,null,sysCode);
		long end=System.currentTimeMillis();
		if(end-start>500){
			log.info("MenuComponentService#userRes 时间："+(end-start)+"毫秒，用户："+user.getUsername());
		}
		return list;
	}
}
