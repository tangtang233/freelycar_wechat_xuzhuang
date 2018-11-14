package com.geariot.platform.freelycar_wechat.dao;

import java.util.List;

import com.geariot.platform.freelycar_wechat.entities.Project;

public interface ProjectDao {
	
	Project findProjectByName(String name);
	
	Project findProjectById(int projectId);
	
	void save(Project project);
	
	void delete(Project project);
	
	void delete(int projectId);
	
	void deleteByprogramId(int programId);
	
	long getCount();
	
	List<Project> listProjects(int from , int pageSize);
	
	List<Object[]> getProjectName();
	
	List<Project> getConditionQuery(String andCondition , int from , int pageSize);
	
	long getConditionCount(String andCondition);
	
	long countInventoryByIds(List<String> inventoryIds);
	
	void deleteInventory(int projectId);

	List<Project> listProjectOnSale();
}
