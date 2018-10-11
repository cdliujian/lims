package com.lanshan.web.admin.sm.service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanshan.core.base.service.BaseServiceImpl;
import com.lanshan.web.admin.model.SmRegion;
import com.lanshan.web.admin.sm.dao.SmRegionDao;

@Service
@Transactional(rollbackFor = Exception.class)
public class SmRegionService extends BaseServiceImpl {
	@Autowired
	public void setDao( SmRegionDao dao){
		super.setDao(dao);
	}
	
	/**
	 * @Description 根据传入的regionid，获得此地区的全路径
	 * @param id
	 * @return 全路径,如：湖南 长沙 岳麓区
	 */
	public String regionPath(int id) {
		String s = "";
		SmRegion r = (SmRegion) this.findById(id);
		s = r.getRegionName();

		while (null != r.getParentId()) {
			r = (SmRegion) this.findById(r.getParentId());
			s = r.getRegionName() + " " + s;
		}

		return s;
	}
	
	public SmRegion getParentRegionById(Integer id) {
		HashMap amap = new HashMap();
		amap.put("id", id);
		SmRegion region = (SmRegion) this.query(amap);

		amap = new HashMap();
		amap.put("id", region.getParentId());
		return (SmRegion) this.query(amap);

	}
}
