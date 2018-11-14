/**
 * 
 */
package com.geariot.platform.freelycar_wechat.daoimpl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.geariot.platform.freelycar_wechat.dao.StoreDao;
import com.geariot.platform.freelycar_wechat.entities.Store;
import com.geariot.platform.freelycar_wechat.model.ORDER_CON;
import com.geariot.platform.freelycar_wechat.utils.Constants;
import com.geariot.platform.freelycar_wechat.utils.query.QueryUtils;

/**
 * @author mxy940127
 *
 */

@Repository
public class StoreDaoImpl implements StoreDao{
	
	@Autowired
	private SessionFactory sessionFactory;
	
	private Session getSession(){
		return this.sessionFactory.getCurrentSession();
	}
	
	@Override
	public void save(Store store) {
		this.getSession().saveOrUpdate(store);
	}

	@Override
	public long getCount() {
		String hql = "select count(*) from Store";
		return (long) this.getSession().createQuery(hql).setCacheable(Constants.SELECT_CACHE).uniqueResult();
	}

	@Override
	public void delete(int storeId) {
		String hql = "delete from Store where id in :id";
		this.getSession().createQuery(hql).setInteger("id", storeId).executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Store> listStore(int from, int pageSize) {
		String hql = "from Store";
		return this.getSession().createQuery(hql).setFirstResult(from).setMaxResults(pageSize).setCacheable(Constants.SELECT_CACHE).list();
	}

	@Override
	public Store findStoreById(int storeId) {
		String hql = "from Store where id = :id";
		return (Store) getSession().createQuery(hql).setInteger("id", storeId).setCacheable(Constants.SELECT_CACHE).uniqueResult();
	}

}
