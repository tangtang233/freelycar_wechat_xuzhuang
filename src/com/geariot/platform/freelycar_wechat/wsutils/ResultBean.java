package com.geariot.platform.freelycar_wechat.wsutils;

public class ResultBean {
	private String res;
	private String value;
	
	public ResultBean() {
		super();
	}
	public ResultBean(String res, String value) {
		super();
		this.res = res;
		this.value = value;
	}
	public String getRes() {
		return res;
	}
	public void setRes(String res) {
		this.res = res;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return "ResultBean [res=" + res + ", value=" + value + "]";
	}
}
