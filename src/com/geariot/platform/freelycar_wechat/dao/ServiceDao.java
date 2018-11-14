package com.geariot.platform.freelycar_wechat.dao;

import java.util.List;
import java.util.Set;

import com.geariot.platform.freelycar_wechat.entities.Service;
import com.geariot.platform.freelycar_wechat.entities.ServiceProjectInfo;

public interface ServiceDao {

	Service findServiceById(int serviceId);
	
	void save(Service service);
	
//	void delete(int serviceId);
	
	/*List<Service> listServices(int from , int pageSize);*/
	
	List<Service> queryByName(String name);
	
	long getCount();
	
	List<Service> listServices(String andCondition , int from , int pageSize);
	
	long getConditionCount(String andCondition);
	
	List<Object> listName();
	
	long countProjectByIds(List<Integer> ids);
	
	List<Service> listOnWX(int from, int pageSize);

	List<ServiceProjectInfo> getListByServiceId(int serviceId);
}
