package com.geariot.platform.freelycar_wechat.utils;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.geariot.platform.freelycar_wechat.dao.ConsumOrderDao;
import com.geariot.platform.freelycar_wechat.entities.ConsumOrder;

@Component
public class StoreUtil {
	private static ConsumOrderDao consumOrderDao; 
	@SuppressWarnings("static-access")
	@Autowired
	public void setConsumOrderDao(ConsumOrderDao consumOrderDao){
		this.consumOrderDao = consumOrderDao;
	}
	public static float getCommentStar(int storeId){
		List<ConsumOrder> consumOrders = consumOrderDao.findCommentByStoreId(storeId);
		float sumStars=0;
		int commentPeople=0;
		for(ConsumOrder consumOrder:consumOrders){
			if(consumOrder.getCommentDate()!=null){
				sumStars += consumOrder.getStars();
				commentPeople++;
			}
		}
		if(commentPeople==0)
			return 0;
		return sumStars/commentPeople;
	}
}
