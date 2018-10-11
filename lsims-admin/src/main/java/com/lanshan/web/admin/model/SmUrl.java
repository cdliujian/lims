package com.lanshan.web.admin.model;
// Generated 2018-8-31 15:09:09 by Hibernate Tools 5.2.0.CR1

import static javax.persistence.GenerationType.IDENTITY;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.bstek.dorado.annotation.PropertyDef;

@Entity
@Table(name = "sm_url" )
public class SmUrl implements java.io.Serializable {

	@PropertyDef(label = "", description = "")

	private Integer id;

	@PropertyDef(label = "", description = "")

	private String desc;

	@PropertyDef(label = "", description = "")

	private String icon;

	@PropertyDef(label = "", description = "")

	private String name;

	@PropertyDef(label = "", description = "")

	private Integer order;

	@PropertyDef(label = "", description = "")

	private Integer parentId;

	@PropertyDef(label = "分布式系统中标识这个菜单前缀域名", description = "")

	private String systemId;

	@PropertyDef(label = "", description = "")

	private String url;

	@PropertyDef(label = "", description = "")

	private String code;
	
	private Date createDate;
	
	//与菜单绑定的资源
	private List<SmComponent> components = new ArrayList<SmComponent>();
	
	//子菜单
	private List<SmUrl> children = new ArrayList<SmUrl>();
	
	@Transient
	public List<SmUrl> getChildren() {
		return children;
	}

	public void setChildren(List<SmUrl> children) {
		this.children = children;
	}

	@Transient
	public List<SmComponent> getComponents() {
		return components;
	}

	public void setComponents(List<SmComponent> components) {
		this.components = components;
	}

	public SmUrl() {

	}

	public SmUrl(Integer id) {
		this.id = id;

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

	@Column(name = "ICON_", length = 120)
	public String getIcon() {
		return this.icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	@Column(name = "NAME_", length = 60)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "ORDER_")
	public Integer getOrder() {
		return this.order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	@Column(name = "PARENT_ID_")
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

	@Column(name = "URL_", length = 120)
	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Column(name = "CODE", length = 60)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
