package com.geariot.platform.freelycar_wechat.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.geariot.platform.freelycar_wechat.utils.JsonDateDeserialize;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Inventory {
	private String id;
	private String name;
//	private InventoryType type;
//	private InventoryBrand brand;
	private int typeId;
	private String typeName;
	private int brandId;
	private String brandName;
	private String standard;
	private String property;
	private float price;
	private float amount;
	private String manufactureNumber;		//v2 添加厂家编号
	private Provider provider;
	private String comment;
	@JsonDeserialize(using=JsonDateDeserialize.class)
	private Date createDate;
	
	
	
	//inner class
	
	public static class InvenInner{
		public InvenInner(){
		}
		
		public InvenInner(String id, String name){
			this.id = id;
			this.name = name;
		}
		
		private String id;
		private String name;
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
	}
	
	
	
	
	
	
	
	
	
	public float getAmount() {
		return amount;
	}
	public int getBrandId() {
		return brandId;
	}
	public String getBrandName() {
		return brandName;
	}
	public String getComment() {
		return comment;
	}
	public Date getCreateDate() {
		return createDate;
	}
	@Id
	public String getId() {
		return id;
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
	@ManyToOne(cascade={}, fetch=FetchType.EAGER)
	@JoinColumn(name="providerId", foreignKey=@ForeignKey(name="none"))
	public Provider getProvider() {
		return provider;
	}
	public String getStandard() {
		return standard;
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
	public void setBrandId(int brandId) {
		this.brandId = brandId;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public void setId(String id) {
		this.id = id;
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
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getManufactureNumber() {
		return manufactureNumber;
	}
	public void setManufactureNumber(String manufactureNumber) {
		this.manufactureNumber = manufactureNumber;
	}
	
	
}
