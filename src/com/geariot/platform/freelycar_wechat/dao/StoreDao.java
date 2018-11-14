/**
 * 
 */
package com.geariot.platform.freelycar_wechat.dao;

import java.util.List;

import com.geariot.platform.freelycar_wechat.entities.Store;

/**
 * @author mxy940127
 *
 */

public interface StoreDao {
	
	void save(Store store);
	
	long getCount();
	
	void delete(int storeId);
	
	List<Store> listStore(int from, int pageSize);
	
	Store findStoreById(int storeId);
}
