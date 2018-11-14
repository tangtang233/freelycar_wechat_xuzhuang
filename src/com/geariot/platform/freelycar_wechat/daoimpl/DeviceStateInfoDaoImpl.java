package com.geariot.platform.freelycar_wechat.daoimpl;


import com.geariot.platform.freelycar_wechat.dao.DeviceStateInfoDao;
import com.geariot.platform.freelycar_wechat.entities.DeviceStateInfo;
import com.geariot.platform.freelycar_wechat.utils.Constants;
import com.geariot.platform.freelycar_wechat.utils.hibernate.BaseDaoImpl;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 唐炜
 */
@Repository
public class DeviceStateInfoDaoImpl extends BaseDaoImpl<Integer,DeviceStateInfo> implements DeviceStateInfoDao {

    public DeviceStateInfoDaoImpl() {
        super(DeviceStateInfo.class);
    }

    @Override
    public DeviceStateInfo findById(int id) {
        String hql = "from DeviceStateInfo where id = :id";
        return (DeviceStateInfo) this.getSession().createSQLQuery(hql).setInteger("id", id).setCacheable(Constants.SELECT_CACHE).uniqueResult();
    }

    @Override
    public List<DeviceStateInfo> findByCabinetId(int cabinetId) {
        String hql = "from DeviceStateInfo where cabinetId = :cabinetId";
        Map<String, Object> params = new HashMap<>(1);
        params.put("cabinetId",cabinetId);
        return this.findList(hql,params);
    }

    @Override
    public List<DeviceStateInfo> findByCabinetSN(String cabinetSN) {
        String hql = "from DeviceStateInfo where cabinetSN = :cabinetSN";
        Map<String, Object> params = new HashMap<>(1);
        params.put("cabinetSN",cabinetSN);
        return this.findList(hql,params);
    }

    @Override
    public List<DeviceStateInfo> findEmptyDevices(String cabinetSN) {
        String hql = "from DeviceStateInfo where cabinetSN = :cabinetSN and state=0";
        Map<String, Object> params = new HashMap<>(1);
        params.put("cabinetSN",cabinetSN);
        return this.findList(hql,params);
    }

    /**
     * 通过网关编号和设备编号查找对应的设备状态
     * @param cabinetSN
     * @param gridSN
     * @return
     */
    @Override
    public DeviceStateInfo findByCabinetSNAndGridSN(String cabinetSN, String gridSN) {
        String hql = "from DeviceStateInfo where cabinetSN = :cabinetSN and gridSN = :gridSN";
        Map<String, Object> params = new HashMap<>(2);
        params.put("cabinetSN",cabinetSN);
        params.put("gridSN",gridSN);
        return this.unique(hql, params);
    }

    /**
     * 通过orderId来查询某个柜子的状态
     * @param orderId   消费订单ID
     * @return DeviceStateInfo
     */
    @Override
    public DeviceStateInfo findByOrderId(String orderId) {
        String hql = "from DeviceStateInfo where orderId = :orderId";
        Map<String, Object> params = new HashMap<>(1);
        params.put("orderId",orderId);
        return this.unique(hql, params);
    }
}
