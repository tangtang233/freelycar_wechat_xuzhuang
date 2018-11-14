package com.geariot.platform.freelycar_wechat.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExpendOrder {
	private int id;
	private String type;
	private float amount;
	private Date payDate;
	private String comment;
	private String reference;
	public float getAmount() {
		return amount;
	}
	public String getComment() {
		return comment;
	}
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public Date getPayDate() {
		return payDate;
	}
	public String getType() {
		return type;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
}
