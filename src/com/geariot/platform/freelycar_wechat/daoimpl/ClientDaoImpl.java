package com.geariot.platform.freelycar_wechat.daoimpl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.geariot.platform.freelycar_wechat.dao.ClientDao;
import com.geariot.platform.freelycar_wechat.entities.Client;
import com.geariot.platform.freelycar_wechat.model.ORDER_CON;
import com.geariot.platform.freelycar_wechat.utils.Constants;
import com.geariot.platform.freelycar_wechat.utils.query.QueryUtils;

@Repository
public class ClientDaoImpl implements ClientDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	private Session getSession(){
		return this.sessionFactory.getCurrentSession();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Client> list(int from, int pageSize) {
		String hql = "from Client";
		return this.getSession().createQuery(hql).setFirstResult(from).setMaxResults(pageSize)
				.setCacheable(Constants.SELECT_CACHE).list();
	}

	@Override
	public long getCount() {
		String hql = "select count(*) from Client";
		return (long) this.getSession().createQuery(hql).setCacheable(Constants.SELECT_CACHE).uniqueResult();
	}

	@Override
	public Client findByPhone(String phone) {
		String hql = "from Client where phone = :phone";
		return (Client) this.getSession().createQuery(hql).setString("phone", phone).setCacheable(Constants.SELECT_CACHE).uniqueResult();
	}
	
	@Override
	public Client findById(int clientId) {
		String hql = "from Client where id = :id";
		return (Client) this.getSession().createQuery(hql).setInteger("id", clientId).setCacheable(Constants.SELECT_CACHE).uniqueResult();
	}

	@Override
	public void save(Client client) {
		this.getSession().saveOrUpdate(client);
	}

	@Override
	public void delete(List<Integer> clientIds) {
		String hql = "delete from Client where id in :ids";
		this.getSession().createQuery(hql).setParameterList("ids", clientIds).executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Client> query(String condition, int from, int pageSize) {
		StringBuffer basic = new StringBuffer("from Client");
		String hql = QueryUtils.createQueryString(basic, condition, ORDER_CON.NO_ORDER).toString();
		return this.getSession().createQuery(hql).setFirstResult(from).setMaxResults(pageSize)
				.setCacheable(Constants.SELECT_CACHE).list();
	}

	@Override
	public long getQueryCount(String andCondition) {
		StringBuffer basic = new StringBuffer("select count(*) from Client where isMember = true");
		String hql = QueryUtils.createQueryString(basic, andCondition, ORDER_CON.NO_ORDER).toString();
		return (long) this.getSession().createQuery(hql).setCacheable(Constants.SELECT_CACHE).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getClientNames(String name) {
		String hql = "select name from Client where name like :name";
		return this.getSession().createQuery(hql).setString("name", "%"+name+"%")
				.setCacheable(Constants.SELECT_CACHE).list();
	}

}
