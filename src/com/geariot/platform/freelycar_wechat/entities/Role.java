package com.geariot.platform.freelycar_wechat.entities;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Role {
	
	private int id;
	private String roleName;
	private String description;
	Set<Permission> permissions;
	public String getDescription() {
		return description;
	}
	@Id
	public int getId() {
		return id;
	}
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER, orphanRemoval=true)
	@JoinColumn(name="roleId", foreignKey=@ForeignKey(name="none"))
	public Set<Permission> getPermissions() {
		return permissions;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
}
