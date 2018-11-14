package com.geariot.platform.freelycar_wechat.utils;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;

public class WsTest {

    public static void main(String[] args) {
        WebSocket ws = new WebSocket();
        JSONObject jo = new JSONObject();
        jo.put("message", "这是后台返回的消息！");
        jo.put("To","tangtang");
        try {
            ws.sendMessageAll(jo.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
