package com.lanshan.web.admin.sm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanshan.core.base.service.BaseServiceImpl;
import com.lanshan.web.admin.sm.dao.SmPreferencesDao;

@Service
@Transactional(rollbackFor = Exception.class)
public class SmPreferencesService  extends BaseServiceImpl{
	@Autowired
	public void setDao( SmPreferencesDao dao){
		super.setDao(dao);
	}
}
