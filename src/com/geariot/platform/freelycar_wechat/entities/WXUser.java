package com.geariot.platform.freelycar_wechat.entities;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.geariot.platform.freelycar_wechat.utils.JsonDateDeserialize;
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class WXUser {
	private int id;            		//数据库记录id
	private String openId;	   		//微信用户openId
	private String nickName;		//微信用户昵称
	private String headimgurl;		//微信用户头像地址
	private String phone;			//手机号
	@JsonDeserialize(using=JsonDateDeserialize.class)
	private Date birthday;			//微信用户生日
	private String name;			//真实姓名
	private String gender;			//用户性别
	/**
	 * 是否是技师
	 */
	private boolean isTechnician;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getHeadimgurl() {
		return headimgurl;
	}
	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}

	public boolean isTechnician() {
		return isTechnician;
	}

	public void setTechnician(boolean technician) {
		isTechnician = technician;
	}

	@Override
	public String toString() {
		return "WXUser{" +
				"id=" + id +
				", openId='" + openId + '\'' +
				", nickName='" + nickName + '\'' +
				", headimgurl='" + headimgurl + '\'' +
				", phone='" + phone + '\'' +
				", birthday=" + birthday +
				", name='" + name + '\'' +
				", gender='" + gender + '\'' +
				", isTechnician=" + isTechnician +
				'}';
	}
}