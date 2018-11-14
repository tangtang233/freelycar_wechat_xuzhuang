package com.geariot.platform.freelycar_wechat.entities;

import java.util.Date;
import java.util.List;
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
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.geariot.platform.freelycar_wechat.utils.JsonDateDeserialize;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Service {
	private int id;
	private String name;
	private int type; //0,1,2=次卡,组合卡,储值卡
	private float price;
	private int discount;
	private float actualPrice;//卡面金额
	private int validTime;
	private String comment;
	private List<ServiceProjectInfo> projectInfos;
	@JsonDeserialize(using=JsonDateDeserialize.class)
	private Date createDate;
	private Set<FavourInfos> favourInfos;		
	private boolean deleted;		//删除Service只标记这条记录为true，不在数据库中实际删掉数据。
	private boolean bookOnline;
	
	public String getComment() {
		return comment;
	}
	public Date getCreateDate() {
		return createDate;
	}
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public float getPrice() {
		return price;
	}
	@OneToMany(cascade={CascadeType.ALL}, fetch=FetchType.EAGER)
	@JoinColumn(name="serviceId", foreignKey=@ForeignKey(name="none"))
	public List<ServiceProjectInfo> getProjectInfos() {
		return projectInfos;
	}
	public int getType() {
		return type;
	}
	public int getValidTime() {
		return validTime;
	}
	public boolean isDeleted() {
		return deleted;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public void setProjectInfos(List<ServiceProjectInfo> projectInfos) {
		this.projectInfos = projectInfos;
	}
	public void setType(int type) {
		this.type = type;
	}
	public void setValidTime(int validTime) {
		this.validTime = validTime;
	}
	@OneToMany(cascade={CascadeType.ALL}, fetch=FetchType.EAGER)
	@JoinColumn(name="serviceId", foreignKey=@ForeignKey(name="none"))
	public Set<FavourInfos> getFavourInfos() {
		return favourInfos;
	}
	public void setFavourInfos(Set<FavourInfos> favourInfos) {
		this.favourInfos = favourInfos;
	}
	public int getDiscount() {
		return discount;
	}
	public void setDiscount(int discount) {
		this.discount = discount;
	}
	public float getActualPrice() {
		return actualPrice;
	}
	public void setActualPrice(float actualPrice) {
		this.actualPrice = actualPrice;
	}
	public boolean isBookOnline() {
		return bookOnline;
	}
	public void setBookOnline(boolean bookOnline) {
		this.bookOnline = bookOnline;
	}
	
	
	
}
