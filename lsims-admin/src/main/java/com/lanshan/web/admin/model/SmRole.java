package com.lanshan.web.admin.model;
// Generated 2018-8-31 15:09:09 by Hibernate Tools 5.2.0.CR1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Formula;

import com.bstek.dorado.annotation.PropertyDef;

@Entity
@Table(name = "sm_role")
public class SmRole implements java.io.Serializable {

	@PropertyDef(label = "", description = "")

	private Integer id;

	@PropertyDef(label = "", description = "")

	private String desc;

	@PropertyDef(label = "", description = "")

	private String enabled;

	@PropertyDef(label = "", description = "")

	private String name;

	@PropertyDef(label = "分布式系统中标识", description = "")

	private String systemId;
	
	private Date createDate;
	
	/**
	 * 角色对应的岗位id
	 */
	private String rolePosiIds;
	/**
	 * 角色对应的岗位名
	 */
	private String rolePosiNames;
	
	private String lastUpdatePerson;
	private Date lastUpdateTime; 
	
	/**
	 * 菜单和资源值，多个以“，”分割,为了页面显示
	 */
	private String roleResIds;
	/**
	 * 菜单和资源名称值，多个以“，”分割,为了页面显示
	 */
	private String roleResNames;
	
	@Column(name = "LAST_UPDATE_PERSON", length = 32)
	public String getLastUpdatePerson() {
		return this.lastUpdatePerson;
	}

	public void setLastUpdatePerson(String lastUpdatePerson) {
		this.lastUpdatePerson = lastUpdatePerson;
	}
	
	@Column(name = "LAST_UPDATE_TIME")
	public Date getLastUpdateTime() {
		return this.lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	@Transient
	public String getRoleResIds() {
		return roleResIds;
	}

	public void setRoleResIds(String roleResIds) {
		this.roleResIds = roleResIds;
	}

	@Transient
	public String getRoleResNames() {
		return roleResNames;
	}

	public void setRoleResNames(String roleResNames) {
		this.roleResNames = roleResNames;
	}

	@Formula("(select group_concat(p.NAME_) from SM_ROLE_MEMBER rm, SM_POSITION p  where rm.POSITION_ID_=p.ID and ID = rm.ROLE_ID_)")
	public String getRolePosiNames() {
		return rolePosiNames;
	}

	public void setRolePosiNames(String rolePosiNames) {
		this.rolePosiNames = rolePosiNames;
	}

	@Formula("(select GROUP_CONCAT(p.ID) from sm_role_member rm, sm_position p where rm.POSITION_ID_ = p.ID and ID = rm.ROLE_ID_)")
	public String getRolePosiIds() {
		return rolePosiIds;
	}

	public void setRolePosiIds(String rolePosiIds) {
		this.rolePosiIds = rolePosiIds;
	}

	@Transient
	public java.io.Serializable get__id() {
		return this.id;
	}

	@Column(name = "CREATE_DATE_")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
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

	@Column(name = "DESC_", length = 120)
	public String getDesc() {
		return this.desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Column(name = "ENABLED_", length = 1)
	public String getEnabled() {
		return this.enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	@Column(name = "NAME_", length = 32)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "SYSTEM_ID_", length = 60)
	public String getSystemId() {
		return this.systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

}
