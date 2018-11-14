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
public class ProjectInventoriesInfo {
	private int id;
	private Inventory inventory;
	private float number;
	@ManyToOne(cascade={}, fetch=FetchType.EAGER)
	@JoinColumn(name="inventoryId", foreignKey=@ForeignKey(name="none"))
	public Inventory getInventory() {
		return inventory;
	}
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public float getNumber() {
		return number;
	}
	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setNumber(float number) {
		this.number = number;
	}
}
