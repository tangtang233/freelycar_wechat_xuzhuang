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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author mxy940127
 *
 */

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Favour {
	private int id;					
	private String name;			//优惠券名称
	private int type;				//优惠券类型 1=抵用券 2=代金券
	private int validTime;			//券类有效时间
	private String content;			//内容说明
	private Date buyStartline;		//限时购买开始日期 
	private Date buyDeadline;		//限时购买截止日期    
	private Set<FavourProjectInfos> set;		//优惠项目
	private boolean deleted;		//是否删除
	//@JsonDeserialize(using=JsonDateDeserialize.class)
	private Date createDate;
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
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getValidTime() {
		return validTime;
	}
	public void setValidTime(int validTime) {
		this.validTime = validTime;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getBuyDeadline() {
		return buyDeadline;
	}
	public void setBuyDeadline(Date buyDeadline) {
		this.buyDeadline = buyDeadline;
	}
	@OneToMany(cascade={CascadeType.ALL}, fetch=FetchType.EAGER)
	@JoinColumn(name="favourId", foreignKey=@ForeignKey(name="none"))
	public Set<FavourProjectInfos> getSet() {
		return set;
	}
	public void setSet(Set<FavourProjectInfos> set) {
		this.set = set;
	}
	public boolean getDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	@Override
	public String toString() {
		return "Favour [id=" + id + ", name=" + name + ", type=" + type + ", validTime=" + validTime + ", content="
				+ content + ", buyDeadline=" + buyDeadline + ", set=" + set + ", deleted=" + deleted + ", createDate="
				+ createDate + "]";
	}
	public Date getBuyStartline() {
		return buyStartline;
	}
	public void setBuyStartline(Date buyStartline) {
		this.buyStartline = buyStartline;
	}
	
}
