package com.geariot.platform.freelycar_wechat.wsutils;


import org.apache.commons.lang.StringUtils;

public class WSClientTest {
    public static void main(String[] args) {
//        String deviceId = "867858031556536-7";
//        System.out.println(WSClient.getDeviceStateByID(deviceId));
//        System.out.println(WSClient.controlDevices(deviceId,"1"));
//        System.out.println(WSClient.getDeviceStateByID("867858031556536"));
//        System.out.println(WSClient.getDeviceStateByID("867858031556536-10"));
//
//        ConsumOrder consumOrder = new ConsumOrder();
//        consumOrder.setId("S20180802y92qzh");
//        consumOrder.setState(1);
//        WechatTemplateMessage.orderChanged(consumOrder,"ojtNs1vQCAHik8kc93vuoAKJlCzs");

//        String sn = "867858031556536";
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
