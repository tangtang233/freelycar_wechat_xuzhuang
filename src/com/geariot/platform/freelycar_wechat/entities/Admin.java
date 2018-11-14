package com.geariot.platform.freelycar_wechat.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.geariot.platform.freelycar_wechat.utils.JsonDateDeserialize;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Admin {
	private int id;
	private String account;
	private String password;
	private String name;
	private Staff staff;
	private Role role;
	private boolean current;
	@JsonDeserialize(using=JsonDateDeserialize.class)
	private Date createDate;
	private String comment;
	public String getAccount() {
		return account;
	}
	public String getComment() {
		return comment;
	}
	
	public Date getCreateDate() {
		return createDate;
	}
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getPassword() {
		return password;
	}
	@ManyToOne(cascade={}, fetch=FetchType.EAGER)
	@JoinColumn(name="roleId", foreignKey=@ForeignKey(name="none"))
	public Role getRole() {
		return role;
	}
	@OneToOne(cascade={}, fetch=FetchType.EAGER)
	@JoinColumn(name="staffId", foreignKey=@ForeignKey(name="none"))
	public Staff getStaff() {
		return staff;
	}
	public boolean isCurrent() {
		return current;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public void setCurrent(boolean current) {
		this.current = current;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	public void setStaff(Staff staff) {
		this.staff = staff;
	}
	@Override
	public String toString() {
		return "Admin [id=" + id + ", account=" + account + ", password=" + password + ", name=" + name + ", staff="
				+ staff + ", role=" + role + ", current=" + current + ", createDate=" + createDate + ", comment="
				+ comment + "]";
	}
	
}
