package com.geariot.platform.freelycar_wechat.model;

public enum DATATYPE {

	USER(0,"User"),
	CLASSDEF(1, "Classdef"),
	CLASSES(2,"Classes"),
	RESERVATION(3,"Reservation"),
	LOCATION(4, "Location"),
	CARD(5, "Card"),
	SERVICES(6,"Services"),
	STAFF(7,"Staff"),
	SESSIONTYPE(8,"SessionType"),
	PROGRAM(9,"Program"),
	AVAILABILITY(10, "Availability")
	;

	// 定义私有变量
	private int nCode;
	private String nMsg;
	

	// 构造函数，枚举类型只能为私有
	private DATATYPE(int _nCode, String msg) {

		this.nCode = _nCode;
		this.nMsg = msg;
	}

	
	public String getMsg() {

		return nMsg;
	}
	
	@Override
	public String toString() {

		return String.valueOf(this.nCode);

	}
}

