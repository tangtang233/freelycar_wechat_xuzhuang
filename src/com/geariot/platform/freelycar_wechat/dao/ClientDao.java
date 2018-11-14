package com.geariot.platform.freelycar_wechat.dao;

import java.util.List;

import com.geariot.platform.freelycar_wechat.entities.Client;

public interface ClientDao {
	
	List<Client> list(int from, int pageSize);

	long getCount();
	
	Client findByPhone(String phone);
	
	Client findById(int clientId);

	void save(Client client);
	
	void delete(List<Integer> clientId);
	
	List<Client> query(String condition, int from, int pageSize);

	long getQueryCount(String andCondition);

	List<String> getClientNames(String name);
	
}
