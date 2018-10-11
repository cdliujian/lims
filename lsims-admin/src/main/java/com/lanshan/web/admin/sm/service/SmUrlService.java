package com.lanshan.web.admin.sm.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.lanshan.core.base.service.BaseServiceImpl;
import com.lanshan.core.util.SessionUtil;
import com.lanshan.web.admin.model.SmComponent;
import com.lanshan.web.admin.model.SmRole;
import com.lanshan.web.admin.model.SmUrl;
import com.lanshan.web.admin.model.SmUrlComponent;
import com.lanshan.web.admin.sm.dao.SmComponentDao;
import com.lanshan.web.admin.sm.dao.SmRoleDao;
import com.lanshan.web.admin.sm.dao.SmUrlComponentDao;
import com.lanshan.web.admin.sm.dao.SmUrlDao;

@Service
@Transactional(rollbackFor = Exception.class)
public class SmUrlService extends BaseServiceImpl{
	
	@Resource
	private SmComponentDao smComponentDao;
	
	@Resource
	private SmUrlComponentDao smUrlComponentDao;
	
	@Resource
	private SmRoleDao smRoleDao;
	
	@Autowired
	public void setService(SmUrlDao dao){
		super.setDao(dao);
	}
	
	@Override
	public void save(Object obj) {
		String username = SessionUtil.getSecurityUser().getUsername();
		SmUrl url = (SmUrl)obj;
		Date now = new Date();
		url.setCreateDate(now);
		super.save(obj);
		SmRole role = smRoleDao.getSmRole();
		// 批量增加菜单所拥有的资源，并将按钮资源分配给默认角色
        List<SmComponent> list = url.getComponents();
        if (list != null) {
            for (SmComponent bean : list) {
                bean.setUrlId(url.getId());
                bean.setCreateDate(now);
                bean.setLastUpdateTime(now);
                bean.setLastUpdatePerson(username);
                bean.setSystemId(url.getSystemId());
                smComponentDao.save(bean);
                
                //将按钮资源分配给默认角色
                if(role != null){
                	SmUrlComponent r2c = new SmUrlComponent();
                    r2c.setRoleId(role.getId());
                    r2c.setUrlId(bean.getUrlId());
                    r2c.setSystemId(url.getSystemId());
                    r2c.setCreateDate(now);
                    r2c.setLastUpdateTime(now);
                    r2c.setLastUpdatePerson(username);
                    this.smUrlComponentDao.save(r2c);
                }
            }
        }
	}
	
	@Override
	public void update(Object obj) {
		String username = SessionUtil.getSecurityUser().getUsername();
		SmUrl url = (SmUrl) obj;
        EntityState urlState = EntityUtils.getState(obj);
        //修改SmUrl
        if(!urlState.equals(EntityState.NONE)){
        	dao.update(url);
        }

        List<SmComponent> list = url.getComponents();
        if (list != null) {
        	Date now = new Date();
            for (SmComponent bean : list) {
            	bean.setLastUpdatePerson(username);
            	bean.setLastUpdateTime(now);
            	bean.setSystemId(url.getSystemId());;
            	EntityState state = EntityUtils.getState(bean);
            	//新增url对应的资源
                if (bean.getId() == null && state.equals(EntityState.NEW)) {
                	bean.setCreateDate(now);
                    bean.setUrlId(url.getId());
                    smComponentDao.save(bean);
                } else {
                	//修改对应资源
                	if(state.equals(EntityState.MODIFIED))
                		smComponentDao.update(bean); 
                	//删除对应资源
                	else if(state.equals(EntityState.DELETED))
                		smComponentDao.delete(bean);
                }
            }
        }
	}
	
	@Override
    public void delete(Object obj) {
		SmUrl smUrl = (SmUrl) obj;
		//检查是否包含子菜单
		validateHasSubs(smUrl.getId());
        
        Map amap = new HashMap();
        amap.put("urlId", smUrl.getId());
        List<SmComponent> components = smComponentDao.queryList(amap);
        
        if(components != null && !components.isEmpty()){  //存在菜单资源
        	//删除角色和菜单的对应关系
        	smUrlComponentDao.deleteByUrlId(smUrl.getId(), smUrl.getSystemId());
            //删除菜单对应的资源 
            smComponentDao.deleteAll(components);  
        } 

        //删除本身菜单信息
        this.dao.delete(obj);
    }
	
	private void validateHasSubs(Integer urlId) {
		Map param = new HashMap();
		param.put("parentId", urlId);
		List<SmUrl> subs = dao.queryList(param);
    	if(subs != null && subs.size() > 0){
    		throw new RuntimeException("请先删除子菜单!");
    	}
    }
}
