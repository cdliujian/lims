package com.lanshan.web.admin.model;
// Generated 2018-9-17 11:30:00 by Hibernate Tools 5.2.3.Final

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * SmDeptAutRange generated by hbm2java
 */
@Entity
@Table(name = "sm_dept_aut_range")
public class SmDeptAutRange implements java.io.Serializable {

	private Integer id;
	private Integer deptId;
	private Integer rangeDeptId;
	private Character kind;
	private String lastUpdatePerson;
	private Date lastUpdateTime;
	private Date createDate;
	
	private String authRangeName;
	
	private SmDept rangeDept;     //用于关联包含机构对象

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "ID", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name = "CREATE_DATE_")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name = "DEPT_ID")
	public Integer getDeptId() {
		return this.deptId;
	}

	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}

	@Column(name = "RANGE_DEPT_ID")
	public Integer getRangeDeptId() {
		return this.rangeDeptId;
	}

	public void setRangeDeptId(Integer rangeDeptId) {
		this.rangeDeptId = rangeDeptId;
	}

	@Column(name = "KIND", length = 1)
	public Character getKind() {
		return this.kind;
	}

	public void setKind(Character kind) {
		this.kind = kind;
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
	public String getAuthRangeName() {
		return authRangeName;
	}

	public void setAuthRangeName(String authRangeName) {
		this.authRangeName = authRangeName;
	}

	@Transient
	public SmDept getRangeDept() {
		return rangeDept;
	}

	public void setRangeDept(SmDept rangeDept) {
		this.rangeDept = rangeDept;
	}

}
