package com.lanshan.core.base.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.ResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.util.NumberUtils;

import com.lanshan.core.business.IUser;
import com.lanshan.core.util.AnalyzerUtil;
import com.lanshan.core.util.Constant;
import com.lanshan.core.util.HibernateHelper;
import com.lanshan.core.util.ReflectHelper;
import com.lanshan.core.util.ResultTransformerHelper;
import com.lanshan.core.util.SessionUtil;
public abstract class BaseDaoImpl<T, PK extends Serializable> extends HibernateDaoSupport implements BaseDao<T,PK> {

	@Autowired
    public void InitSession(SessionFactory sessionFactory) {

	      setSessionFactory(sessionFactory);

	  }
	
	private Class<T> clazz;
	private ProjectionList _prolist = null;
	
	public BaseDaoImpl(){
		if( clazz == null )
		{
			Class cl = getClass();
	        java.lang.reflect.Type superType = cl.getGenericSuperclass();
	        if(superType instanceof ParameterizedType)
	        {
	            java.lang.reflect.Type paramTypes[] = ((ParameterizedType)superType).getActualTypeArguments();
	            if(paramTypes.length > 0)
	            	clazz = (Class)paramTypes[0];
	        } 
		}
	}
	
	public Class<T> getEntityClass(){
		return clazz;
	}
	
	@Override
	public void save(T entity) {
		// TODO Auto-generated method stub
		 getHibernateTemplate().save(entity);
	}

	@Override
	public void delete(T entity) {
		// TODO Auto-generated method stub
		getHibernateTemplate().delete(entity);
	}

	@Override
	public void deleteAll(List<T> entities) {
		// TODO Auto-generated method stub
		getHibernateTemplate().deleteAll(entities);
		
	}

	@Override
	public void saveOrUpdate(T entity) {
		// TODO Auto-generated method stub
		getHibernateTemplate().saveOrUpdate(entity);
		
	}

	@Override
	public T findById(PK id) {
		// TODO Auto-generated method stub
		return this.getHibernateTemplate().get(clazz, (Serializable)id);

	}

	@Override
	public void update(T entity) {
		// TODO Auto-generated method stub
		getHibernateTemplate().update(entity);
		
	}

	@Override
	public List<T> findAll(Object... args) {
		// TODO Auto-generated method stub
		return getHibernateTemplate().loadAll(clazz);
		
	}

	@Override
	public Object getUniqueObject(String hql, Object... params) {
		// TODO Auto-generated method stub
		return null;
	}

	

	@Override
	public void deletebyhql(String hql) {
		// TODO Auto-generated method stub
		
	}

	

	

	@Override
	public List<T> findAllbyhql(String hql) {
		// TODO Auto-generated method stub
		return null;
	}

	public void evict(Object entity)
	{
		this.getHibernateTemplate().evict(entity);
	}
	public int count(Map param) {
		// TODO Auto-generated method stub
		DetachedCriteria criteria = getCountDetachedCriteria(param);
		List list = this.findByCriteria(criteria);
		if( list.isEmpty() )
			return 0;
		return Integer.valueOf(list.get(0).toString());
	}
	
	protected DetachedCriteria getCountDetachedCriteria(Map param)
	{
		DetachedCriteria criteria = getDetachedCriteria(param);
		criteria.setProjection(Projections.rowCount());
		return criteria;
	}

	public List queryList(Map param) {
		// TODO Auto-generated method stub
		DetachedCriteria criteria = getQueryListDetachedCriteria(param);
		int firstResult = -1;
		int maxResults = -1;
		Object o =  param.get(Constant.LIMIT_ROWSTART);
		if( o != null )
		{
			firstResult = Integer.valueOf(o.toString());
		}
		o =  param.get(Constant.LIMIT_ROWNUM);
		if( o != null )
		{
			maxResults = Integer.valueOf(o.toString());
		}
		return this.findByCriteria(criteria,firstResult,maxResults);
	}
	protected DetachedCriteria getQueryListDetachedCriteria(Map param){
		DetachedCriteria criteria = getDetachedCriteria(param);	
		ProjectionList prolist = this._prolist;
		if( prolist == null )
		{
			prolist = Projections.projectionList();
			this._prolist = prolist;
			setSelectColumn(prolist);			
		}		
		setOrder(criteria);
		if( prolist.getLength() > 0)
		{
			criteria.setProjection(prolist);
			criteria.setResultTransformer(getResultTransformer());
		}
		return criteria;
	}
	
