package com.geariot.platform.freelycar_wechat.dao;

import java.util.List;

import com.geariot.platform.freelycar_wechat.entities.WXPayOrder;

public interface WXPayOrderDao {
	public void saveWXPayOrder(WXPayOrder wxPayOrder);
	public WXPayOrder findById(String wxPayOrderId);
	public List<WXPayOrder> listByClientId(int clientId, int from, int number);
	public long getCountByClientId(int clientId);
}
