package com.lanshan.web.admin.sm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanshan.core.base.service.BaseServiceImpl;
import com.lanshan.web.admin.model.SmEnums;
import com.lanshan.web.admin.sm.dao.SmEnumsDao;
import com.lanshan.web.admin.sm.dao.impl.SmEnumsDaoImpl;

@Service
@Transactional(rollbackFor = Exception.class)
public class SmEnumsService extends BaseServiceImpl{

	@Autowired
	public void setDao(SmEnumsDao dao) {
		super.setDao(dao);
	}
	
	/**
     * @根据字典项来获得所有的字典项
     * @param categoryId：字典编码ID
     * @return  SmEnums:字典项集合
     */
    public List<SmEnums> getEnumsByCategoryId(Integer categoryId, String systemId){
        return ((SmEnumsDaoImpl)dao).getEnumsByCategoryId(categoryId, systemId);
    }
}