	protected void setAssociate(DetachedCriteria criteria)
	{
		
	}
	
	protected void setSelectColumn(ProjectionList prolist)
	{
		_prolist = prolist;
	}
	protected void setOrder(DetachedCriteria criteria)
	{
		
	}
	protected ResultTransformer getResultTransformer()
	{
		return ResultTransformerHelper.getResultTransformer(clazz);
	}
	private static final String SPLIT_CHAR = "\\.";
	private static final String INDEXOF_CHAR = ".";
	protected void setQuery(DetachedCriteria criteria,Map param){
		if( param != null )
		{
			Iterator iter = param.keySet().iterator();
			if( propertyMapping == null )
				propertyMapping = getPropertyMapping();
			if( propertyOperMapping == null )
				propertyOperMapping = getPropertyOperMapping();
			if( propertyTypeMapping == null )
				propertyTypeMapping = getPropertyTypeMapping();
			while( iter.hasNext() )
			{
				String key = String.valueOf(iter.next());
				String keyns[] = key.split(SPLIT_CHAR);
				String name = null;
				Object v = param.get(key);
				Integer restrictions = 1;
				if( keyns.length > 1 )
				{
					name = key.substring(0,key.lastIndexOf(INDEXOF_CHAR));
					Object oper = propertyOperMapping.get(name);
					if( oper != null )	
						restrictions = (Integer)restrictionsMap.get(oper);						
					else
						restrictions = (Integer)restrictionsMap.get(keyns[keyns.length - 1]);
					if( restrictions == null )
					{
						restrictions = 1;
						name = key;
					}					
				}
				else
				{
					name = key;					
				}
				Object mname = propertyMapping.get(name);
		          if (mname != null)
		          {
		            if (restrictions == Constant.LC_ALIKE)
		            {
		              try
		              {
		                String[] vs = AnalyzerUtil.phrase(v.toString().trim());
		                restrictions = Constant.LC_LIKE;
		                if (vs != null)
		                  v = vs;
		              }
		              catch (Exception e)
		              {
		              }
		            }
		            if ((((v instanceof Object[])) || ((v instanceof Collection))) && (restrictions != Constant.LC_IN))
		            {
		              if ((v instanceof Object[])) {
		                Object[] os = (Object[])v;
		                for (int i = 0; i < os.length; i++)
		                {
		                  addRestrictions(criteria, restrictions.intValue(), (String)mname, os[i]);
		                }
		              }
		              else if ((v instanceof Collection))
		              {
		                Collection coll = (Collection)v;
		                Iterator viter = coll.iterator();
		                for (int i = 0; i < coll.size(); i++)
		                {
		                  addRestrictions(criteria, restrictions.intValue(), (String)mname, viter.next());
		                }
		              }
		              else
		              {
		            	  addRestrictions(criteria,restrictions,(String)mname,v);
		              }
		            }
		            else
		            	addRestrictions(criteria,restrictions,(String)mname,v);
		          }
			}
		}		
		
	}
	
	private Map propertyMapping = null;
	private Map propertyOperMapping = null;
	private Map propertyTypeMapping = null;
	 
