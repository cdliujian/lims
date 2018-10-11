package com.lanshan.web.admin.sm.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.lanshan.core.base.service.BaseServiceImpl;
import com.lanshan.core.util.SessionUtil;
import com.lanshan.web.admin.model.SmEnumCategorys;
import com.lanshan.web.admin.model.SmEnums;
import com.lanshan.web.admin.sm.dao.SmEnumCategorysDao;
import com.lanshan.web.admin.sm.dao.SmEnumsDao;
import com.lanshan.web.admin.sm.utils.DeptUtils;

@Service
@Transactional(rollbackFor = Exception.class)
public class SmEnumCategorysService extends BaseServiceImpl{
	
	@Resource
	private SmEnumsDao smEnumsDao;
	
	@Autowired
	public void setDao(SmEnumCategorysDao dao) {
		super.setDao(dao);
	}
	
	@Override
    public void save(Object obj) {
        // 首先保存字典
        super.save(obj);
        // 判断是否有字典项，如果有也要保存
        SmEnumCategorys e = (SmEnumCategorys) obj;
        List<SmEnums> list = e.getEnumses();
        if (list != null && list.size() != 0) {
        	String username = SessionUtil.getSecurityUser().getUsername();
        	Date now = e.getCreateDate();
            for (SmEnums bean : list) {
            	bean.setLastUpdatePerson(username);
            	bean.setLastUpdateTime(now);
                bean.setCategoryId(e.getId());
                bean.setSystemId(e.getSystemId());
                bean.setCreateDate(now);
                smEnumsDao.save(bean);
            }
        }
    }
	
	@Override
    public void update(Object obj) {
        // 首先更新字典
        super.update(obj);
        // 判断字典项是要更新还是增加操作-----------------begin
        SmEnumCategorys e = (SmEnumCategorys) obj;
        List<SmEnums> list = e.getEnumses();
        if (list != null && list.size() != 0) {
        	String username = e.getLastUpdatePerson();
        	Date now = e.getLastUpdateTime();
            for (SmEnums bean : list) {
            	bean.setLastUpdatePerson(username);
            	bean.setLastUpdateTime(now);
                if (bean.getId() == null || bean.getId() == 0) {
                    bean.setCategoryId(e.getId());
                    bean.setSystemId(e.getSystemId());
                    bean.setCreateDate(now);
                    smEnumsDao.save(bean);
                } else {
                	bean.setLastUpdateTime(now);
                	bean.setLastUpdatePerson(username);
                	smEnumsDao.update(bean);
                }
            }
        }
    }
	
	@Override
	public void delete(Object obj) {
		SmEnumCategorys e = (SmEnumCategorys)obj;
		super.delete(obj);
		//删除字典项
		Map param = new HashMap();
		String systemId = DeptUtils.getSystemIdByCurUser();
		param.put("categoryId", e.getId());
		param.put("systemId", systemId);
		smEnumsDao.deleteAll(this.smEnumsDao.queryList(param));
	}
}
