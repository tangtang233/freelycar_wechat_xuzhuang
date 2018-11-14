package com.geariot.platform.freelycar_wechat.dao;

import com.geariot.platform.freelycar_wechat.entities.DeviceStateInfo;
import com.geariot.platform.freelycar_wechat.utils.hibernate.BaseDaoInter;

import java.util.List;

/**
 * @author 唐炜
 */
public interface DeviceStateInfoDao extends BaseDaoInter<Integer,DeviceStateInfo> {
//    int save(DeviceStateInfo deviceStateInfo);

//    void saveOrUpdate(DeviceStateInfo deviceStateInfo);

    DeviceStateInfo findById(int id);

    List<DeviceStateInfo> findByCabinetId(int cabinetId);

    List<DeviceStateInfo> findByCabinetSN(String cabinetSN);

    List<DeviceStateInfo> findEmptyDevices(String cabinetSN);

    DeviceStateInfo findByCabinetSNAndGridSN(String cabinetSN, String gridSN);

    DeviceStateInfo findByOrderId(String orderId);
}