	protected Map getPropertyMapping()
	{		
		Map m  = new HashMap();
		String pns[]= HibernateHelper.getBasicPropertyNames(clazz);
		for( int i=0;i<pns.length;i++ )
		{
			m.put(pns[i], pns[i]);
		}	
		return m;
	}
	protected Map getPropertyTypeMapping() {
	    Map m = new HashMap();
	    String[] pns = HibernateHelper.getBasicPropertyNames(clazz);

	    for (int i = 0; i < pns.length; i++)
	    {
	      m.put(pns[i], HibernateHelper.getReturnedClass(clazz, pns[i]));
	    }
	    return m;
	  }

	protected Map getPropertyOperMapping()
	{		
		return new HashMap();
	}
	
	private static Map restrictionsMap = new HashMap();
	static {
		 restrictionsMap.put("=", Constant.LC_EQ);
		    restrictionsMap.put("<>", Constant.LC_NE);
		    restrictionsMap.put("!=", Constant.LC_NE);
		    restrictionsMap.put(">=", Constant.LC_GE);
		    restrictionsMap.put("<=", Constant.LC_LE);
		    restrictionsMap.put(">", Constant.LC_GT);
		    restrictionsMap.put("<", Constant.LC_LT);

		    restrictionsMap.put("eq", Constant.LC_EQ);
		    restrictionsMap.put("ne", Constant.LC_NE);
		    restrictionsMap.put("ge", Constant.LC_GE);
		    restrictionsMap.put("le", Constant.LC_LE);
		    restrictionsMap.put("gt", Constant.LC_GT);
		    restrictionsMap.put("lt", Constant.LC_LT);

		    restrictionsMap.put("in", Constant.LC_IN);
		    restrictionsMap.put("like", Constant.LC_LIKE);
		    restrictionsMap.put("ilike", Constant.LC_ILIKE);
		    restrictionsMap.put("endlike", Constant.LC_LIKE_END);
		    restrictionsMap.put("startlike", Constant.LC_LIKE_START);
		    restrictionsMap.put("notNull", Constant.LC_NOTNULL);
		    restrictionsMap.put("isNull", Constant.LC_ISNULL);

		    restrictionsMap.put("alike", Constant.LC_ALIKE);
	}
	 private Object tranform(String name, Object value)
	  {
	    if (this.propertyTypeMapping != null)
	    {
	      Class pc = (Class)this.propertyTypeMapping.get(name);
	      if (pc != null)
	      {
	        Class vc = value.getClass();

	        if (pc.isAssignableFrom(vc))
	          return value;
	        //如果为集合类型，尝试将之中的类型进行转换
	        if( value instanceof Collection )
	        {
	        	Collection c = (Collection)value;
	        	Iterator iter = c.iterator();
	        	if( iter.hasNext() )
	        	{
	        		Object cv = iter.next();
	        		vc = cv.getClass();
	        		//如果发现第一个类型跟实体属性的类型匹配，将不进行转换
	        		if ( pc.isAssignableFrom(vc) )
	        		{
	        			return value;	        			
	        		}	        		
	        	}
	        	List ac = new ArrayList();
	        	iter = c.iterator();
	        	while( iter.hasNext() )
	        	{
	        		ac.add(tranform(name, iter.next()));
	           	}
	        	return ac;
	        	
	        }
	        else if( value instanceof Object[] )
	        {
	        	Object[] c = (Object[])value;
	        	
	        	if( c.length > 0 )
	        	{
	        		vc = c[0].getClass();
	        		//如果发现第一个类型跟实体属性的类型匹配，将不进行转换
	        		if ( pc.isAssignableFrom(vc) )
	        		{
	        			return value;	        			
	        		}	 
	        	}
	        	
	        	Object[] ac =  new Object[c.length];
	        	for( int i=0;i<c.length;i++ )
	        	{
	        		ac[i] = tranform(name, c[i]);
	        	}
	        	return ac;
	        }	        
	        else if (CharSequence.class.isAssignableFrom(pc))
	        {
	          value = value.toString();
	        }
	        else if (((value instanceof CharSequence)) && (Number.class.isAssignableFrom(pc)))
	        {
	          return NumberUtils.parseNumber(value.toString(), pc);
	        }
	      }
	    }
	    return value;
	  }
	private void addRestrictions(DetachedCriteria criteria,int chr,String name,Object value)
	{
	    try
	    {
	    	 value = tranform(name, value);
	    }
	    catch (Exception e)
	    {
	    }
		switch (chr) {
		case 1:
			criteria.add(Restrictions.eq(name,value));
			break;
		case 2:
			criteria.add(Restrictions.ne(name,value));
			break;
		case 3:
			criteria.add(Restrictions.ge(name,value));
			break;
		case 4:
			criteria.add(Restrictions.le(name,value));
			break;	
		case 5:
			criteria.add(Restrictions.gt(name,value));
			break;
		case 6:
			criteria.add(Restrictions.lt(name,value));
			break;
		case 10:
			if( value instanceof Collection )
			{
				criteria.add(Restrictions.in(name,(Collection)value));
			}
			else if( value instanceof Object[])
			{
				criteria.add(Restrictions.in(name,(Object[])value));
			}
			else if ((value instanceof String))
		      {
				criteria.add(Restrictions.in(name, value.toString().split(","))); 
		      }			
			break;
		case 11:
			criteria.add(Restrictions.like(name,value.toString().trim(),MatchMode.ANYWHERE));
			break;
		case 12:
			criteria.add(Restrictions.ilike(name,value.toString().trim(),MatchMode.ANYWHERE));
			break;
		case 13:
			criteria.add(Restrictions.like(name,value.toString().trim(),MatchMode.END));
			break;
		case 14:
			criteria.add(Restrictions.like(name,value.toString().trim(),MatchMode.START));
			break;
		case 15:
			criteria.add(Restrictions.isNotNull(name));
			break;
		case 16:
			criteria.add(Restrictions.isNull(name));
			break;
		default:
			criteria.add(Restrictions.eq(name,value));
			break;
		}
	}
	
