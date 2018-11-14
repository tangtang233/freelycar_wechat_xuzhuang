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
public class StoreFavour {
	private int id;
	private Favour favour;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@ManyToOne
	@JoinColumn(name="favourId",foreignKey=@ForeignKey(name="none"))
	public Favour getFavour() {
		return favour;
	}
	public void setFavour(Favour favour) {
		this.favour = favour;
	}
	
}
