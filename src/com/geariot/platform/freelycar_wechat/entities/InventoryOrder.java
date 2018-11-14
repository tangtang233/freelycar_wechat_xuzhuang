package com.geariot.platform.freelycar_wechat.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class InventoryOrder {
	private String id;
	private List<InventoryOrderInfo> inventoryInfos;
	private int type;	//0,1,2,3=入库,维修出库,美容出库,退货出库
	//private float totalAmount;  无用字段
	private float totalPrice;
	private Admin orderMaker;
	private int state;	//0,1=已完成,已作废等
	private int payState;     //0,1,2 = 已结清,未结清,待付款
	private int providerId;   //入库单据供应商Id
	private String providerName;   //入库单据供应商Id
	private float needPayAmount;   // 待结清金额
	private Date payDate;			//清算时间
	private Date createDate;

	public Date getCreateDate() {
		return createDate;
	}
	@Id
	public String getId() {
		return id;
	}
	@OneToMany(cascade={CascadeType.ALL}, fetch=FetchType.EAGER)
	@JoinColumn(name="inventoryOrderId", foreignKey=@ForeignKey(name="none"))
	public List<InventoryOrderInfo> getInventoryInfos() {
		return inventoryInfos;
	}
	@ManyToOne(cascade={}, fetch=FetchType.EAGER)
	@JoinColumn(name="makerId", foreignKey=@ForeignKey(name="none"))
	public Admin getOrderMaker() {
		return orderMaker;
	}
	public int getState() {
		return state;
	}
	/*public float getTotalAmount() {
		return totalAmount;
	}*/
	public float getTotalPrice() {
		return totalPrice;
	}
	public int getType() {
		return type;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setInventoryInfos(List<InventoryOrderInfo> inventoryInfos) {
		this.inventoryInfos = inventoryInfos;
	}
	public void setOrderMaker(Admin orderMaker) {
		this.orderMaker = orderMaker;
	}
	public void setState(int state) {
		this.state = state;
	}
	/*public void setTotalAmount(float totalAmount) {
		this.totalAmount = totalAmount;
	}*/
	public void setTotalPrice(float totalPrice) {
		this.totalPrice = totalPrice;
	}
	public void setType(int type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "InventoryOrder [id=" + id + ", inventoryInfos=" + inventoryInfos + ", type=" + type + ", totalPrice="
				+ totalPrice + ", orderMaker=" + orderMaker + ", state=" + state + ", createDate=" + createDate + "]";
	}
	public int getProviderId() {
		return providerId;
	}
	public void setProviderId(int providerId) {
		this.providerId = providerId;
	}
	public int getPayState() {
		return payState;
	}
	public void setPayState(int payState) {
		this.payState = payState;
	}
	public float getNeedPayAmount() {
		return needPayAmount;
	}
	public void setNeedPayAmount(float needPayAmount) {
		this.needPayAmount = needPayAmount;
	}
	public Date getPayDate() {
		return payDate;
	}
	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}
	
	@Transient
	public String getProviderName() {
		return providerName;
	}
	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}
	
	
	
}
