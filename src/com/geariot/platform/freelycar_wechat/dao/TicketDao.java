package com.geariot.platform.freelycar_wechat.dao;

import java.util.List;

import com.geariot.platform.freelycar_wechat.entities.Ticket;
public interface TicketDao {
	public List<Ticket> favourtByClientId(int clientId);
	public Object getCountByClientId(int clientId);
}
