/**
 * 
 */
package com.geariot.platform.freelycar_wechat.entities;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author mxy940127
 *
 */

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class FavourProjectInfos {
	private int id;
	private Project project;
	private int times;
	private float buyPrice;
	private float presentPrice;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@ManyToOne(cascade={}, fetch=FetchType.EAGER)
	@JoinColumn(name="projectId", foreignKey=@ForeignKey(name="none"))
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
	}
	public int getTimes() {
		return times;
	}
	public void setTimes(int times) {
		this.times = times;
	}
	public float getBuyPrice() {
		return buyPrice;
	}
	public void setBuyPrice(float buyPrice) {
		this.buyPrice = buyPrice;
	}
	public float getPresentPrice() {
		return presentPrice;
	}
	public void setPresentPrice(float presentPrice) {
		this.presentPrice = presentPrice;
	}
	@Override
	public String toString() {
		return "FavourProjectInfos [id=" + id + ", project=" + project + ", times=" + times + ", buyPrice=" + buyPrice
				+ ", presentPrice=" + presentPrice + "]";
	}
		
}
