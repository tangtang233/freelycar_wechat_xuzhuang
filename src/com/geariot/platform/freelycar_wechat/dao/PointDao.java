package com.geariot.platform.freelycar_wechat.dao;

import java.util.List;

public interface PointDao {
	public List<Object[]> getPoint(int clientId);
	public Object getSumPoint(int clientId);
}
