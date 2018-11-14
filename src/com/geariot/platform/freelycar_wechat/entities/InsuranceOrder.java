/**
 * 
 */
package com.geariot.platform.freelycar_wechat.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.geariot.platform.freelycar_wechat.utils.JsonDateDeserialize;

/**
 * @author mxy940127
 *
 */

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class InsuranceOrder {
	private int id;
	private String name;				//客户姓名
	private String licensePlate;		//保险询价 车牌
	private String phone;				//客户联系方式
	private String insuranceCompany;	//客户意向 保险公司
	@JsonDeserialize(using=JsonDateDeserialize.class)
	private Date createDate;			//询价创建日期
	private String intent;				//客户意向
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLicensePlate() {
		return licensePlate;
	}
	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getInsuranceCompany() {
		return insuranceCompany;
	}
	public void setInsuranceCompany(String insuranceCompany) {
		this.insuranceCompany = insuranceCompany;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getIntent() {
		return intent;
	}
	public void setIntent(String intent) {
		this.intent = intent;
	}
	@Override
	public String toString() {
		return "insuranceOrder [id=" + id + ", name=" + name + ", licensePlate=" + licensePlate + ", phone=" + phone
				+ ", insuranceCompany=" + insuranceCompany + ", createDate=" + createDate + ", intent=" + intent + "]";
	}
	
}
