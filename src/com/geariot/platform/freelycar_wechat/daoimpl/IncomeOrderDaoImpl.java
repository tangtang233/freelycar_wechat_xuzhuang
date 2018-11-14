package com.geariot.platform.freelycar_wechat.daoimpl;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.geariot.platform.freelycar_wechat.dao.IncomeOrderDao;
import com.geariot.platform.freelycar_wechat.entities.IncomeOrder;
import com.geariot.platform.freelycar_wechat.model.ORDER_CON;
import com.geariot.platform.freelycar_wechat.utils.Constants;
import com.geariot.platform.freelycar_wechat.utils.DateHandler;
import com.geariot.platform.freelycar_wechat.utils.query.QueryUtils;

@Repository
public class IncomeOrderDaoImpl implements IncomeOrderDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	private Session getSession(){
		return this.sessionFactory.getCurrentSession();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<IncomeOrder> findByClientId(int clientId) {
		String hql = "from IncomeOrder where clientId = :clientId order by payDate desc";
		return this.getSession().createQuery(hql).setInteger("clientId", clientId).setCacheable(Constants.SELECT_CACHE).list();
	}

	@Override
	public void save(IncomeOrder incomeOrder) {
		this.getSession().save(incomeOrder);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<IncomeOrder> listByDate(int from , int pageSize) {
		String hql = "from IncomeOrder where date(payDate) = curdate()";
		return this.getSession().createQuery(hql).setFirstResult(from).setMaxResults(pageSize).setCacheable(Constants.SELECT_CACHE).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<IncomeOrder> listByDate() {
		String hql = "from IncomeOrder where date(payDate) = curdate()";
		return this.getSession().createQuery(hql).setCacheable(Constants.SELECT_CACHE).list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<IncomeOrder> listByMonth(int from , int pageSize) {
		String hql = "from IncomeOrder where date_format(payDate,'%Y-%m') = date_format(now(),'%Y-%m')";
		return this.getSession().createQuery(hql).setFirstResult(from).setMaxResults(pageSize).setCacheable(Constants.SELECT_CACHE).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<IncomeOrder> listByMonth() {
		String hql = "from IncomeOrder where date_format(payDate,'%Y-%m') = date_format(now(),'%Y-%m')";
		return this.getSession().createQuery(hql).setCacheable(Constants.SELECT_CACHE).list();
	}
	
	/*
	 * 完整sql查询语句：
	 * FROM
			(
			SELECT
				i.amount AS income,
				0 AS expend,
				DATE_FORMAT(i.payDate, '%Y-%m') AS payDate
			FROM
				incomeorder AS i
			WHERE
				i.payDate > '2017-01-01'
			AND i.payDate < '2017-12-31'
			UNION
				SELECT
					0 AS income,
					e.amount AS expend,
					DATE_FORMAT(e.payDate, '%Y-%m') AS payDate
				FROM
					expendorder AS e
				WHERE
					e.payDate > '2017-01-01'
				AND e.payDate < '2017-12-31'
			) AS t
		GROUP BY
			payDate;
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> listMonthStat(Date start, Date end) {
		StringBuilder sb = new StringBuilder("SELECT SUM(income), SUM(expend), payDate ");
		sb.append("FROM (");
		sb.append("SELECT i.amount AS income,0 AS expend,DATE_FORMAT(i.payDate, '%Y-%m') AS payDate FROM incomeorder AS i ");
		sb.append("WHERE i.payDate >= :start AND i.payDate <= :end");
		sb.append(" UNION ALL ");
		sb.append("SELECT 0 AS income,e.amount AS expend,DATE_FORMAT(e.payDate, '%Y-%m') AS payDate FROM expendorder AS e ");
		sb.append("WHERE e.payDate >= :start AND e.payDate <= :end");
		sb.append(") AS t ");
		sb.append("GROUP BY payDate");
		return this.getSession().createSQLQuery(sb.toString()).setDate("start", start).setDate("end", end)
				.setCacheable(Constants.SELECT_CACHE).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<IncomeOrder> listByWeek(int from , int pageSize) {
		String hql = "from IncomeOrder where YEARWEEK(date_format(payDate,'%Y-%m-%d')) = YEARWEEK(now())";
		return this.getSession().createQuery(hql).setFirstResult(from).setMaxResults(pageSize).setCacheable(Constants.SELECT_CACHE).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<IncomeOrder> listByWeek() {
		String hql = "from IncomeOrder where YEARWEEK(date_format(payDate,'%Y-%m-%d')) = YEARWEEK(now())";
		return this.getSession().createQuery(hql).setCacheable(Constants.SELECT_CACHE).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<IncomeOrder> listByDateRange(Date startTime, Date endTime, int from, int pageSize) {
		String hql = "from IncomeOrder where payDate >= :date1 and payDate <= :date2";
		return this.getSession().createQuery(hql)
				.setTimestamp("date1", DateHandler.setTimeToBeginningOfDay(DateHandler.toCalendar(startTime)).getTime())
				.setTimestamp("date2", DateHandler.setTimeToEndofDay(DateHandler.toCalendar(endTime)).getTime())
				.setFirstResult(from).setMaxResults(pageSize).setCacheable(Constants.SELECT_CACHE).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<IncomeOrder> listByDateRange(Date startTime, Date endTime) {
		String hql = "from IncomeOrder where payDate >= :date1 and payDate <= :date2";
		return this.getSession().createQuery(hql)
				.setTimestamp("date1", DateHandler.setTimeToBeginningOfDay(DateHandler.toCalendar(startTime)).getTime())
				.setTimestamp("date2", DateHandler.setTimeToEndofDay(DateHandler.toCalendar(endTime)).getTime())
				.setCacheable(Constants.SELECT_CACHE).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> listByPayMethodToday() {
		String hql = "select sum(amount) , payMethod from IncomeOrder where date(payDate) = curdate() group by payMethod";
		return this.getSession().createSQLQuery(hql).setCacheable(Constants.SELECT_CACHE).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> listByPayMethodMonth() {
		String hql = "select sum(amount) , payMethod from IncomeOrder where date_format(payDate,'%Y-%m') = date_format(now(),'%Y-%m') group by payMethod";
		return this.getSession().createSQLQuery(hql).setCacheable(Constants.SELECT_CACHE).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> listByPayMethodRange(Date startTime, Date endTime) {
		String hql = "select sum(amount) , payMethod from IncomeOrder where payDate >= :date1 and payDate < :date2 group by payMethod";
		return this.getSession().createSQLQuery(hql)
				.setTimestamp("date1", DateHandler.setTimeToBeginningOfDay(DateHandler.toCalendar(startTime)).getTime())
				.setTimestamp("date2", DateHandler.setTimeToEndofDay(DateHandler.toCalendar(endTime)).getTime())
				.setCacheable(Constants.SELECT_CACHE).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<IncomeOrder> listIncomeOrderByClientId(String condition, int clientId, int from, int pageSize) {
		StringBuffer basic = new StringBuffer("from IncomeOrder where clientId = :clientId");
		String hql = QueryUtils.createString(basic, condition, ORDER_CON.DESC_ORDER).toString();
		return this.getSession().createQuery(hql).setInteger("clientId", clientId)
				.setFirstResult(from).setMaxResults(pageSize)
				.setCacheable(Constants.SELECT_CACHE).list();
	}

	@Override
	public long countIncomeOrderByClientId(String condition, int clientId) {
		StringBuffer basic = new StringBuffer("select count(*) from IncomeOrder where clientId = :clientId");
		StringBuffer temp = QueryUtils.createQueryString(basic, condition, ORDER_CON.NO_ORDER);
		temp.append(" order by payDate desc");
		String hql = temp.toString();
		return (long) this.getSession().createQuery(hql).setInteger("clientId", clientId)
				.setCacheable(Constants.SELECT_CACHE).uniqueResult();
	}

	@Override
	public float countAmountByClientId(String condition, int clientId) {
		StringBuffer basic = new StringBuffer("select sum(amount) from IncomeOrder where clientId = :clientId");
		String hql = QueryUtils.createQueryString(basic, condition, ORDER_CON.NO_ORDER).toString();
		return (float) this.getSession().createQuery(hql).setInteger("clientId", clientId)
				.setCacheable(Constants.SELECT_CACHE).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> MemberPayToday() {
		String hql = "select sum(amount) , member from IncomeOrder where date(payDate) = curdate() group by member";
		return this.getSession().createSQLQuery(hql).setCacheable(Constants.SELECT_CACHE).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> MemberPayMonth() {
		String hql = "select sum(amount) , member from IncomeOrder where date_format(payDate,'%Y-%m') = date_format(now(),'%Y-%m') group by member";
		return this.getSession().createSQLQuery(hql).setCacheable(Constants.SELECT_CACHE).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> MemberPayRange(Date startTime, Date endTime) {
		String hql = "select sum(amount) , member from IncomeOrder where payDate >= :date1 and payDate < :date2 group by member";
		return this.getSession().createSQLQuery(hql)
				.setTimestamp("date1", DateHandler.setTimeToBeginningOfDay(DateHandler.toCalendar(startTime)).getTime())
				.setTimestamp("date2", DateHandler.setTimeToEndofDay(DateHandler.toCalendar(endTime)).getTime())
				.setCacheable(Constants.SELECT_CACHE).list();
	}
	
	
}
