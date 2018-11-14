package com.geariot.platform.freelycar_wechat.entities;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConsumOrder {
	private String id;
	private int carId;
	private String licensePlate;
	private String carType;
	private String carBrand;
	private int clientId;
	private String clientName;
	private String gender;
	private String phone;
	private Set<ProjectInfo> projects;
	private int programId;
	private String payMethod;   // 0,1,2,3,4,5  现金,刷卡,支付宝,微信,易付宝,储值卡形式(5$12345$)
	private String payMethod1;	//	0,1,2,3,4,5  现金,刷卡,支付宝,微信,易付宝,储值卡形式(5$12345$)
	private String programName;
	private String parkingLocation;
	private Set<ConsumExtraInventoriesInfo> inventoryInfos;
	private int state;		//0,1,2=接,完,交
	private float totalPrice;//实际支付金额
	private float presentPrice;//展示原价
	private int payState;	//0,1=未结算,已结算
	private Date pickTime;
	private Date finishTime;
	private Date deliverTime;
	private Date createDate;
	private int lastMiles;
	private int miles;
	private Admin orderMaker;
	private String comment;			//客户消费开单服务评价
	private Date commentDate;
	private int stars;			//客户消费开单评分
	private String faultDesc;
	private String repairAdvice;
	private Staff pickCarStaff;
	private Store store;			//门店Id
	private String headimgurl;	    //头像地址
	private float actualPrice;			// 订单实付金额
	private float actualPrice1;			// 订单支付方式1 实付金额

	private Integer reservationId;

	public Integer getReservationId() {
		return reservationId;
	}

	public void setReservationId(Integer reservationId) {
		this.reservationId = reservationId;
	}

	public float getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(float totalPrice) {
		this.totalPrice = totalPrice;
	}
	public float getPresentPrice() {
		return presentPrice;
	}
	public void setPresentPrice(float presentPrice) {
		this.presentPrice = presentPrice;
	}
	public String getCarBrand() {
		return carBrand;
	}
	public int getCarId() {
		return carId;
	}
	public String getCarType() {
		return carType;
	}
	public int getClientId() {
		return clientId;
	}
	public String getClientName() {
		return clientName;
	}
	public String getComment() {
		return comment;
	}
	
	public Date getCreateDate() {
		return createDate;
	}
	public Date getDeliverTime() {
		return deliverTime;
	}
	public String getFaultDesc() {
		return faultDesc;
	}
	public Date getFinishTime() {
		return finishTime;
	}
	public String getGender() {
		return gender;
	}
	@Id
	public String getId() {
		return id;
	}
	@OneToMany(cascade={CascadeType.ALL}, fetch=FetchType.EAGER)
	@JoinColumn(name="consumeOrdersId", foreignKey=@ForeignKey(name="none"))
	public Set<ConsumExtraInventoriesInfo> getInventoryInfos() {
		return inventoryInfos;
	}
	public int getLastMiles() {
		return lastMiles;
	}
	public String getLicensePlate() {
		return licensePlate;
	}
	public int getMiles() {
		return miles;
	}
	@ManyToOne(cascade={}, fetch=FetchType.EAGER)
	@JoinColumn(name="adminId", foreignKey=@ForeignKey(name="none"))
	public Admin getOrderMaker() {
		return orderMaker;
	}
	public String getParkingLocation() {
		return parkingLocation;
	}
	public String getPayMethod() {
		return payMethod;
	}
	public int getPayState() {
		return payState;
	}
	public String getPhone() {
		return phone;
	}
	@ManyToOne(cascade={}, fetch=FetchType.EAGER)
	@JoinColumn(name="pickCarStaffId", foreignKey=@ForeignKey(name="none"))
	public Staff getPickCarStaff() {
		return pickCarStaff;
	}
	
	public Date getPickTime() {
		return pickTime;
	}
	public int getProgramId() {
		return programId;
	}
	public String getProgramName() {
		return programName;
	}
	@OneToMany(cascade={CascadeType.ALL}, fetch=FetchType.EAGER)
	@JoinColumn(name="consumOrderId", foreignKey=@ForeignKey(name="none"))
	public Set<ProjectInfo> getProjects() {
		return projects;
	}
	public String getRepairAdvice() {
		return repairAdvice;
	}
	public int getState() {
		return state;
	}
	public void setCarBrand(String carBrand) {
		this.carBrand = carBrand;
	}
	public void setCarId(int carId) {
		this.carId = carId;
	}
	public void setCarType(String carType) {
		this.carType = carType;
	}
	public void setClientId(int clientId) {
		this.clientId = clientId;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public void setDeliverTime(Date deliverTime) {
		this.deliverTime = deliverTime;
	}
	public void setFaultDesc(String faultDesc) {
		this.faultDesc = faultDesc;
	}
	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setInventoryInfos(Set<ConsumExtraInventoriesInfo> inventoryInfos) {
		this.inventoryInfos = inventoryInfos;
	}
	public void setLastMiles(int lastMiles) {
		this.lastMiles = lastMiles;
	}
	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}
	public void setMiles(int miles) {
		this.miles = miles;
	}
	public void setOrderMaker(Admin orderMaker) {
		this.orderMaker = orderMaker;
	}
	public void setParkingLocation(String parkingLocation) {
		this.parkingLocation = parkingLocation;
	}
	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}
	public void setPayState(int payState) {
		this.payState = payState;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public void setPickCarStaff(Staff pickCarStaff) {
		this.pickCarStaff = pickCarStaff;
	}
	
	public void setPickTime(Date pickTime) {
		this.pickTime = pickTime;
	}
	public void setProgramId(int programId) {
		this.programId = programId;
	}
	public void setProgramName(String programName) {
		this.programName = programName;
	}
	public void setProjects(Set<ProjectInfo> projects) {
		this.projects = projects;
	}
	public void setRepairAdvice(String repairAdvice) {
		this.repairAdvice = repairAdvice;
	}
	public void setState(int state) {
		this.state = state;
	}
	
	public int getStars() {
		return stars;
	}
	public void setStars(int stars) {
		this.stars = stars;
	}
	@ManyToOne(cascade={}, fetch=FetchType.EAGER)
	@JoinColumn(name="storeId", foreignKey=@ForeignKey(name="none"))
	public Store getStore() {
		return store;
	}
	public void setStore(Store store) {
		this.store = store;
	}
	public Date getCommentDate() {
		return commentDate;
	}
	public void setCommentDate(Date commentDate) {
		this.commentDate = commentDate;
	}
	public String getHeadimgurl() {
		return headimgurl;
	}
	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}
	public float getActualPrice() {
		return actualPrice;
	}
	public void setActualPrice(float actualPrice) {
		this.actualPrice = actualPrice;
	}
	public String getPayMethod1() {
		return payMethod1;
	}
	public void setPayMethod1(String payMethod1) {
		this.payMethod1 = payMethod1;
	}
	public float getActualPrice1() {
		return actualPrice1;
	}
	public void setActualPrice1(float actualPrice1) {
		this.actualPrice1 = actualPrice1;
	}
	@Override
	public String toString() {
		return "ConsumOrder [id=" + id + ", carId=" + carId + ", licensePlate=" + licensePlate + ", carType=" + carType
				+ ", carBrand=" + carBrand + ", clientId=" + clientId + ", clientName=" + clientName + ", gender="
				+ gender + ", phone=" + phone + ", projects=" + projects + ", programId=" + programId + ", payMethod="
				+ payMethod + ", payMethod1=" + payMethod1 + ", programName=" + programName + ", parkingLocation="
				+ parkingLocation + ", inventoryInfos=" + inventoryInfos + ", state=" + state + ", totalPrice="
				+ totalPrice + ", presentPrice=" + presentPrice + ", payState=" + payState + ", pickTime=" + pickTime
				+ ", finishTime=" + finishTime + ", deliverTime=" + deliverTime + ", createDate=" + createDate
				+ ", lastMiles=" + lastMiles + ", miles=" + miles + ", orderMaker=" + orderMaker + ", comment="
				+ comment + ", commentDate=" + commentDate + ", stars=" + stars + ", faultDesc=" + faultDesc
				+ ", repairAdvice=" + repairAdvice + ", pickCarStaff=" + pickCarStaff + ", store=" + store
				+ ", headimgurl=" + headimgurl + ", actualPrice=" + actualPrice + ", actualPrice1=" + actualPrice1
				+ "]";
	}
	
	
}
