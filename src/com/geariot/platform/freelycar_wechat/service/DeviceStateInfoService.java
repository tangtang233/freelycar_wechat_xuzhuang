package com.geariot.platform.freelycar_wechat.service;

import com.alibaba.fastjson.JSONObject;
import com.geariot.platform.freelycar_wechat.dao.CabinetDao;
import com.geariot.platform.freelycar_wechat.dao.DeviceStateInfoDao;
import com.geariot.platform.freelycar_wechat.entities.Cabinet;
import com.geariot.platform.freelycar_wechat.entities.DeviceStateInfo;
import com.geariot.platform.freelycar_wechat.model.RESCODE;
import com.geariot.platform.freelycar_wechat.utils.DeviceStateThread;
import com.geariot.platform.freelycar_wechat.utils.JsonResFactory;
import com.geariot.platform.freelycar_wechat.wsutils.DeviceBean;
import com.geariot.platform.freelycar_wechat.wsutils.DeviceValue;
import com.geariot.platform.freelycar_wechat.wsutils.ResultBean;
import com.geariot.platform.freelycar_wechat.wsutils.WSClient;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author 唐炜
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DeviceStateInfoService {

    /**
     * 关门操作状态标识
     */
    public final static String OPEN_SUCCESS = "success";
    public final static String OPEN_FAILED = "failed";
    public final static String CLOSED = "closed";
    public final static String CLOSE_TIMEOUT = "timeout";
    public final static String DEVICE_OFFLINE = "offline";
    public final static String ALREADY_OPEN = "opened";


    private static Logger log = LogManager.getLogger(DeviceStateInfoService.class);
    @Autowired
    private DeviceStateInfoDao deviceStateInfoDao;

    @Autowired
    private CabinetDao cabinetDao;

    public DeviceStateInfo saveOrUpdate(DeviceStateInfo deviceStateInfo) {
        return deviceStateInfoDao.saveOrUpdate(deviceStateInfo);
    }

    public void addDeviceStateInfoByCabinetInfo(int cabinetId) {
        Cabinet cabinet = cabinetDao.findById(cabinetId);
        String sn = cabinet.getSn();
        Integer specification = cabinet.getSpecification();
        if (null == specification) {
            log.error("执行addDeviceStateInfoByCabinetInfo方法错误：查询不到指定网关的规格");
            return;
        }
        for (int i = 0; i < specification; i++) {
            DeviceStateInfo deviceStateInfo = new DeviceStateInfo();
            deviceStateInfo.setState(0);
            deviceStateInfo.setCabinetId(cabinetId);
            deviceStateInfo.setCabinetSN(sn);
            deviceStateInfo.setGridSN(String.valueOf(i + 1));
            deviceStateInfoDao.save(deviceStateInfo);
        }
    }

    /**
     * 添加柜子状态信息
     *
     * @param deviceStateInfo
     * @return
     */
    public String add(DeviceStateInfo deviceStateInfo) {
        if (null == deviceStateInfo) {
            return JsonResFactory.buildOrg(RESCODE.WRONG_PARAM).toString();
        }
        deviceStateInfo.setState(0);
        int id = deviceStateInfoDao.save(deviceStateInfo);
        return JsonResFactory.buildNetWithData(RESCODE.SUCCESS, id).toString();
    }

    /**
     * 修改柜子状态信息
     *
     * @param deviceStateInfo
     * @return
     */
    public String modifyById(DeviceStateInfo deviceStateInfo) {
        if (null == deviceStateInfo) {
            return JsonResFactory.buildOrg(RESCODE.WRONG_PARAM).toString();
        }
        Integer id = deviceStateInfo.getId();
        if (null == id) {
            return JsonResFactory.buildOrg(RESCODE.WRONG_PARAM).toString();
        }
        DeviceStateInfo oldDeviceStateInfo = deviceStateInfoDao.findById(id);
        oldDeviceStateInfo.setState(deviceStateInfo.getState());
        oldDeviceStateInfo.setLicensePlate(deviceStateInfo.getLicensePlate());
        oldDeviceStateInfo.setOrderId(deviceStateInfo.getOrderId());
        oldDeviceStateInfo.setReservationId(deviceStateInfo.getReservationId());
        deviceStateInfoDao.saveOrUpdate(oldDeviceStateInfo);
        return JsonResFactory.buildNetWithData(RESCODE.SUCCESS, oldDeviceStateInfo).toString();
    }

    /**
     * 获取一个随机的可用的柜子编号
     * 注：用于用户随机开门
     */
    private String getRandomDeviceId(String cabinetSN) {
        if (StringUtils.isEmpty(cabinetSN)) {
            log.error("执行getRandomDeviceId方法错误：参数cabinetSN为空值！");
            return null;
        }
        List<DeviceStateInfo> emptyDevicesList = deviceStateInfoDao.findEmptyDevices(cabinetSN);
        if (null != emptyDevicesList && !emptyDevicesList.isEmpty()) {
            int targetIndex;

            List<DeviceStateInfo> onlineEmptyDevices = new ArrayList<>();

            //获取远端设备状态
            Map<String,DeviceBean> remoteDeviceBeans = this.formatRemoteDevicesInfosToMap(cabinetSN);
            if (remoteDeviceBeans.isEmpty()) {
                log.error("IMEI编号： "+ cabinetSN +" 的远端设备状态查询失败！返回null");
                return null;
            }

            log.debug("开始遍历所有可分配柜子的实际柜门开关状态：");
            //遍历出所有可分配柜子的实际柜门开关状态，如果为“开”则排除；如果是离线的柜子，也做一下排除；
            for (DeviceStateInfo emptyDeviceStateInfo : emptyDevicesList) {
                //验证柜子是否离线
                String emptyDeviceId = emptyDeviceStateInfo.getCabinetSN() + "-" + emptyDeviceStateInfo.getGridSN();

                log.debug("开始分析远端状态。柜门编号：", emptyDeviceId);
                DeviceBean remoteDeviceInfo = remoteDeviceBeans.get(emptyDeviceId);
                    if (null != remoteDeviceInfo) {
                        //如果柜子在线（online）且柜门是关闭的（0），则符合要求
                        String heartBeat = remoteDeviceInfo.getHeartbeat();
                        String magne = remoteDeviceInfo.getMagne();
                        if (DeviceValue.HEARTBEAT_ONLINE.getValue().equals(heartBeat) && DeviceValue.MAGNE_CLOSE.getValue().equals(magne)) {
                            log.info(emptyDeviceId + " 符合 随机分配要求。");
                            onlineEmptyDevices.add(emptyDeviceStateInfo);
                        } else {
                            log.info(emptyDeviceId + " 不符合 随机分配要求。");
                            log.info("不符合的原因：");
                            if (DeviceValue.HEARTBEAT_OFFLINE.getValue().equals(heartBeat)) {
                                log.info(emptyDeviceId + "设备是离线的状态");
                                log.debug("heartBeat: " + heartBeat);
                            }
                            if (DeviceValue.MAGNE_OPEN.getValue().equals(magne)) {
                                log.info(emptyDeviceId + "设备是已经打开的状态");
                                log.debug("magne: " + magne);
                            }
                        }
                    }
                log.debug("分析结束。柜门编号：", emptyDeviceId);
            }

            int emptyDevicesCount = onlineEmptyDevices.size();
            if (emptyDevicesCount == 0) {
                log.info("没有可分配的智能柜！");
                return null;
            }

            if (emptyDevicesCount == 1) {
                targetIndex = 0;
            } else {
                Random random = new Random();
                targetIndex = random.nextInt(emptyDevicesCount);
            }
            DeviceStateInfo targetDeviceStateInfo = onlineEmptyDevices.get(targetIndex);
            if (null != targetDeviceStateInfo) {
                String gridSN = targetDeviceStateInfo.getGridSN();
                if (StringUtils.isEmpty(gridSN)) {
                    log.error("执行getRandomDeviceId方法错误：获取的设备编号为空值，请联系维护人员，谢谢！");
                    return null;
                }
                String deviceId = cabinetSN + "-" + gridSN;
                log.info("分配的柜门号：" + deviceId);
                return deviceId;
            }
        }
        return null;
    }

    /**
     * 打开一个随机的可用的门
     * 注：用于用户随机开门
     *
     * @param cabinetSN
     * @return
     */
    public JSONObject openRandomDeviceDoor(String cabinetSN) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("res", OPEN_FAILED);
        jsonObject.put("deviceId", null);
        jsonObject.put("msg", "柜门打开失败，请重试或联系技术人员。");
        if (StringUtils.isEmpty(cabinetSN)) {
            log.error("执行openRandomDeviceDoor方法错误：参数cabinetSN为空值！");
            return jsonObject;
        }
        String deviceId = this.getRandomDeviceId(cabinetSN);
        String openResult = openDeviceDoorByDeviceId(deviceId);
        if (!OPEN_FAILED.equals(openResult)) {
            jsonObject.put("res", openResult);
            jsonObject.put("deviceId", deviceId);
            switch (openResult) {
                case CLOSE_TIMEOUT:
                    jsonObject.put("msg", "关闭柜门超时。");
                    break;
                case ALREADY_OPEN:
                    jsonObject.put("msg", "柜门状态，请重试或联系技术人员。");
                    break;
                case DEVICE_OFFLINE:
                    jsonObject.put("msg", "设备离线，请重试或联系技术人员。");
                    break;
                default:
                    jsonObject.put("msg", "柜门打开失败，请重试或联系技术人员。");
            }
        }
        return jsonObject;
    }

    /**
     * 打开指定的柜门
     *
     * @param cabinetSN
     * @param gridSN
     * @return
     */
    public JSONObject openAppointedDeviceDoor(String cabinetSN, String gridSN) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("res", OPEN_FAILED);
        jsonObject.put("deviceId", null);
        jsonObject.put("msg", "柜门打开失败，请重试或联系技术人员。");
        if (StringUtils.isEmpty(cabinetSN)) {
            log.error("执行openRandomDeviceDoor方法错误：参数cabinetSN为空值！");
            return jsonObject;
        }
        if (StringUtils.isEmpty(gridSN)) {
            log.error("执行openRandomDeviceDoor方法错误：参数gridSN为空值！");
            return jsonObject;
        }
        String deviceId = cabinetSN + "-" + gridSN;
        return this.openAppointedDeviceDoor(deviceId);
    }

    /**
     * 打开指定的柜门
     *
     * @param deviceId
     * @return
     */
    public JSONObject openAppointedDeviceDoor(String deviceId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("res", OPEN_FAILED);
        jsonObject.put("deviceId", null);
        jsonObject.put("msg", "柜门打开失败，请重试或联系技术人员。");
        if (StringUtils.isEmpty(deviceId)) {
            log.error("执行openRandomDeviceDoor方法错误：参数deviceId为空值！");
            return jsonObject;
        }
        String openResult = openDeviceDoorByDeviceId(deviceId);
        if (!OPEN_FAILED.equals(openResult)) {
            jsonObject.put("res", openResult);
            jsonObject.put("deviceId", deviceId);
            switch (openResult) {
                case CLOSE_TIMEOUT:
                    jsonObject.put("msg", "关闭柜门超时。");
                    break;
                case ALREADY_OPEN:
                    jsonObject.put("msg", "柜门状态，请重试或联系技术人员。");
                    break;
                case DEVICE_OFFLINE:
                    jsonObject.put("msg", "设备离线，请重试或联系技术人员。");
                    break;
                default:
                    jsonObject.put("msg", "柜门打开失败，请重试或联系技术人员。");
            }
        }
        return jsonObject;
    }

    /**
     * 打开柜门，并启动关门监控线程
     *
     * @param deviceId
     * @return
     */
    private String openDeviceDoorByDeviceId(String deviceId) {
        if (StringUtils.isNotEmpty(deviceId)) {
            //验证柜子是否离线
            JSONObject resJSONObject = JSONObject.parseObject(WSClient.getDeviceStateByID(deviceId));
            if (WSClient.RESULT_SUCCESS.equalsIgnoreCase(resJSONObject.getString("res"))) {
                DeviceBean deviceBean = JSONObject.parseObject(resJSONObject.getJSONObject("value").toJSONString(), DeviceBean.class);
                if (null != deviceBean) {
                    if (DeviceValue.HEARTBEAT_OFFLINE.getValue().equals(deviceBean.getHeartbeat())) {
                        return DEVICE_OFFLINE;
                    }
                    if (DeviceValue.MAGNE_OPEN.getValue().equals(deviceBean.getMagne())) {
                        return ALREADY_OPEN;
                    }
                } else {
                    return OPEN_FAILED;
                }
            }

            //打开柜子
            String resString = WSClient.controlDevices(deviceId, "1");
            if (StringUtils.isEmpty(resString)) {
                return OPEN_FAILED;
            }
            JSONObject openDeviceResultJSONObject = JSONObject.parseObject(resString);
            //返回值为“1”，说明开门成功
            if (!WSClient.DOOR_STATE_OPEN.equals(openDeviceResultJSONObject.getString("value"))) {
                return OPEN_FAILED;
            }


            DeviceStateThread thread = new DeviceStateThread();
            thread.setDeviceId(deviceId);

            thread.start();
            //等待线程结束
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String endStatus = thread.getEndStatus();

            if (OPEN_SUCCESS.equals(endStatus)) {
                log.debug("task finished");
                return CLOSED;
            }
            log.debug("task time out,will terminate");
            return CLOSE_TIMEOUT;
        }
        return OPEN_FAILED;
    }

    /**
     * 通过网关编号和设备编号查找对应的设备状态
     *
     * @param cabinetSN
     * @param gridSN
     * @return
     */
    public DeviceStateInfo getDeviceStateInfoByCabinetSNAndGridSN(String cabinetSN, String gridSN) {
        if (StringUtils.isEmpty(cabinetSN)) {
            log.error("参数cabinetSN为空！");
            return null;
        }
        if (StringUtils.isEmpty(gridSN)) {
            log.error("参数gridSN为空！");
            return null;
        }
        return deviceStateInfoDao.findByCabinetSNAndGridSN(cabinetSN, gridSN);
    }

    /**
     * 查询某智能柜下所有柜子的使用状态
     *
     * @param cabinetSN
     * @return
     */
    public Map<String, Object> showDeviceStateInfo(String cabinetSN) {
        if (StringUtils.isEmpty(cabinetSN)) {
            log.error("参数cabinetSN为空！");
            return RESCODE.WRONG_PARAM.getJSONRES();
        }
        List<DeviceStateInfo> deviceStateInfos = deviceStateInfoDao.findByCabinetSN(cabinetSN);
        if (null != deviceStateInfos && !deviceStateInfos.isEmpty()) {
            return RESCODE.SUCCESS.getJSONRES(deviceStateInfos);
        }
        return RESCODE.NOT_FOUND.getJSONRES(deviceStateInfos);
    }

    /**
     * 通过orderId来查询某个柜子的状态
     *
     * @param orderId 消费订单ID
     * @return DeviceStateInfo
     */
    public DeviceStateInfo findByOrderId(String orderId) {
        if (StringUtils.isEmpty(orderId)) {
            return null;
        }
        return deviceStateInfoDao.findByOrderId(orderId);
    }


    /**
     * 格式化远端设备状态，转化成
     * @param gwNum IMEI编号（sn）
     * @return map
     */
    Map<String, DeviceBean> formatRemoteDevicesInfosToMap(String gwNum) {
        Map<String, DeviceBean> remoteDevicesInfo = new HashMap<>();

        //查询远端该设备编号下所有线路状态
        String remoteAllDevicesStateResult = WSClient.getAllDeviceStateByGWNum(gwNum);
        ResultBean resultBean = JSONObject.parseObject(remoteAllDevicesStateResult, ResultBean.class);
        if (null != resultBean) {
            String res = resultBean.getRes();
            if (WSClient.RESULT_SUCCESS.equalsIgnoreCase(res)) {
                String value = resultBean.getValue();
                List<DeviceBean> remoteDeviceBeans = JSONObject.parseArray(value, DeviceBean.class);
                if (null != remoteDeviceBeans && !remoteDeviceBeans.isEmpty()) {
                    for (DeviceBean remoteDeviceBean : remoteDeviceBeans) {
                        if (null != remoteDeviceBean && StringUtils.isNotEmpty(remoteDeviceBean.getId())) {
                            remoteDevicesInfo.put(remoteDeviceBean.getId(), remoteDeviceBean);
                        }
                    }
                }
            }
        }
        return remoteDevicesInfo;
    }
}
