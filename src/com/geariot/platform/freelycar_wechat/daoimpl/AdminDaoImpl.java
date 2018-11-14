package com.geariot.platform.freelycar_wechat.daoimpl;

import com.geariot.platform.freelycar_wechat.dao.AdminDao;
import com.geariot.platform.freelycar_wechat.entities.Admin;
import com.geariot.platform.freelycar_wechat.entities.Role;
import com.geariot.platform.freelycar_wechat.utils.Constants;
import com.geariot.platform.freelycar_wechat.utils.query.QueryUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AdminDaoImpl implements AdminDao {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	private Session getSession(){
		return sessionFactory.getCurrentSession();
	}
	
	@Override
	public Admin findAdminByAccount(String account) {
		String hql = "from Admin where account = :account";
		return (Admin) getSession().createQuery(hql).setString("account", account)
				.setCacheable(Constants.SELECT_CACHE).uniqueResult();
	}

	@Override
	public Admin findAdminById(int id) {
		String hql = "from Admin where id = :id";
		return (Admin) getSession().createQuery(hql).setInteger("id", id)
				.setCacheable(Constants.SELECT_CACHE).uniqueResult();
	}
	
	@Override
	public void save(Admin admin) {
		Session session = this.getSession();
		session.save(admin);
		session.evict(admin);	//detach对象，保存之后会立即重新从数据库中获取数据
	}

	@Override
	public void delete(Admin admin) {
		this.getSession().delete(admin);
	}
	
	@Override
	public void delete(String account) {
		String hql = "delete from Admin where account = :account";
		this.getSession().createQuery(hql).setString("account", account).executeUpdate();
	}

	@Override
	public boolean delete(int adminId) {
		String hql = "delete from Admin where id = :adminId";
		return this.getSession().createQuery(hql).setInteger("adminId", adminId).executeUpdate()>0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Admin> listAdmins(int from, int pageSize) {
		String hql = "from Admin";
		return this.getSession().createQuery(hql).setFirstResult(from).setMaxResults(pageSize)
				.setCacheable(Constants.SELECT_CACHE).list();
	}

	@Override
	public long getCount() {
		String hql = "select count(*) from Admin";
		return (long) this.getSession().createQuery(hql).setCacheable(Constants.SELECT_CACHE).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Admin> queryByNameAndAccount(String account, String name, int from, int pageSize) {
		QueryUtils qutils = new QueryUtils(getSession(), "from Admin");
		Query query = qutils.addStringLike("account", account)
				.addStringLike("name", name)
		.setFirstResult(from)
		.setMaxResults(pageSize)
		.getQuery();
		return query.list();
	}

	@Override
	public long getQueryCount(String account, String name) {
		QueryUtils qutils = new QueryUtils(getSession(), "select count(*) from Admin");
		Query query = qutils.addStringLike("account", account)
				.addStringLike("name", name)
		.getQuery();
		return (long) query.uniqueResult();
	}
	
	@Override
	public void save(Role role) {
		this.getSession().save(role);
	}

	@Override
	public void deleteByStaffId(int staffId) {
		String hql = "delete from Admin where staff.id = :id";
		this.getSession().createQuery(hql).setInteger("id", staffId).executeUpdate();
	}

	@Override
	public void clearRoles() {
		String hql1 = "delete from Role";
		int deleted1 = this.getSession().createQuery(hql1).executeUpdate();
		String hql2 = "delete from Permission";
		int deleted2 = this.getSession().createQuery(hql2).executeUpdate();
	}

}
