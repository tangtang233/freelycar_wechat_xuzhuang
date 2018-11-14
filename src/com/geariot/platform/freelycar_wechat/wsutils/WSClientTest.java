package com.geariot.platform.freelycar_wechat.wsutils;


import com.geariot.platform.freelycar_wechat.entities.ConsumOrder;
import com.geariot.platform.freelycar_wechat.wxutils.WechatTemplateMessage;

public class WSClientTest {
    public static void main(String[] args) {
        String deviceId = "867858031556536-10";
        System.out.println(WSClient.getDeviceStateByID(deviceId));
        System.out.println(WSClient.controlDevices(deviceId,"1"));
//        System.out.println(WSClient.getDeviceStateByID("867858031556536"));
//        System.out.println(WSClient.getDeviceStateByID("867858031556536-10"));
//
//        ConsumOrder consumOrder = new ConsumOrder();
//        consumOrder.setId("S20180802y92qzh");
//        consumOrder.setState(1);
//        WechatTemplateMessage.orderChanged(consumOrder,"ojtNs1vQCAHik8kc93vuoAKJlCzs");
    }
}
