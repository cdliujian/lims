package com.lanshan.web.admin.sm.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanshan.core.base.service.BaseServiceImpl;
import com.lanshan.web.admin.model.SmDept;
import com.lanshan.web.admin.model.SmDeptAutRange;
import com.lanshan.web.admin.sm.dao.SmDeptAutRangeDao;
import com.lanshan.web.admin.sm.utils.SmConstant;

@Service
@Transactional(rollbackFor = Exception.class)
public class SmDeptAutRangeService extends BaseServiceImpl {

	@Autowired
	public void setDao( SmDeptAutRangeDao dao){
		super.setDao(dao);
	}
	
	@Autowired
	private SmDeptService smDeptService;
	
	
	public String generateScopeSQL(String filed, String alias,Integer deptId) {
		Map queryMap = new HashMap();
		queryMap.put("deptId", deptId);
		List<SmDeptAutRange> ranges = dao.queryList(queryMap);
		String aliasFiled = StringUtils.isBlank(alias) ? filed : alias + "." + filed;

		if (ranges == null || ranges.isEmpty()) { // 没有配置数据范围，只允许访问自身数据
			return aliasFiled + "=" + deptId;
		}
		String sql = "create temporary table tmp_deptaut (DEPT_ID integer not null) ENGINE=MEMORY  select "+deptId +" DEPT_ID";
		dao.getSession().createNativeQuery("DROP TEMPORARY TABLE IF EXISTS tmp_deptaut").executeUpdate();
		dao.getSession().createNativeQuery(sql).executeUpdate();
		
		for (SmDeptAutRange range : ranges) {
			Integer rangeDeptId = range.getRangeDeptId();
			SmDept rangeDept =  (SmDept)smDeptService.findById(rangeDeptId);
			if (rangeDept.getParentId() == null) {
				return "";
			}
			if (range.getKind().equals(SmConstant.DATA_SCOPE_CURRENT)) {
				dao.getSession().createNativeQuery("insert into tmp_deptaut select "+rangeDeptId +" DEPT_ID").executeUpdate();
			}else if (range.getKind().equals(SmConstant.DATA_SCOPE_CASCADE)) {
				sql = "insert into tmp_deptaut select id DEPT_ID from ( "+
					  "  select t1.id, "+
					  "   if(find_in_set(parent_id, @pids) > 0, @pids := concat(@pids, ',', id), 0) as ischild "+
					  "         from ( "+
					  "           select id,parent_id from sm_dept t   order by parent_id, id"+
					  "            ) t1, "+ 
					  "            (select @pids := 2) t2 "+
					  "          ) t3 where ischild != 0" ;
				dao.getSession().createNativeQuery(sql).executeUpdate();	
					
				
			}
		}
		String ret = " AND EXSITS (select DEPT_ID from tmp_deptaut where DEPT_ID="+aliasFiled + ")";
		return ret;
	}
}
