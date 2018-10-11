package com.lanshan.core.base.service;

import java.util.List;
import java.util.Map;

public interface BaseService {

	public void save(Object obj);
	public void update(Object obj);
	public void delete(Object obj);
	public Object findById(Object id);
	
	public int count(Map param);
	public List queryList(Map param);
	public Object query(Map param);
}
