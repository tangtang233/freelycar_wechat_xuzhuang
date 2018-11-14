package com.geariot.platform.freelycar_wechat.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class IncomeOrder {
	private int id;
	private int clientId;
	private String licensePlate;
	private float amount;
	private Date payDate;
	private int payMethod; //// 0,1,2,3,4,5,6  现金,刷卡,支付宝,微信,易付宝,储值卡,混合支付
	private String programName;
	// private String staffNames;
	private boolean member;

	public IncomeOrder() {
		super();
	}
	
	//续卡的构造 肯定是会员 因此member=true
	public IncomeOrder(int clientId, float amount, Date payDate, int payMethod, String programName) {
		super();
		this.clientId = clientId;
		this.amount = amount;
		this.payDate = payDate;
		this.payMethod = payMethod;
		this.programName = programName;
		this.member = true;
	}

	public float getAmount() {
		return amount;
	}

	public int getClientId() {
		return clientId;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getId() {
		return id;
	}

	public String getLicensePlate() {
		return licensePlate;
	}

	public Date getPayDate() {
		return payDate;
	}

	public int getPayMethod() {
		return payMethod;
	}

	public String getProgramName() {
		return programName;
	}

	// public String getStaffNames() {
	// return staffNames;
	// }
	public void setAmount(float amount) {
		this.amount = amount;
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}

	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}

	public void setPayMethod(int payMethod) {
		this.payMethod = payMethod;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	// public void setStaffNames(String staffNames) {
	// this.staffNames = staffNames;
	// }
	public boolean isMember() {
		return member;
	}

	public void setMember(boolean member) {
		this.member = member;
	}
}
