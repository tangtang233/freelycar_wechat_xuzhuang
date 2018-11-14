package com.geariot.platform.freelycar_wechat.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.geariot.platform.freelycar_wechat.utils.JsonDateDeserialize;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author 唐炜
 */
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Reservation implements Serializable {
    /**
     * 0：待服务；1：正在服务；2：服务完毕(车主可取车)；3：已结算；4：用户取消；5：已取车
     */
    public static final int WAITING_FOR_THE_SERVICE = 0;
    public static final int IS_THE_SERVICE = 1;
    public static final int CAN_PICK_UP = 2;
    public static final int  USER_PAY_FINISH = 3;
    public static final int USER_CANCEL = 4;
    public static final int ALREADY_PICK_UP = 5;
    /**
     * 0：未读；1：已读
     */
    public static final int UNREAD = 0;
    public static final int READ = 1;
    private static final long serialVersionUID = 2L;
    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 车牌号码
     */
    private String licensePlate;
    /**
     * 车主姓名
     */
    private Integer clientId;
    /**
     * 车主姓名
     */
    private String name;
    /**
     * 服务项目ids
     */
    private String serviceIds;
    /**
     * 服务项目
     */
    private String services;
    /**
     * 预约时间
     */
    @JsonDeserialize(using = JsonDateDeserialize.class)
    private Date createTime;
    /**
     * 预计取车时间
     */
    @JsonDeserialize(using = JsonDateDeserialize.class)
    private Date pickUpTime;
    /**
     * 接单时间
     */
    @JsonDeserialize(using = JsonDateDeserialize.class)
    private Date orderTime;
    /**
     * 预约智能柜别名
     */
    private String cabinetName;
    /**
     * 预约智能柜编号
     */
    private String cabinetSN;
    /**
     * 格子编号
     */
    private String gridSN;
    /**
     * 车辆停放位置
     */
    private String location;
    /**
     * 状态（0：待服务；1：正在服务（接车）；2：服务完毕（完工）；3：已取车（交车）；4：用户取消）
     */
    private Integer state;
    /**
     * openId
     */
    private String openId;
    /**
     * 对应的consumOrderId
     */
    private String consumOrderId;

    /**
     * 车型
     */
    private String carBrand;

    @Transient
    private List<Project> projects;

    @Transient
    private float totalPrice;

    /**
     * 构造方法
     */
    public Reservation() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServiceIds() {
        return serviceIds;
    }

    public void setServiceIds(String serviceIds) {
        this.serviceIds = serviceIds;
    }

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getPickUpTime() {
        return pickUpTime;
    }

    public void setPickUpTime(Date pickUpTime) {
        this.pickUpTime = pickUpTime;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public String getCabinetName() {
        return cabinetName;
    }

    public void setCabinetName(String cabinetName) {
        this.cabinetName = cabinetName;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getConsumOrderId() {
        return consumOrderId;
    }

    public void setConsumOrderId(String consumOrderId) {
        this.consumOrderId = consumOrderId;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", licensePlate='" + licensePlate + '\'' +
                ", name='" + name + '\'' +
                ", services='" + services + '\'' +
                ", createTime=" + createTime +
                ", pickUpTime=" + pickUpTime +
                ", cabinetSN='" + cabinetSN + '\'' +
                ", gridSN='" + gridSN + '\'' +
                ", location='" + location + '\'' +
                ", state=" + state +
                ", openId='" + openId + '\'' +
                ", consumOrderId='" + consumOrderId + '\'' +
                '}';
    }
}
