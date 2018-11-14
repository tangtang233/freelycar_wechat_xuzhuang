package com.geariot.platform.freelycar_wechat.entities;

import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.geariot.platform.freelycar_wechat.utils.JsonDateDeserialize;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProjectInfo {
	private int id;
	private int projectId;
	private String name;
	private float price;
	private int clientId;
	private float presentPrice;     //项目现价
	private int payMethod;			//0,1,2=扣卡次，付现金，扣券
	private String cardId;
	private String cardName;
	private String ticketId;
	private String favourName;
	private String cardNumber;
	private int payCardTimes;
	private float referWorkTime;
	private float pricePerUnit;
	private Set<Staff> staffs;
	private String clientName;
	private String licensePlate;
	private String brandName;
	@JsonDeserialize(using=JsonDateDeserialize.class)
	private Date createDate;
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getLicensePlate() {
		return licensePlate;
	}
	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getCardId() {
		return cardId;
	}
	public String getCardName() {
		return cardName;
	}
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public int getPayCardTimes() {
		return payCardTimes;
	}
	public int getPayMethod() {
		return payMethod;
	}
	public float getPrice() {
		return price;
	}
	public float getPricePerUnit() {
		return pricePerUnit;
	}
	public int getProjectId() {
		return projectId;
	}
	public float getReferWorkTime() {
		return referWorkTime;
	}
	@ManyToMany(cascade={}, fetch=FetchType.EAGER)
	@JoinTable(name="projectinfo_staff", 
				joinColumns={@JoinColumn(name="projectInfoId", foreignKey=@ForeignKey(name="none"))}, 
				inverseJoinColumns={@JoinColumn(name="staffId", foreignKey=@ForeignKey(name="none"))})
	public Set<Staff> getStaffs() {
		return staffs;
	}
	public void setCardId(String cardId) {
		this.cardId = cardId;
	}
	public void setCardName(String cardName) {
		this.cardName = cardName;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setPayCardTimes(int payCardTimes) {
		this.payCardTimes = payCardTimes;
	}
	public void setPayMethod(int payMethod) {
		this.payMethod = payMethod;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public void setPricePerUnit(float pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
	}
	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
	public void setReferWorkTime(float referWorkTime) {
		this.referWorkTime = referWorkTime;
	}
	public void setStaffs(Set<Staff> staffs) {
		this.staffs = staffs;
	}
	public String getTicketId() {
		return ticketId;
	}
	public void setTicketId(String ticketId) {
		this.ticketId = ticketId;
	}
	public String getFavourName() {
		return favourName;
	}
	public void setFavourName(String favourName) {
		this.favourName = favourName;
	}
	@Override
	public String toString() {
		return "ProjectInfo [id=" + id + ", projectId=" + projectId + ", name=" + name + ", price=" + price
				+ ", payMethod=" + payMethod + ", cardId=" + cardId + ", cardName=" + cardName + ", ticketId="
				+ ticketId + ", favourName=" + favourName + ", payCardTimes=" + payCardTimes
				+ ", referWorkTime=" + referWorkTime + ", pricePerUnit=" + pricePerUnit + ", staffs=" + staffs
				+ ", clientName=" + clientName + ", licensePlate=" + licensePlate + ", brandName=" + brandName
				+ ", createDate=" + createDate + "]";
	}
	public float getPresentPrice() {
		return presentPrice;
	}
	public void setPresentPrice(float presentPrice) {
		this.presentPrice = presentPrice;
	}
	public int getClientId() {
		return clientId;
	}
	public void setClientId(int clientId) {
		this.clientId = clientId;
	}
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	
}
