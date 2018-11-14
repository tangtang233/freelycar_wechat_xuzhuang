package com.geariot.platform.freelycar_wechat.daoimpl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.geariot.platform.freelycar_wechat.dao.TicketDao;
import com.geariot.platform.freelycar_wechat.entities.Ticket;
import com.geariot.platform.freelycar_wechat.utils.Constants;

import java.math.BigDecimal;
import java.util.List;
@Repository
public class TicketDaoImpl implements TicketDao{
	@Autowired
	private SessionFactory sessionFactory;
	
	private Session getSession(){
		return this.sessionFactory.getCurrentSession();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Ticket> favourtByClientId(int clientId) {
		String hql = "from Ticket where clientId = :clientId";
		return this.getSession().createQuery(hql).setCacheable(Constants.SELECT_CACHE).setInteger("clientId",clientId).list();
	}

	@Override
	public Object getCountByClientId(int clientId) {
		String sql = "SELECT SUM(remaining) FROM `Ticket` where clientId= :clientId";
		return  this.getSession().createSQLQuery(sql).setInteger("clientId", clientId).uniqueResult();
	}

}
