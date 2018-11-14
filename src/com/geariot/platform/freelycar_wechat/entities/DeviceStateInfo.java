package com.geariot.platform.freelycar_wechat.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * 智能柜设备状态信息
 * @author 唐炜
 */
@Entity
public class DeviceStateInfo implements Serializable {

    public final static int EMPTY = 0;
    /**
     * 用户开门标识
     * 1.用户预约
     * 2.用户取消预约（已作废）
     * 3.用户确认服务完成（已作废）
     */
    public final static int USER_RESERVATION = 1;
    public final static int USER_CANCEL_RESERVATION = 2;
    public final static int USER_FINFISH = 3;
    /**
     * 技师开门标识
     * 4.技师开始服务（取车）（已作废）
     * 5.技师结束服务（放回车钥匙）
     */
    public final static int STAFF_RECEIVE = 4;
    public final static int STAFF_FINISH = 5;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer cabinetId;

    private String orderId;

    private Integer reservationId;

    private String cabinetSN;

    private String gridSN;

    private Integer state;

    private String licensePlate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCabinetId() {
        return cabinetId;
    }

    public void setCabinetId(Integer cabinetId) {
        this.cabinetId = cabinetId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getReservationId() {
        return reservationId;
    }

    public void setReservationId(Integer reservationId) {
        this.reservationId = reservationId;
    }

    public String getCabinetSN() {
        return cabinetSN;
    }

    public void setCabinetSN(String cabinetSN) {
        this.cabinetSN = cabinetSN;
    }

    public String getGridSN() {
        return gridSN;
    }

    public void setGridSN(String gridSN) {
        this.gridSN = gridSN;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    @Override
    public String toString() {
        return "DeviceStateInfo{" +
                "id=" + id +
                ", cabinetId=" + cabinetId +
                ", orderId='" + orderId + '\'' +
                ", reservationId=" + reservationId +
                ", cabinetSN='" + cabinetSN + '\'' +
                ", gridSN='" + gridSN + '\'' +
                ", state=" + state +
                ", licensePlate='" + licensePlate + '\'' +
                '}';
    }
}
