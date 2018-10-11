package com.lanshan.web.admin.sm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanshan.core.base.service.BaseServiceImpl;
import com.lanshan.web.admin.model.SmSystem;
import com.lanshan.web.admin.sm.dao.SmSystemDao;

@Service
@Transactional(rollbackFor = Exception.class)
public class SmSystemService extends BaseServiceImpl {
	@Autowired
	public void setDao( SmSystemDao dao){
		super.setDao(dao);
	}
}
