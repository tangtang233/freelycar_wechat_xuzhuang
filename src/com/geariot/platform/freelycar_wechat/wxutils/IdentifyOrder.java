package com.geariot.platform.freelycar_wechat.wxutils;

public class IdentifyOrder {
	public static boolean identify(String orderId){
		if(orderId.charAt(0)=='S') {
			return true;
		} else {
			return false;
		}
	}
}
