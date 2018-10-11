package com.lanshan.web.admin.model;
// Generated 2018-8-31 15:09:09 by Hibernate Tools 5.2.0.CR1

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.bstek.dorado.annotation.PropertyDef;

@Entity
@Table(name = "sm_system")
public class SmSystem implements java.io.Serializable {

	@PropertyDef(label = "全英文", description = "")

	private String systemId;

	@PropertyDef(label = "", description = "")

	private String desc;

	@PropertyDef(label = "存一些可能字典需要记录数值的信息", description = "")

	private String memo;

	@PropertyDef(label = "", description = "")

	private String lastUpdatePerson;

	@PropertyDef(label = "", description = "")

	private Date lastUpdateTime;
	
	private Date createDate;

	public SmSystem() {

	}

	public SmSystem(String systemId) {
		this.systemId = systemId;

	}

	@Transient
	public java.io.Serializable get__id() {
		return this.systemId;
	}

	@Column(name = "CREATE_DATE_")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Id

	@Column(name = "SYSTEM_ID_", unique = true, nullable = false, length = 60)
	public String getSystemId() {
		return this.systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	@Column(name = "DESC_", length = 120)
	public String getDesc() {
		return this.desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Column(name = "MEMO_", length = 120)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
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

}
