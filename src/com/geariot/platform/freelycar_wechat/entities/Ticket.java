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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.geariot.platform.freelycar_wechat.utils.JsonDateDeserialize;

/**
 * @author mxy940127
 *
 */
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Ticket {
	private int id;
	private Favour favour;
	private Set<FavourProjectRemainingInfo> remainingInfos;
	@JsonDeserialize(using=JsonDateDeserialize.class)
	private Date expirationDate;
	private boolean failed;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@ManyToOne
	@JoinColumn(name="favourId", foreignKey=@ForeignKey(name="none"))
	public Favour getFavour() {
		return favour;
	}
	public void setFavour(Favour favour) {
		this.favour = favour;
	}
	@OneToMany(cascade={CascadeType.ALL}, fetch=FetchType.EAGER)
	@JoinColumn(name="ticketId", foreignKey=@ForeignKey(name="none"))
	public Set<FavourProjectRemainingInfo> getRemainingInfos() {
		return remainingInfos;
	}
	public void setRemainingInfos(Set<FavourProjectRemainingInfo> remainingInfos) {
		this.remainingInfos = remainingInfos;
	}
	public Date getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}
	public boolean isFailed() {
		return failed;
	}
	public void setFailed(boolean failed) {
		this.failed = failed;
	}
	@Override
	public String toString() {
		return "Ticket [id=" + id + ", favour=" + favour + ", remainingInfos=" + remainingInfos + ", expirationDate="
				+ expirationDate + ", failed=" + failed + "]";
	}
	
	
}
