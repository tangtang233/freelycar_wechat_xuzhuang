package com.geariot.platform.freelycar_wechat.entities;

import java.util.Date;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.FetchType;
import javax.persistence.CascadeType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.geariot.platform.freelycar_wechat.utils.JsonDateDeserialize;

/**
 * @author mxy940127
 *
 */

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class WXPayOrder {
	private String id;
	private String openId;		//微信openId
	private int clientId;				//微信卡券订单对应clientId
	private float totalPrice;		//支付金额
	@JsonDeserialize(using=JsonDateDeserialize.class)
	private Date createDate;	//订单产生时间
	private Date finishDate;	//支付完时间
	private int payState;		//订单支付状态,0未支付，1支付完成
	private Service service;	//购买的卡类
	private String productName; //产品名称
	private Set<FavourToOrder> favours;//购买券
	private int payMethod;		//支付方式，只有微信0
	public Date getFinishDate() {
		return finishDate;
	}
	public float getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(float totalPrice) {
		this.totalPrice = totalPrice;
	}
	public void setFinishDate(Date finishDate) {
		this.finishDate = finishDate;
	}

	@Id
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public int getPayState() {
		return payState;
	}
	public void setPayState(int payState) {
		this.payState = payState;
	}
	@ManyToOne
	@JoinColumn(name="serviceId", foreignKey=@ForeignKey(name="none"))
	public Service getService() {
		return service;
	}
	public void setService(Service service) {
		this.service = service;
	}
	@OneToMany(cascade={CascadeType.ALL}, fetch=FetchType.EAGER)
	@JoinColumn(name="wxOrderId", foreignKey=@ForeignKey(name="none"))
	public Set<FavourToOrder> getFavours() {
		return favours;
	}
	public void setFavours(Set<FavourToOrder> favours) {
		this.favours = favours;
	}
	public int getPayMethod() {
		return payMethod;
	}
	public void setPayMethod(int payMethod) {
		this.payMethod = payMethod;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public int getClientId() {
		return clientId;
	}
	public void setClientId(int clientId) {
		this.clientId = clientId;
	}
	
}

