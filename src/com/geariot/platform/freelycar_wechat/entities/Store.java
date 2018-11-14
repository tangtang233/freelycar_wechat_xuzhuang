/**
 * 
 */
package com.geariot.platform.freelycar_wechat.entities;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author mxy940127
 *
 */

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Store {
	private int id;							//门店Id
	private String name;					//门店名称
	private String address;					//门店地址
	private double latitude;				//门店纬度
	private double longitude;				//门店经度
	private String openingTime;				//门店营业时间
	private String closingTime;				//门店歇业事件
	private String phone;					//门店联系方式
	private Set<StoreProject> storeProjects;			//门店经营项目
	private Set<StoreFavour> storefavours;			//门店优惠活动
	private Set<ImgUrl> imgUrls;			//门店图片
	private Date createDate;				//门店创建时间
	
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
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	@OneToMany(cascade={CascadeType.ALL}, fetch=FetchType.EAGER)
	@JoinColumn(name="storeId", foreignKey=@ForeignKey(name="none"))
	public Set<StoreProject> getStoreProjects() {
		return storeProjects;
	}
	public void setStoreProjects(Set<StoreProject> storeProjects) {
		this.storeProjects = storeProjects;
	}
	@OneToMany(cascade={CascadeType.ALL}, fetch=FetchType.EAGER)
	@JoinColumn(name="storeId", foreignKey=@ForeignKey(name="none"))
	public Set<StoreFavour> getStorefavours() {
		return storefavours;
	}
	public void setStorefavours(Set<StoreFavour> storefavours) {
		this.storefavours = storefavours;
	}
	@OneToMany(cascade={CascadeType.ALL}, fetch=FetchType.EAGER)
	@JoinColumn(name="storeId", foreignKey=@ForeignKey(name="none"))
	public Set<ImgUrl> getImgUrls() {
		return imgUrls;
	}
	public void setImgUrls(Set<ImgUrl> imgUrls) {
		this.imgUrls = imgUrls;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getOpeningTime() {
		return openingTime;
	}
	public void setOpeningTime(String openingTime) {
		this.openingTime = openingTime;
	}
	public String getClosingTime() {
		return closingTime;
	}
	public void setClosingTime(String closingTime) {
		this.closingTime = closingTime;
	}
	@Override
	public String toString() {
		return "Store [id=" + id + ", name=" + name + ", address=" + address + ", latitude=" + latitude + ", longitude="
				+ longitude + ", openingTime=" + openingTime + ", closingTime=" + closingTime + ", phone=" + phone
				+ ", storeProjects=" + storeProjects + ", storefavours=" + storefavours + ", imgUrls=" + imgUrls
				+ ", createDate=" + createDate + "]";
	}
	
	
}
