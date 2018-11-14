package com.geariot.platform.freelycar_wechat.daoimpl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.geariot.platform.freelycar_wechat.dao.PointDao;
import com.geariot.platform.freelycar_wechat.utils.Constants;
@Repository
public class PointDaoImpl implements PointDao{
	@Autowired
	private SessionFactory sessionFactory;
	
	private Session getSession(){
		return this.sessionFactory.getCurrentSession();
	}
	//临时使用sql返回积分相关信息
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getPoint(int clientId) {
		String sql="SELECT c1.commentDate,SUM(price) FROM projectinfo,consumorder AS c1 WHERE consumOrderId in (SELECT id FROM consumorder as c2 WHERE c1.id=c2.id and clientId =:clientId and commentDate<>\"\") GROUP BY consumOrderId ORDER BY commentDate";
		return this.getSession().createSQLQuery(sql).setInteger("clientId", clientId).setCacheable(Constants.SELECT_CACHE).list();
	}
	@Override
	public Object getSumPoint(int clientId) {
		String sql = "SELECT SUM(price) FROM projectinfo WHERE consumOrderId in (SELECT id FROM consumorder  WHERE clientId =:clientId and commentDate<>\"\")";
		return this.getSession().createSQLQuery(sql).setInteger("clientId", clientId).uniqueResult();
	}
}
