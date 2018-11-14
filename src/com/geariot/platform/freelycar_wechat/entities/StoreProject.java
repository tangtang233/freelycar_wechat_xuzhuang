/**
 * 
 */
package com.geariot.platform.freelycar_wechat.entities;

import javax.persistence.Entity;
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
public class StoreProject {
	private int id;
	private Project project;
	@ManyToOne
	@JoinColumn(name="projectId", foreignKey=@ForeignKey(name="none"))
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
	}
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
}
