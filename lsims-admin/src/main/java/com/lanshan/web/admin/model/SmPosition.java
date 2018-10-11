package com.lanshan.web.admin.model;
// Generated 2018-9-17 10:50:52 by Hibernate Tools 5.2.3.Final

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

/**
 * SmPosition generated by hbm2java
 */
@Entity
@Table(name = "sm_position")
public class SmPosition implements java.io.Serializable {

	private Integer id;
	private String name;
	private String desc;
	private String code;
	private String lastUpdatePerson;
	private Date lastUpdateTime;
	private String systemId;
	private Date createDate;

	// 关联岗位
	private String rolesFullName;
	// 岗位下面所有的用户
	private List<SmUser> users;
	// 用于保存岗位和角色的对应关系
	private List<SmRole> roles;

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

	@Column(name = "NAME_", length = 32)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DESC_", length = 120)
	public String getDesc() {
		return this.desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Column(name = "CODE_", length = 32)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "LAST_UPDATE_PERSON", length = 10)
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

	@Column(name = "SYSTEM_ID_", length = 60)
	public String getSystemId() {
		return this.systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	@Transient
	public List<SmUser> getUsers() {
		return users;
	}

	public void setUsers(List<SmUser> users) {
		this.users = users;
	}

	@Transient
	public List<SmRole> getRoles() {
		return roles;
	}

	public void setRoles(List<SmRole> roles) {
		this.roles = roles;
	}

	@Formula("(SELECT group_concat(R.NAME_) FROM sm_role R, sm_role_member RM WHERE R.ID = RM.ROLE_ID_ AND RM.POSITION_ID_ = ID)")
	public String getRolesFullName() {
		return rolesFullName;
	}

	public void setRolesFullName(String rolesFullName) {
		this.rolesFullName = rolesFullName;
	}

}