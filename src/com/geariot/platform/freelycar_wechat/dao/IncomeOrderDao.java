package com.geariot.platform.freelycar_wechat.dao;

import java.util.Date;
import java.util.List;

import com.geariot.platform.freelycar_wechat.entities.IncomeOrder;

public interface IncomeOrderDao {
	List<IncomeOrder> findByClientId(int clientId);
	
	void save(IncomeOrder incomeOrder);
	
	List<IncomeOrder> listByDate(int from , int pageSize);
	
	List<IncomeOrder> listByMonth(int from , int pageSize);

	List<Object[]> listMonthStat(Date start, Date end);
	
	List<IncomeOrder> listByWeek(int from , int pageSize);
	
	List<IncomeOrder> listByDate();
	
	List<IncomeOrder> listByMonth();
	
	List<IncomeOrder> listByWeek();
	
	List<IncomeOrder> listByDateRange(Date startTime , Date endTime , int from , int pageSize);
	
	List<IncomeOrder> listByDateRange(Date startTime , Date endTime);
	
	List<Object[]> listByPayMethodToday();
	
	List<Object[]> listByPayMethodMonth();
	
	List<Object[]> listByPayMethodRange(Date startTime , Date endTime);
	
	/*List<Object[]> programNameToday();
	
	List<Object[]> programNameMonth();
	
	List<Object[]> programNameRange(Date startTime , Date endTime);*/
	
	List<IncomeOrder> listIncomeOrderByClientId(String condition, int clientId, int from, int pageSize);
	
	long countIncomeOrderByClientId(String condition, int clientId);
	
	float countAmountByClientId(String condition, int clientId);
	
	List<Object[]> MemberPayToday();
	
	List<Object[]> MemberPayMonth();
	
	List<Object[]> MemberPayRange(Date startTime , Date endTime);
}
