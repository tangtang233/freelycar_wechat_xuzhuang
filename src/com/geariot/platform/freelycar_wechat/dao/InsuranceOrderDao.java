/**
 * 
 */
package com.geariot.platform.freelycar_wechat.dao;

import com.geariot.platform.freelycar_wechat.entities.InsuranceOrder;

/**
 * @author mxy940127
 *
 */
public interface InsuranceOrderDao {
	
	void save(InsuranceOrder insuranceOrder);
	
	InsuranceOrder findByLicensePlate(String licensePlate);

	void update(InsuranceOrder insuranceOrder);
}
