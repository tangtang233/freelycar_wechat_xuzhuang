package com.geariot.platform.freelycar_wechat.service;

import com.alibaba.fastjson.JSONObject;
import com.geariot.platform.freelycar_wechat.dao.*;
import com.geariot.platform.freelycar_wechat.entities.*;
import com.geariot.platform.freelycar_wechat.model.RESCODE;
import com.geariot.platform.freelycar_wechat.utils.JsonResFactory;
import com.geariot.platform.freelycar_wechat.utils.SocketHelper;
import com.geariot.platform.freelycar_wechat.utils.WebSocket;
import org.apache.axis.utils.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author 唐炜
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ReservationService {

    private static Logger log = LogManager.getLogger(ReservationService.class);
    @Autowired
    private ReservationDao reservationDao;
    @Autowired
    private DeviceStateInfoService deviceStateInfoService;
    @Autowired
    private DeviceStateInfoDao deviceStateInfoDao;
    @Autowired
    private ProjectDao projectDao;
    @Autowired
    private CarDao carDao;
    @Autowired
    private CabinetDao cabinetDao;
    @Autowired
    private WebSocket WebSocket;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 进行一项预约
     *
     * @param reservation
     * @return jsonString
     */
    public String add(Reservation reservation) {
        if (null == reservation) {
            return JsonResFactory.buildOrg(RESCODE.WRONG_PARAM).toString();
        }

        //打开柜子，并等待柜门关上再操作数据
        String cabinetSN = reservation.getCabinetSN();
        if (StringUtils.isEmpty(cabinetSN)) {
            return JsonResFactory.buildOrg(RESCODE.WRONG_PARAM).toString();
        }

        //新增：判断是否超过上限，超过上限则不可以预约
        boolean isOverCount = isTheReservationCountGoOverTheTop(cabinetSN);
        if (isOverCount) {
            log.info("正在预约的订单数量超过上限。");
            return JsonResFactory.buildOrg(RESCODE.NOT_HAVE_EMPTY_GRID).toString();
        }

        log.debug("开始获取随机分配柜子。");
        JSONObject doorResult = deviceStateInfoService.openRandomDeviceDoor(cabinetSN);
        String deviceId = doorResult.getString("deviceId");
        log.debug("获取到分配的柜子编号：", deviceId);
        if (null == deviceId) {
            return JsonResFactory.buildOrg(RESCODE.NOT_HAVE_EMPTY_GRID, "msg", doorResult.getString("msg")).toString();
        }
        if (!DeviceStateInfoService.CLOSED.equals(doorResult.getString("res"))) {
            return JsonResFactory.buildOrg(RESCODE.REMOTE_OPERATION_FAILURE, "msg", doorResult.getString("msg")).toString();
        }
        String gridSN = deviceId.split("-")[1];

        //获取车辆类型
        String licensePlate = reservation.getLicensePlate();
        String carBrand = null;
        Car carInfo = carDao.findByLicense(licensePlate);
        if (null != carInfo) {
            carBrand = carInfo.getCarbrand();
        }

        //获取柜子别名
        Cabinet cabinet = cabinetDao.findBySn(cabinetSN);
        String cabinetName = null;
        if (null != cabinet) {
            cabinetName = cabinet.getName();
        }

        reservation.setCarBrand(carBrand);
        reservation.setCreateTime(new Date());
        reservation.setGridSN(gridSN);
        reservation.setState(0);
        reservation.setCabinetName(cabinetName);

        //若用户没有填写取车时间，则默认生成一个
        if (null == reservation.getPickUpTime()) {
            reservation.setPickUpTime(this.getClosedTime());
        }

        Reservation reservationRes = reservationDao.saveOrUpdate(reservation);
        if (null != reservationRes) {
            //添加柜子状态信息：设置状态位为“用户已预约”
            DeviceStateInfo deviceStateInfo = deviceStateInfoService.getDeviceStateInfoByCabinetSNAndGridSN(cabinetSN, gridSN);

            deviceStateInfo.setState(DeviceStateInfo.USER_RESERVATION);
            deviceStateInfo.setReservationId(reservationRes.getId());
            deviceStateInfo.setLicensePlate(licensePlate);
            deviceStateInfoDao.update(deviceStateInfo);

            //推送websocket消息：告知客户端有订单的变动，前端进行页面数据刷新
            try {
                WebSocket.sendMessageAll("{\"message\":{\"type\":\"orderChanged\"}}");
                log.debug("有用户进行了一项预约。");
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            //推送websocket消息：告知客户端有订单的变动，前端进行页面数据刷新
            try {
                WebSocket.sendMessageAll("{\"message\":{\"type\":\"newReservation\"}}");
                log.debug("有用户进行了一项预约。");
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            return JsonResFactory.buildNetWithData(RESCODE.SUCCESS, reservationRes).toString();
        }

        return JsonResFactory.buildOrg(RESCODE.NOT_FOUND).toString();
    }

    /**
     * 取消预约
     *
     * @param reservationId
     * @return
     */
    public String cancel(Integer reservationId) {
        if (null == reservationId) {
            return JsonResFactory.buildOrg(RESCODE.WRONG_PARAM).toString();
        }
        Reservation reservation = reservationDao.findById(reservationId);
        if (null == reservation) {
            return JsonResFactory.buildOrg(RESCODE.NOT_FOUND).toString();
        }

        //打开柜子，并等待柜门关上再操作数据
        String cabinetSN = reservation.getCabinetSN();
        String gridSN = reservation.getGridSN();
        if (StringUtils.isEmpty(cabinetSN) || StringUtils.isEmpty(gridSN)) {
            return JsonResFactory.buildOrg(RESCODE.WRONG_PARAM).toString();
        }
        JSONObject doorResult = deviceStateInfoService.openAppointedDeviceDoor(cabinetSN, gridSN);
        if (!DeviceStateInfoService.CLOSED.equals(doorResult.getString("res"))) {
            return JsonResFactory.buildOrg(RESCODE.REMOTE_OPERATION_FAILURE, "msg", doorResult.getString("msg")).toString();
        }

        reservation.setState(Reservation.USER_CANCEL);
        Reservation reservationRes = reservationDao.saveOrUpdate(reservation);

        //更新柜子的数据：设置状态位为“用户用户已取消预约”
        DeviceStateInfo deviceStateInfo = deviceStateInfoService.getDeviceStateInfoByCabinetSNAndGridSN(cabinetSN, gridSN);
        deviceStateInfo.setState(DeviceStateInfo.EMPTY);
        deviceStateInfo.setReservationId(null);
        deviceStateInfo.setLicensePlate(null);
        deviceStateInfoDao.update(deviceStateInfo);

        //推送websocket消息：告知客户端有订单的变动，前端进行页面数据刷新
        try {
            WebSocket.sendMessageAll("{\"message\":{\"type\":\"orderChanged\"}}");
            log.debug("有用户取消了一项预约。");
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        return JsonResFactory.buildNetWithData(RESCODE.SUCCESS, reservationRes).toString();
    }

    /**
     * 查找预约列表
     *
     * @param paramMap
     * @param page
     * @param number
     * @return
     */
    public Map<String, Object> list(Map<String, Object> paramMap, Integer page, Integer number) {
        //如果没有分页参数，则全部查询出来
        if (null == page || null == number) {
            List<Reservation> list = reservationDao.queryAll(paramMap);
            if (null == list || list.isEmpty()) {
                return RESCODE.NOT_FOUND.getJSONRES();
            }
            return RESCODE.SUCCESS.getJSONRES(list);
        }
        int from = (page - 1) * number;
        List<Reservation> list = reservationDao.query(paramMap, from, number);
        if (null == list || list.isEmpty()) {
            return RESCODE.NOT_FOUND.getJSONRES();
        }

        long count = reservationDao.getReservationCount(paramMap);
        int size = (int) Math.ceil(count / (double) number);
        return RESCODE.SUCCESS.getJSONRES(list, size, count);
    }

    /**
     * 根据id查找某条预约数据
     *
     * @param id
     * @return
     */
    public Map<String, Object> findById(Integer id) {
        if (null != id) {
            Reservation reservation = reservationDao.findById(id);
            //返回预约数据前，需要将状态位替换为“已读”
            if (null != reservation) {
                Reservation res = reservationDao.saveOrUpdate(reservation);
                return RESCODE.SUCCESS.getJSONRES(res);
            }
        }
        return RESCODE.NOT_FOUND.getJSONRES();
    }

    /**
     * 加载“本次预约订单”
     *
     * @param openId 用户openId
     * @return json/map
     */
    public Map<String, Object> loadTheBookingOrder(String openId) {
        if (StringUtils.isEmpty(openId)) {
            return RESCODE.WRONG_PARAM.getJSONRES();
        }
        List<Reservation> results = reservationDao.loadTheBookingOrder(openId);
        if (null != results && !results.isEmpty()) {
            Reservation reservationRes = results.get(0);
            List<Project> projects = this.getProjectsWithReservation(reservationRes);
            reservationRes.setProjects(projects);

            return RESCODE.SUCCESS.getJSONRES(reservationRes);
        }
        return RESCODE.NO_RECORD.getJSONRES();
    }

    /**
     * 加载“本次预约订单”
     *
     * @param clientId 用户clientId
     * @return json/map
     */
    public Map<String, Object> loadTheBookingOrderByClientId(String clientId) {
        if (StringUtils.isEmpty(clientId)) {
            return RESCODE.WRONG_PARAM.getJSONRES();
        }
        List<Reservation> results = reservationDao.loadTheBookingOrderByClientId(clientId);
        if (null != results && !results.isEmpty()) {
            Reservation reservationRes = results.get(0);
            List<Project> projects = this.getProjectsWithReservation(reservationRes);

            float totalPrice = 0;
            for (Project project : projects) {
                totalPrice += project.getPrice();
            }

            reservationRes.setProjects(projects);
            reservationRes.setTotalPrice(totalPrice);

            return RESCODE.SUCCESS.getJSONRES(reservationRes);
        }
        return RESCODE.NO_RECORD.getJSONRES();
    }

    /**
     * 将serviceIds转换为Project的集合
     *
     * @param reservation 预约的实体对象
     * @return list
     */
    public List<Project> getProjectsWithReservation(Reservation reservation) {
        List<Project> res = new ArrayList<>();
        String serviceIds = reservation.getServiceIds();
        if (StringUtils.isEmpty(serviceIds)) {
            return res;
        }
        String[] projectIds = serviceIds.split(",");
        for (String projectId : projectIds) {
            if (!StringUtils.isEmpty(projectId)) {
                Project project = projectDao.findProjectById(Integer.parseInt(projectId));
                if (null != project) {
                    res.add(project);
                }
            }
        }
        return res;
    }

    /**
     * 设置当日18时为默认取车时间
     *
     * @return
     */
    public Date getClosedTime() {
        String closingTimeStr = "18:00:00";
        Date currentDate = new Date();
        //获取默认选中的日期的年月日星期的值，并赋值
        //日历对象
        Calendar calendar = Calendar.getInstance();
        //设置当前日期
        calendar.setTime(currentDate);

        //获取年份
        String yearStr = calendar.get(Calendar.YEAR) + "";
        //获取月份
        int month = calendar.get(Calendar.MONTH) + 1;
        String monthStr = month < 10 ? "0" + month : month + "";
        //获取日
        int day = calendar.get(Calendar.DATE);
        String dayStr = day < 10 ? "0" + day : day + "";

        closingTimeStr = yearStr + "-" + monthStr + "-" + dayStr + " " + closingTimeStr;

        try {
            Date returnDate = simpleDateFormat.parse(closingTimeStr);
            return returnDate;
        } catch (ParseException e) {
            log.error("生成默认取车时间失败。", e);
            e.printStackTrace();
        }

        return currentDate;
    }

    /**
     * 判断正在进行的预约（state为：0，1，2）是否超过一个值，这样可以避免多个订单的钥匙未取回时，没有格子供后续完工的格子去
     *
     * @param cabinetSN 智能柜编号
     * @return 是否超过上限（true/false）
     */
    public boolean isTheReservationCountGoOverTheTop(String cabinetSN) {
        if (StringUtils.isEmpty(cabinetSN)) {
            log.error("指定智能柜预约是否超过上限判断失败，原因：参数cabinetSN为空值，默认将返回true，预约将终止。");
            return true;
        }
        long count = reservationDao.theCountInProgress(cabinetSN);

        //从数据库里获取同时服务的上限数值，如果出错，则为0，默认停止这个柜子的所有新预约
        int limitCount = 0;
        Cabinet cabinet = cabinetDao.findBySn(cabinetSN);
        if (null != cabinet) {
            limitCount = cabinet.getServiceCount();
        }
        log.info("limitCount： " + limitCount);
        if (count < limitCount) {
            log.info("智能柜 " + cabinetSN + " 正在进行的预约服务有 " + count + " 条，将返回false，允许新的预约。");
            return false;
        }
        return true;
    }
}
