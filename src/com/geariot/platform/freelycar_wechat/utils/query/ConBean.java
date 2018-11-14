package com.geariot.platform.freelycar_wechat.utils.query;

public class ConBean {

	private String key;
	
	private String operator;
	
	public ConBean(String key, String operator)
	{
		this.key = key;
		this.operator = operator;
	}
	
	
	public String getKey() {
		return key;
	}


	public void setKey(String key) {
		this.key = key;
	}


	public String getOperator() {
		return operator;
	}


	public void setOperator(String operator) {
		this.operator = operator;
	}


	/**
	 * 暂时只支持 = 和 like两种查询， between等暂时先不实现
	 */
	public String toString()
	{
		StringBuffer buf = new StringBuffer(key);
		if(operator.equals("=="))
		{
			buf.append(" ");
			buf.append("=");
			buf.append(" ");
		}
		else
		{
			buf.append(" ");
			buf.append(operator);
			buf.append(" ");
		}
		return buf.toString();
	}
}
