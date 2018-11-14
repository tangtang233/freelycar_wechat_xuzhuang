/**
 * 
 */
package com.geariot.platform.freelycar_wechat.daoimpl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.geariot.platform.freelycar_wechat.dao.InsuranceOrderDao;
import com.geariot.platform.freelycar_wechat.entities.InsuranceOrder;
import com.geariot.platform.freelycar_wechat.utils.Constants;


/**
 * @author mxy940127
 *
 */

@Repository
public class InsuranceOrderDaoImpl implements InsuranceOrderDao{

	@Autowired
	private SessionFactory sessionFactory;
	
	private Session getSession(){
		return this.sessionFactory.getCurrentSession();
	}

	@Override
	public void save(InsuranceOrder insuranceOrder) {
		this.getSession().saveOrUpdate(insuranceOrder);
	}

	@Override
	public InsuranceOrder findByLicensePlate(String licensePlate) {
		String hql = "from InsuranceOrder where licensePlate = :licensePlate";
		return (InsuranceOrder) this.getSession().createQuery(hql).setString("licensePlate", licensePlate).setCacheable(Constants.SELECT_CACHE).uniqueResult();
	}

	@Override
	public void update(InsuranceOrder insuranceOrder) {
		this.getSession().update(insuranceOrder);
	}
	

}
