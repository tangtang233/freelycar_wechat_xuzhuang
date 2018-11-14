package com.geariot.platform.freelycar_wechat.entities;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceProjectInfo {
	private int id;
	private Project project;
	private int times;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	@ManyToOne
	@JoinColumn(name="projectId", foreignKey=@ForeignKey(name="none"))
	public Project getProject() {
		return project;
	}
	public int getTimes() {
		return times;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setProject(Project project) {
		this.project = project;
	}
	public void setTimes(int times) {
		this.times = times;
	}
}
