package com.lanshan.core.base.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.springframework.orm.hibernate5.HibernateTemplate;

public interface BaseDao<T, PK> {

	void save(T entity);


	void delete(T entity);


	void deleteAll(List<T> entities);

    void saveOrUpdate(T entity);

    T findById(PK id);
    
    void update(T entity);

    List<T> findAll(Object... args);

    Object getUniqueObject(String hql, Object... params);

    List<T> findAllbyhql(String hql);

    void deletebyhql(String hql);

    public int count(Map param);
	public List queryList(Map param);
	public Object query(Map param);
	public HibernateTemplate getTemplate();
	public Session getSession();
	
}
