package com.geariot.platform.freelycar_wechat.entities;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.geariot.platform.freelycar_wechat.utils.JsonDateDeserialize;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Card {
	private int id;
	private Service service;
	@JsonDeserialize(using=JsonDateDeserialize.class)
	private Date payDate;
	@JsonDeserialize(using=JsonDateDeserialize.class)
	private Date expirationDate;
	private int payMethod; // 0,1,2,3,4  现金,刷卡,支付宝,微信,易付宝
	private Set<CardProjectRemainingInfo> projectInfos;
	private Admin orderMaker;
	private String cardNumber;
	private boolean failed;
	
	private int handlerId;//办理人
	
	
	private float balance;// 卡内的余额
	
	
	
	//因为被Client里面有Card 关联这里只是 用作查询的时候的映射
	@Transient
	private int clientId;
	public int getClientId() {
		return clientId;
	}
	
	public void setClientId(int clientId) {
		this.clientId = clientId;
	}
	
	public Date getExpirationDate() {
		return expirationDate;
	}
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	@ManyToOne(cascade={}, fetch=FetchType.EAGER)
	@JoinColumn(name="makerId", foreignKey=@ForeignKey(name="none"))
	public Admin getOrderMaker() {
		return orderMaker;
	}
	public Date getPayDate() {
		return payDate;
	}
	public int getPayMethod() {
		return payMethod;
	}
	@OneToMany(cascade={CascadeType.ALL}, fetch=FetchType.EAGER)
	@JoinColumn(name="cardId", foreignKey=@ForeignKey(name="none"))
	public Set<CardProjectRemainingInfo> getProjectInfos() {
		return projectInfos;
	}
	@ManyToOne
	@JoinColumn(name="serviceId", foreignKey=@ForeignKey(name="none"))
	public Service getService() {
		return service;
	}
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setOrderMaker(Admin orderMaker) {
		this.orderMaker = orderMaker;
	}
	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}	
	public void setPayMethod(int payMethod) {
		this.payMethod = payMethod;
	}
	public void setProjectInfos(Set<CardProjectRemainingInfo> projectInfos) {
		this.projectInfos = projectInfos;
	}
	public void setService(Service service) {
		this.service = service;
	}
	
	
	public float getBalance() {
		return balance;
	}
	public void setBalance(float balance) {
		this.balance = balance;
	}
	@Override
	public String toString() {
		return "Card [id=" + id + ", service=" + service + ", payDate=" + payDate + ", expirationDate=" + expirationDate
				+ ", payMethod=" + payMethod + ", projectInfos=" + projectInfos + ", orderMaker=" + orderMaker
				+ ", cardNumber=" + cardNumber + "]";
	}
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	public boolean isFailed() {
		return failed;
	}
	public void setFailed(boolean failed) {
		this.failed = failed;
	}

	public int getHandlerId() {
		return handlerId;
	}

	public void setHandlerId(int handlerId) {
		this.handlerId = handlerId;
	}
	
	
}
