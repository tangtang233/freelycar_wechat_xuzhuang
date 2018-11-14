package com.geariot.platform.freelycar_wechat.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class Constants {

	public static final String RESPONSE_CODE_KEY = "code"; //返回对象里的code的key名称
	public static final String RESPONSE_MSG_KEY = "msg"; //返回对象里的msg的key名称
	public static final String RESPONSE_DATA_KEY = "data"; //返回对象里的data的key名称
	public static final String RESPONSE_SIZE_KEY = "size"; //返回对象里的size的key名称
	public static final String RESPONSE_CLIENT_KEY = "client";
	public static final String RESPONSE_REAL_SIZE_KEY = "realSize";
	public static final String RESPONSE_AMOUNT_KEY = "amount";
	public static final String RESPONSE_FAVOUR_KEY = "favour";
	public static final String RESPONSE_WXORDER_KEY = "wxOrder";
	public static final String RESPONSE_WXUSER_KEY = "wxUser";
	public static final String RESPONSE_POINT_KEY = "point";
	public static final String RESPONSE_CONSUMORDER_KEY = "orders";
	public static final String RESPONSE_STORE_KEY = "store";
	public static final String RESPONSE_STAR_KEY = "star";
	public static boolean RELOAD_ROLES;
	
	public static final int PAY_BY_WX = 3;
	public static final int PAY_UNPAY = 0;
	public static final int PAY_FINISH = 1;
	
	public static final int PROJECT_WITH_CARD = 0;
	public static final int PROJECT_WITH_CASH = 1;
	
	public static final String CARD_PROGRAM = "办卡";
	public static final String WX_CARDANDFAVOUR = "微信卡券";
	
	public static final String STAFF_NAME_SPLIT = ";";

	/**
	 * 订单状态 0,1,2=接,完,交
	 */
	public static final String CAR_SERVICE_START = "0";
	public static final String CAR_SERVICE_COMPLETE  = "1";
	public static final String CAR_SERVICE_FINISH = "2";

	/*是否开启查询缓存*/
	public static boolean SELECT_CACHE = false;
	
	private static Properties p = null;
	
	/*static {
		p = new Properties();
		try {
			ClassLoader cl = Thread.currentThread().getContextClassLoader();  
			if (cl == null)
				cl = Constants.class.getClassLoader(); 
			InputStream in = cl.getResourceAsStream("config.properties");
			
			p.load(in);
			RELOAD_ROLES = Boolean.valueOf(p.getProperty(RELOAD_ROLES_KEY));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}*/
	
	
		
}
