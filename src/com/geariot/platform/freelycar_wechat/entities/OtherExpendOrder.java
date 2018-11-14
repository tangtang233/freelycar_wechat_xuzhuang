package com.geariot.platform.freelycar_wechat.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class OtherExpendOrder {
	private String id;
	private String typeName;
	private int typeId;
	private float amount;
	private String comment;
	private Date expendDate;
	private Date createDate;
	public float getAmount() {
		return amount;
	}
	public String getComment() {
		return comment;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public Date getExpendDate() {
		return expendDate;
	}
	@Id
	public String getId() {
		return id;
	}
	public int getTypeId() {
		return typeId;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public void setExpendDate(Date expendDate) {
		this.expendDate = expendDate;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
}
