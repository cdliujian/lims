package com.lanshan.web.admin.model;
// Generated 2018-8-28 17:07:00 by Hibernate Tools 5.2.0.CR1

import static javax.persistence.GenerationType.IDENTITY;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Formula;

import com.bstek.dorado.annotation.PropertyDef;

@Entity
@Table(name = "SM_USER")
public class SmUser implements java.io.Serializable {

	@PropertyDef(label = "", description = "")

	private Integer id;

	@PropertyDef(label = "", description = "")

	private String username;

	@PropertyDef(label = "", description = "")

	private String address;

	@PropertyDef(label = "", description = "")

	private String cname;

	@PropertyDef(label = "0：否  1：是", description = "")

	private String administrator;

	@PropertyDef(label = "", description = "")

	private Date birthday;

	@PropertyDef(label = "", description = "")

	private Integer deptId;

	@PropertyDef(label = "", description = "")

	private Date createDate;

	@PropertyDef(label = "", description = "")

	private String email;

	@PropertyDef(label = "0：否  1：是", description = "")

	private String enabled;

	@PropertyDef(label = "", description = "")

	private String ename;

	@PropertyDef(label = "", description = "")

	private String male;

	@PropertyDef(label = "", description = "")

	private String mobile;

	@PropertyDef(label = "", description = "")

	private String password;

	@PropertyDef(label = "", description = "")

	private String salt;

	@PropertyDef(label = "", description = "")

	private String lastUpdatePerson;

	@PropertyDef(label = "", description = "")

	private Date lastUpdateTime;

	@PropertyDef(label = "", description = "")

	private String weixinOpenId;
	
	private SmDept dept;
	
	private String oldPassword = "";
	private String newPassword = "";
	private String againPassword = "";
	//用户所有的岗位
	private List<SmPosition> positions=new ArrayList<SmPosition>();
	 
	private String positionsFullName;

	public SmUser() {

	}

	@Transient
	public java.io.Serializable get__id() {
		return this.id;
	}

	public SmUser(String username, String address, String cname, String administrator, Date birthday, Integer deptId,
			Date createDate, String email, String enabled, String ename, String male, String mobile, String password,
			String salt, String lastUpdatePerson, Date lastUpdateTime, String weixinOpenId) {
		this.username = username;
		this.address = address;
		this.cname = cname;
		this.administrator = administrator;
		this.birthday = birthday;
		this.deptId = deptId;
		this.createDate = createDate;
		this.email = email;
		this.enabled = enabled;
		this.ename = ename;
		this.male = male;
		this.mobile = mobile;
		this.password = password;
		this.salt = salt;
		this.lastUpdatePerson = lastUpdatePerson;
		this.lastUpdateTime = lastUpdateTime;
		this.weixinOpenId = weixinOpenId;
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

	@Column(name = "USERNAME_", length = 32)
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "ADDRESS_", length = 120)
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "CNAME_", length = 32)
	public String getCname() {
		return this.cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	@Column(name = "ADMINISTRATOR_", length = 1)
	public String getAdministrator() {
		return this.administrator;
	}

	public void setAdministrator(String administrator) {
		this.administrator = administrator;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "BIRTHDAY_", length = 19)
	public Date getBirthday() {
		return this.birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

//	@Column(name = "DEPT_ID")
	@Transient
	public Integer getDeptId() {
		return this.deptId;
	}

	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_DATE_", length = 19)
	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name = "EMAIL_", length = 32)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "ENABLED_", length = 1)
	public String getEnabled() {
		return this.enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	@Column(name = "ENAME_", length = 32)
	public String getEname() {
		return this.ename;
	}

	public void setEname(String ename) {
		this.ename = ename;
	}

	@Column(name = "MALE_", length = 1)
	public String getMale() {
		return this.male;
	}

	public void setMale(String male) {
		this.male = male;
	}

	@Column(name = "MOBILE_", length = 32)
	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Column(name = "PASSWORD_", length = 60)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "SALT_", length = 32)
	public String getSalt() {
		return this.salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
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

	@Column(name = "WEIXIN_OPEN_ID", length = 60)
	public String getWeixinOpenId() {
		return this.weixinOpenId;
	}

	public void setWeixinOpenId(String weixinOpenId) {
		this.weixinOpenId = weixinOpenId;
	}

	/*
	 * 多对一关联关系
	 * 延迟加载：fetch = FetchType.LAZY
	 * 引用外键：DEPT_ID
	 * 
	 */
	@ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name = "DEPT_ID")
	public SmDept getDept() {
		return dept;
	}

	public void setDept(SmDept dept) {
		this.dept = dept;
	}

	@Transient
	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	@Transient
	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	@Transient
	public String getAgainPassword() {
		return againPassword;
	}

	public void setAgainPassword(String againPassword) {
		this.againPassword = againPassword;
	}

	@Transient
	public List<SmPosition> getPositions() {
		return positions;
	}

	public void setPositions(List<SmPosition> positions) {
		this.positions = positions;
	}

	@Formula("(SELECT GROUP_CONCAT(p.NAME_) FROM sm_user_position up, sm_position p WHERE up.POSITION_ID_ = p.ID AND up.USER_ID = ID)")
	public String getPositionsFullName() {
		return positionsFullName;
	}

	public void setPositionsFullName(String positionsFullName) {
		this.positionsFullName = positionsFullName;
	}

}
