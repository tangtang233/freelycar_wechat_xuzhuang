package com.geariot.platform.freelycar_wechat.model;

import java.util.Date;

public class ExpendStat {
	private float amount;
	private Date payDate;
	public float getAmount() {
		return amount;
	}
	public Date getPayDate() {
		return payDate;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}
	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}
}
