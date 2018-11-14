package com.geariot.platform.freelycar_wechat.model;

public enum ORDERS_TYPE {
	
	FAVOUR(0),
	MEMBER_CARD(1),
//	PACKAGE(5)
	;

	// 定义私有变量
	private int nCode;
	
	// 构造函数，枚举类型只能为私有
	private ORDERS_TYPE(int _nCode) {

		this.nCode = _nCode;
	}

	
	public int getValue()
	{
		return nCode;
	}
	
	@Override
	public String toString() {

		return String.valueOf(this.nCode);

	}
}

