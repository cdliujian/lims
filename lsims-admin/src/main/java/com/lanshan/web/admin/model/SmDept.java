package com.lanshan.web.admin.model;
// Generated 2018-8-31 15:09:09 by Hibernate Tools 5.2.0.CR1

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Formula;

import com.bstek.dorado.annotation.PropertyDef;

@Entity
@Table(name = "sm_dept")
public class SmDept implements java.io.Serializable {

	@PropertyDef(label = "", description = "")

	private Integer id;

	@PropertyDef(label = "", description = "")

	private Date createTime;

	@PropertyDef(label = "", description = "")

	private String desc;

	@PropertyDef(label = "", description = "")

	private String name;

	@PropertyDef(label = "", description = "")

	private Integer parentId;

	@PropertyDef(label = "分布式系统中标识", description = "")

	private String systemId;

	@PropertyDef(label = "0:否 1：是", description = "")

	private String enabled;

	@PropertyDef(label = "", description = "")

	private Integer provinceId;

	@PropertyDef(label = "", description = "")

	private Integer cityId;

	@PropertyDef(label = "", description = "")

	private Integer areaId;

	@PropertyDef(label = "", description = "")

	private String address;

	@PropertyDef(label = "", description = "")

	private String lastUpdatePerson;

	@PropertyDef(label = "", description = "")

	private Date lastUpdateTime;
	
	private List<SmDept> children; // 子机构
	private List<SmDept> parents; // 父机构
	private String parentName; //上级机构名称
	private String areaName; //区域名称

	public SmDept() {

	}

	@Transient
	public java.io.Serializable get__id() {
		return this.id;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "ID", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TIME", length = 19)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "DESC_", length = 400)
	public String getDesc() {
		return this.desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Column(name = "NAME_", length = 60)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "PARENT_ID")
	public Integer getParentId() {
		return this.parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	@Column(name = "SYSTEM_ID_", length = 60)
	public String getSystemId() {
		return this.systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	@Column(name = "ENABLED_", length = 1)
	public String getEnabled() {
		return this.enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	@Column(name = "PROVINCE_ID")
	public Integer getProvinceId() {
		return this.provinceId;
	}

	public void setProvinceId(Integer provinceId) {
		this.provinceId = provinceId;
	}

	@Column(name = "CITY_ID")
	public Integer getCityId() {
		return this.cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	@Column(name = "AREA_ID")
	public Integer getAreaId() {
		return this.areaId;
	}

	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}

	@Column(name = "ADDRESS", length = 400)
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "LAST_UPDATE_PERSON", length = 32)
	public String getLastUpdatePerson() {
		return this.lastUpdatePerson;
	}

	public void setLastUpdatePerson(String lastUpdatePerson) {
		this.lastUpdatePerson = lastUpdatePerson;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_UPDATE_TIME", length = 19)
	public Date getLastUpdateTime() {
		return this.lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	@Transient
	public List<SmDept> getChildren() {
		return children;
	}

	public void setChildren(List<SmDept> children) {
		this.children = children;
	}

	@Transient
	public List<SmDept> getParents() {
		return parents;
	}

	public void setParents(List<SmDept> parents) {
		this.parents = parents;
	}

	@Formula("(SELECT p.NAME_ from sm_dept c, sm_dept p WHERE c.PARENT_ID = p.ID AND c.ID = ID)")
	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	@Formula("(SELECT r.REGION_NAME from sm_region r where r.ID = AREA_ID)")
	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

}
