package com.lanshan.core.base.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lanshan.core.base.dao.BaseDao;

public abstract class BaseServiceImpl implements BaseService {

	protected BaseDao dao;
	
	public void setDao(BaseDao dao)
	{
		this.dao = dao;
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void save(Object obj) {
		// TODO Auto-generated method stub
		dao.save(obj);

	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void update(Object obj) {
		// TODO Auto-generated method stub
		dao.update(obj);

	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void delete(Object obj) {
		// TODO Auto-generated method stub
		dao.delete(obj);

	}

	@Override
	public Object findById(Object id) {
		// TODO Auto-generated method stub
		return dao.findById(id);
	}

	@Override
	public int count(Map param) {
		// TODO Auto-generated method stub
		return dao.count(param);
	}

	@Override
	public List queryList(Map param) {
		// TODO Auto-generated method stub
		return dao.queryList(param);
	}

	@Override
	public Object query(Map param) {
		// TODO Auto-generated method stub
		return this.dao.query(param);
	}

}
