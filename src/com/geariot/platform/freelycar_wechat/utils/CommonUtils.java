package com.geariot.platform.freelycar_wechat.utils;

import java.util.UUID;

public class CommonUtils {
	
	public static String generateUUID() {
		
		return UUID.randomUUID().toString().replace("-", "");
	}

	
	/**
     * 检测字符串是否不为空(null,"","null")
     * 
     * @param s
     * @return 不为空则返回true，否则返回false
     */
    public static boolean notEmpty(String s) {
        return s != null && !"".equals(s) && !"null".equals(s);
    }

    /**
     * 检测字符串是否为空(null,"","null")
     * 
     * @param s
     * @return 为空则返回true，不否则返回false
     */
    public static boolean isEmpty(String s) {
        return s == null || "".equals(s) || "null".equals(s);
    }

	
	
	

}
