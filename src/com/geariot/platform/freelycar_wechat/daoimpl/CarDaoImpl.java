package com.geariot.platform.freelycar_wechat.daoimpl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.geariot.platform.freelycar_wechat.dao.CarDao;
import com.geariot.platform.freelycar_wechat.entities.Car;
import com.geariot.platform.freelycar_wechat.utils.Constants;

@Repository
public class CarDaoImpl implements CarDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	private Session getSession(){
		return this.sessionFactory.getCurrentSession();
	}
	
	@Override
	public void deleteById(int carId) {
		String hql = "delete from Car where id = :id";
		this.getSession().createQuery(hql).setInteger("id", carId).executeUpdate();
	}

	@Override
	public Car findById(int carId) {
		String hql = "from Car where id = :id";
		return (Car) this.getSession().createQuery(hql).setInteger("id", carId)
				.setCacheable(Constants.SELECT_CACHE).uniqueResult();
	}

	@Override
	public Car findByLicense(String licensePlate) {
		String hql = "from Car where licensePlate = :license";
		return (Car) this.getSession().createQuery(hql).setString("license", licensePlate)
				.setCacheable(Constants.SELECT_CACHE).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> queryLicensePlate(String queryText) {
		String hql = "select licensePlate from Car where licensePlate like :text";
		return this.getSession().createQuery(hql).setString("text", "%"+queryText+"%")
				.setCacheable(Constants.SELECT_CACHE).list();
	}

	@Override
	public void save(Car car) {
		Session session = this.getSession();
		session.save(car);
	}

	@Override
	public void update(Car car) {
		getSession().update(car);		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Car> findByClientId(int clientId) {
		String hql = "from Car where clientId = :clientId";
		return this.getSession().createQuery(hql).setInteger("clientId", clientId).setCacheable(Constants.SELECT_CACHE).list();
	}

}