	public final String scopeAlias="_alias";
	public final String scopeFiled="_filedName";
	protected void setQueryScope(DetachedCriteria criteria,Map params){
		if(params!=null && !params.isEmpty()){
			String alias=(String) params.get(scopeAlias);
			String colname=(String) params.get(scopeFiled);  
			if(StringUtils.isNotBlank(colname)){
				if(StringUtils.isBlank(alias)){
					alias="{alias}";
				}
				String sql=null;
				try {
					sql = generateScopeSQL(colname, alias);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//CommonUtils.setDeptScope( criteria, colname, alias);
				if(StringUtils.isNotEmpty(sql)){
					criteria.add(Restrictions.sqlRestriction(sql));	
				}
			}
		}
	}
	
	/**
	 * 生成当前用户的数据范围sql
	 * 
	 * @return
	 * @throws Exception 
	 */
	public String generateScopeSQL(String field, String alias) throws Exception {
		if (StringUtils.isBlank(field)) {
			return "";
		}
		IUser user = SessionUtil.getSecurityUser();
		if(user==null)
			return "";
		try {
			return (String) ReflectHelper.invokeService("smDeptAutRangeService", "generateScopeSQL", field,alias);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("系统内部异常");
		}
		
	}
	
	protected DetachedCriteria getDetachedCriteria(Map param)
	{
		DetachedCriteria criteria = DetachedCriteria.forClass(clazz);
		setAssociate(criteria);	
		setQuery(criteria,param);	
		setQueryScope(criteria,param);
		return criteria;
	}
	
	
	
	protected List findByCriteria(DetachedCriteria criteria)
	{
		return this.getHibernateTemplate().findByCriteria(criteria);
	}
	
	protected List findByCriteria(DetachedCriteria criteria,int firstResult,int maxResults)
	{
		return this.getHibernateTemplate().findByCriteria(criteria,firstResult,maxResults);
	}

	

	@Override
	public Object query(Map param) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public HibernateTemplate getTemplate(){
		return this.getHibernateTemplate();
	}
	
	public Session getSession(){
		return this.getSessionFactory().getCurrentSession();
	}

}
