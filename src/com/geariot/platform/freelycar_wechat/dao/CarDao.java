package com.geariot.platform.freelycar_wechat.dao;

import java.util.List;

import com.geariot.platform.freelycar_wechat.entities.Car;

public interface CarDao {
	void deleteById(int carId);
	
	Car findById(int carId);
	
	Car findByLicense(String licensePlate);

	List<String> queryLicensePlate(String queryText);
	
	void save(Car car);

	void update(Car car);
	
	List<Car> findByClientId(int clientId);
}
