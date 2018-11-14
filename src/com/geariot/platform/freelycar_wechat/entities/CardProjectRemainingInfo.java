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
public class CardProjectRemainingInfo {
	private int id;
	private Project project;
	private int remaining;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	@ManyToOne(cascade={}, fetch=FetchType.EAGER)
	@JoinColumn(name="projectId", foreignKey=@ForeignKey(name="none"))
	public Project getProject() {
		return project;
	}
	public int getRemaining() {
		return remaining;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setProject(Project project) {
		this.project = project;
	}
	public void setRemaining(int remaining) {
		this.remaining = remaining;
	}
	@Override
	public String toString() {
		return "CardProjectRemainingInfo [id=" + id + ", project=" + project + ", remaining=" + remaining + "]";
	}
	
	
}
