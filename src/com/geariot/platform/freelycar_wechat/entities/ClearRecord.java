/**
 * 
 */
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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author mxy940127
 *	供应商清算记录
 */

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClearRecord {
	private int id;				//数据库自增Id
	private int providerId;		//清算供应商的Id
	private String inStockDate;		//入库日期
	private float totalPrice;	//合计金额
	private float clearPrice;   //清算金额
	private Date clearDate;		//清算时间
	private int payState;		//清算状态 0,1=已结清,未结清
	private Admin orderMaker;	//清算人
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getProviderId() {
		return providerId;
	}
	public void setProviderId(int providerId) {
		this.providerId = providerId;
	}
	public String getInStockDate() {
		return inStockDate;
	}
	public void setInStockDate(String inStockDate) {
		this.inStockDate = inStockDate;
	}
	public float getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(float totalPrice) {
		this.totalPrice = totalPrice;
	}
	public float getClearPrice() {
		return clearPrice;
	}
	public void setClearPrice(float clearPrice) {
		this.clearPrice = clearPrice;
	}
	public Date getClearDate() {
		return clearDate;
	}
	public void setClearDate(Date clearDate) {
		this.clearDate = clearDate;
	}
	public int getPayState() {
		return payState;
	}
	public void setPayState(int payState) {
		this.payState = payState;
	}
	@ManyToOne(cascade={}, fetch=FetchType.EAGER)
	@JoinColumn(name="adminId", foreignKey=@ForeignKey(name="none"))
	public Admin getOrderMaker() {
		return orderMaker;
	}
	public void setOrderMaker(Admin orderMaker) {
		this.orderMaker = orderMaker;
	}
	
}
