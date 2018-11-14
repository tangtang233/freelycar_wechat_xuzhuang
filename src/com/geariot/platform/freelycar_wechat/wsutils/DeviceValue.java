package com.geariot.platform.freelycar_wechat.wsutils;

/**
 * 智能柜信息类对应值
 *
 * @author 唐炜
 */
public enum DeviceValue {
    /**
     * 设备在线
     */
    HEARTBEAT_ONLINE("online"),
    /**
     * 设备离线
     */
    HEARTBEAT_OFFLINE("offline"),
    /**
     * 设备是关门状态
     */
    MAGNE_CLOSE("0"),
    /**
     * 设备是开门状态
     */
    MAGNE_OPEN("1");

    private String value;

    DeviceValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "DeviceValue{" +
                "value='" + value + '\'' +
                '}';
    }
}
