package com.geariot.platform.freelycar_wechat.wsutils;


import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WSClientTest {
    public static void main(String[] args) {
//        String deviceId = "869300034071526-10";
//        System.out.println(WSClient.getDeviceStateByID(deviceId));
//        System.out.println(WSClient.controlDevices(deviceId,"1"));
//        System.out.println(WSClient.getDeviceStateByID("867858031556536"));
//        System.out.println(WSClient.getDeviceStateByID("867858031556536-10"));
//
//        ConsumOrder consumOrder = new ConsumOrder();
//        consumOrder.setId("S20180802y92qzh");
//        consumOrder.setState(1);
//        WechatTemplateMessage.orderChanged(consumOrder,"ojtNs1vQCAHik8kc93vuoAKJlCzs");

        String sn = "869300034071526";
//        openAllDoors(sn);
    }

    public static void openAllDoors(String sn) {
        if (StringUtils.isNotEmpty(sn)) {
            for (int i = 0; i < 16; i++) {
                int number = i + 1;
                String deviceId = sn + "-" + number;
                System.out.println(WSClient.getDeviceStateByID(deviceId));
                System.out.println(WSClient.controlDevices(deviceId, "1"));
            }
        }
    }
}
