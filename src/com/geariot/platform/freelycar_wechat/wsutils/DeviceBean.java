package com.geariot.platform.freelycar_wechat.wsutils;

/**
 * 智能柜设备信息类
 * @author 唐炜
 */
public class DeviceBean {
    /**
     * 设备ID（范例：800100-1）
     */
    private String id;
    /**
     * 电压
     */
    private String voltage;
    /**
     * 在线或离线
     */
    private String heartbeat;
    /**
     * 库存剩余量
     */
    private Integer goods;
    /**
     * 柜门状态（0：关闭；1：打开）
     */
    private String magne;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVoltage() {
        return voltage;
    }

    public void setVoltage(String voltage) {
        this.voltage = voltage;
    }

    public String getHeartbeat() {
        return heartbeat;
    }

    public void setHeartbeat(String heartbeat) {
        this.heartbeat = heartbeat;
    }

    public Integer getGoods() {
        return goods;
    }

    public void setGoods(Integer goods) {
        this.goods = goods;
    }

    public String getMagne() {
        return magne;
    }

    public void setMagne(String magne) {
        this.magne = magne;
    }
}
