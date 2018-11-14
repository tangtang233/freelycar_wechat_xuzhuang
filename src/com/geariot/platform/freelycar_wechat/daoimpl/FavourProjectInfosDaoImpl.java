package com.geariot.platform.freelycar_wechat.daoimpl;

import com.geariot.platform.freelycar_wechat.entities.FavourProjectInfos;
import com.geariot.platform.freelycar_wechat.utils.Constants;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.geariot.platform.freelycar_wechat.dao.FavourProjectInfosDao;
@Repository
public class FavourProjectInfosDaoImpl implements FavourProjectInfosDao{
	private SessionFactory sessionFactory;
	
	private Session getSession(){
		return this.sessionFactory.getCurrentSession();
	}

	public FavourProjectInfos findById(int projectId){
		String hql= "from favourProjectInfos where id= :id";
		return (FavourProjectInfos)this.getSession().createQuery(hql).setCacheable(Constants.SELECT_CACHE).setInteger("id", projectId).uniqueResult();
	}
}
