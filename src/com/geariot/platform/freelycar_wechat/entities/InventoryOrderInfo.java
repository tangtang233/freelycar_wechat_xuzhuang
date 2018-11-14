package com.geariot.platform.freelycar_wechat.entities;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class InventoryOrderInfo {
	private int id;
	private String inventoryId;
	private String name;
	private String typeName;
	private String brandName;
	private String standard;
	private String property;
	private Provider provider;
	private float amount;
	private float price;
	public float getAmount() {
		return amount;
	}
	public String getBrandName() {
		return brandName;
	}
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public String getInventoryId() {
		return inventoryId;
	}
	public String getName() {
		return name;
	}
	public float getPrice() {
		return price;
	}
	public String getProperty() {
		return property;
	}
	@ManyToOne
	@JoinColumn(name="providerId", foreignKey=@ForeignKey(name="none"))
	public Provider getProvider() {
		return provider;
	}
	public String getStandard() {
		return standard;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setInventoryId(String inventoryId) {
		this.inventoryId = inventoryId;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	public void setProvider(Provider provider) {
		this.provider = provider;
	}
	public void setStandard(String standard) {
		this.standard = standard;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
}
