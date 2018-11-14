package com.geariot.platform.freelycar_wechat.entities;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class FavourToOrder {
	private int id;
	private Favour favour;	//
	private int count;
	private WXPayOrder wxPayOrder;
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="wxOrderId", foreignKey=@ForeignKey(name="none"))
	public WXPayOrder getWxPayOrder() {
		return wxPayOrder;
	}
	public void setWxPayOrder(WXPayOrder wxPayOrder) {
		this.wxPayOrder = wxPayOrder;
	}
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@ManyToOne
	@JoinColumn(name="favourId", foreignKey=@ForeignKey(name="none"))
	public Favour getFavour() {
		return favour;
	}
	public void setFavour(Favour favour) {
		this.favour = favour;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
}
