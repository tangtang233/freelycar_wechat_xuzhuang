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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.geariot.platform.freelycar_wechat.utils.JsonDateDeserialize;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Car {
	private int id;
	
	private Client client;
	//private CarType type;
	private String carMark;
	private String carbrand;
	private String cartype;
	private String licensePlate;
	private String driveLicenseNumber;
	private Date insuranceStarttime;
	private Date insuranceEndtime;
	private float insuranceAmount;
	private String insuranceCompany;
	private String insuranceCity;
	private String frameNumber;
	private String engineNumber;
	private Date licenseDate;
	private boolean newCar;
	private int lastMiles;
	private int miles;
	@JsonDeserialize(using=JsonDateDeserialize.class)
	private Date createDate;
	private boolean defaultCar;
	
	private boolean needInsuranceRemind;
	private boolean needInspectionRemind;
	
	@ManyToOne(cascade={}, fetch=FetchType.EAGER)
	@JoinColumn(name="clientId", foreignKey=@ForeignKey(name="none"))
	public Client getClient() {
		return client;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public String getDriveLicenseNumber() {
		return driveLicenseNumber;
	}
	public String getEngineNumber() {
		return engineNumber;
	}
	public String getFrameNumber() {
		return frameNumber;
	}
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public float getInsuranceAmount() {
		return insuranceAmount;
	}
	public Date getInsuranceEndtime() {
		return insuranceEndtime;
	}
	public Date getInsuranceStarttime() {
		return insuranceStarttime;
	}
	public int getLastMiles() {
		return lastMiles;
	}
	public Date getLicenseDate() {
		return licenseDate;
	}
	public String getLicensePlate() {
		return licensePlate;
	}
	public int getMiles() {
		return miles;
	}
	/*@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="carTypeId", foreignKey=@ForeignKey(name="none"))
	public CarType getType() {
		return type;
	}*/
	public boolean isNewCar() {
		return newCar;
	}
	@JsonBackReference
	public void setClient(Client client) {
		this.client = client;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public void setDriveLicenseNumber(String driveLicenseNumber) {
		this.driveLicenseNumber = driveLicenseNumber;
	}
	public void setEngineNumber(String engineNumber) {
		this.engineNumber = engineNumber;
	}
	public void setFrameNumber(String frameNumber) {
		this.frameNumber = frameNumber;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setInsuranceAmount(float insuranceAmount) {
		this.insuranceAmount = insuranceAmount;
	}
	public void setInsuranceEndtime(Date insuranceEndtime) {
		this.insuranceEndtime = insuranceEndtime;
	}
	public void setInsuranceStarttime(Date insuranceStarttime) {
		this.insuranceStarttime = insuranceStarttime;
	}
	public void setLastMiles(int lastMiles) {
		this.lastMiles = lastMiles;
	}
	public void setLicenseDate(Date licenseDate) {
		this.licenseDate = licenseDate;
	}
	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}
	public void setMiles(int miles) {
		this.miles = miles;
	}
	public void setNewCar(boolean newCar) {
		this.newCar = newCar;
	}
	/*public void setType(CarType type) {
		this.type = type;
	}*/
	
    public String getCartype() {
		return cartype;
	}
	public void setCartype(String cartype) {
		this.cartype = cartype;
	}
	
	public String getCarbrand() {
		return carbrand;
	}
	public void setCarbrand(String carbrand) {
		this.carbrand = carbrand;
	}
	@Override
    public String toString() {
        return "Car [id=" + id + ", client=" + client + ", insuranceStarttime="
                + insuranceStarttime + ", insuranceEndtime=" + insuranceEndtime
                + "]";
    }
	public boolean isDefaultCar() {
		return defaultCar;
	}
	public void setDefaultCar(boolean defaultCar) {
		this.defaultCar = defaultCar;
	}
	public String getInsuranceCity() {
		return insuranceCity;
	}
	public void setInsuranceCity(String insuranceCity) {
		this.insuranceCity = insuranceCity;
	}
	public String getInsuranceCompany() {
		return insuranceCompany;
	}
	public void setInsuranceCompany(String insuranceCompany) {
		this.insuranceCompany = insuranceCompany;
	}
	public boolean isNeedInspectionRemind() {
		return needInspectionRemind;
	}
	public void setNeedInspectionRemind(boolean needInspectionRemind) {
		this.needInspectionRemind = needInspectionRemind;
	}
	public boolean isNeedInsuranceRemind() {
		return needInsuranceRemind;
	}
	public void setNeedInsuranceRemind(boolean needInsuranceRemind) {
		this.needInsuranceRemind = needInsuranceRemind;
	}
	public String getCarMark() {
		return carMark;
	}
	public void setCarMark(String carMark) {
		this.carMark = carMark;
	}
    
	
	
}
