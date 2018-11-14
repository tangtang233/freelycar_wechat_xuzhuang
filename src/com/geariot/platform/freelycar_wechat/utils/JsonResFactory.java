package com.geariot.platform.freelycar_wechat.utils;


import java.util.Date;

import org.json.JSONObject;

import com.geariot.platform.freelycar_wechat.model.RESCODE;

public class JsonResFactory {
	public static JSONObject buildOrg(RESCODE rescode){
		JSONObject obj = new JSONObject();
		obj.put(Constants.RESPONSE_CODE_KEY, rescode);
		obj.put(Constants.RESPONSE_MSG_KEY, rescode.getMsg());
		return obj;
	}
	
	public static JSONObject buildOrg(RESCODE rescode, String key, Object value){
		JSONObject obj = buildOrg(rescode);
		obj.put(key, value);
		return obj;
	}
	
	public static net.sf.json.JSONObject buildNet(RESCODE rescode){
		net.sf.json.JSONObject obj = new net.sf.json.JSONObject();
		obj.put(Constants.RESPONSE_CODE_KEY, rescode.toString());
		obj.put(Constants.RESPONSE_MSG_KEY, rescode.getMsg());
		return obj;
	}
	
	public static net.sf.json.JSONObject buildNetWithData(RESCODE rescode, Object data){
		net.sf.json.JSONObject obj = buildNet(rescode);
		obj.put(Constants.RESPONSE_DATA_KEY, data);
		return obj;
	}
	
	public static net.sf.json.JSONObject buildNet(RESCODE rescode, String key, Object value){
		net.sf.json.JSONObject obj = buildNet(rescode);
		obj.put(key, value);
		return obj;
	}
	
	public static net.sf.json.JsonConfig dateConfig(Class<?>... classes){
		net.sf.json.JsonConfig config = new net.sf.json.JsonConfig();
		config.registerJsonValueProcessor(Date.class, new DateJsonValueProcessor());
		config.setJsonPropertyFilter(new JsonPropertyFilter(classes));
		return config;
	}
	
}
