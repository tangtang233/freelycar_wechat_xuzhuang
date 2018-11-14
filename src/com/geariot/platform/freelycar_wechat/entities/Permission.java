package com.geariot.platform.freelycar_wechat.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Permission {

	private int id;
	private String permission;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public String getPermission() {
		return permission;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setPermission(String permission) {
		this.permission = permission;
	}
	
	@Override
	public String toString() {
		return permission;
	}
	
}
