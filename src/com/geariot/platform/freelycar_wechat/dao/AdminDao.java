package com.geariot.platform.freelycar_wechat.dao;


import com.geariot.platform.freelycar_wechat.entities.Admin;
import com.geariot.platform.freelycar_wechat.entities.Role;

import java.util.List;

public interface AdminDao {
	
	Admin findAdminByAccount(String account);
	
	Admin findAdminById(int id);

	void save(Admin admin);

	void delete(Admin admin);
	
	void delete(String account);
	
	boolean delete(int adminId);
	
	List<Admin> listAdmins(int from, int pageSize);
	
	long getCount();

	List<Admin> queryByNameAndAccount(String account, String name, int from, int pageSize);
	
	long getQueryCount(String account, String name);
	
	void save(Role role);
	
	void clearRoles();
	
	void deleteByStaffId(int staffId);
}
