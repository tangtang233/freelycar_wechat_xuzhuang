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

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConsumExtraInventoriesInfo {
	private int id;
	private Inventory inventory;
	private float number;
	private int projectId;		//配件所属项目，必须与ConsumOrder中Projects中某一个对应。
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	@ManyToOne(cascade={}, fetch=FetchType.EAGER)
	@JoinColumn(name="inventoryId", foreignKey=@ForeignKey(name="none"))
	public Inventory getInventory() {
		return inventory;
	}
	public float getNumber() {
		return number;
	}
	public int getProjectId() {
		return projectId;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}
	public void setNumber(float number) {
		this.number = number;
	}
	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
	
	@Override
	public String toString() {
		return "ConsumExtraInventoriesInfo [id=" + id + ", inventory=" + inventory + ", number=" + number
				+ ", projectId=" + projectId + "]";
	}
	
}
