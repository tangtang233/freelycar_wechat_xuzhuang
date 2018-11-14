package com.geariot.platform.freelycar_wechat.service;

import com.geariot.platform.freelycar_wechat.dao.*;
import com.geariot.platform.freelycar_wechat.entities.*;
import com.geariot.platform.freelycar_wechat.model.RESCODE;
import com.geariot.platform.freelycar_wechat.utils.Constants;
import com.geariot.platform.freelycar_wechat.utils.IDGenerator;
import com.geariot.platform.freelycar_wechat.utils.JsonResFactory;
import com.geariot.platform.freelycar_wechat.utils.WebSocket;
import com.geariot.platform.freelycar_wechat.utils.query.OrderBean;
import com.geariot.platform.freelycar_wechat.wxutils.WechatTemplateMessage;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

@Service
@Transactional
public class ConsumOrderService {
    private static final Logger log = LogManager.getLogger(ConsumOrderService.class);
    @Autowired
    private ConsumOrderDao consumOrderDao;
    @Autowired
    private WXPayOrderDao wxPayOrderDao;
    @Autowired
    private WXUserDao wxUserDao;
    @Autowired
    private ClientDao clientDao;
    @Autowired
    private CardDao cardDao;
    @Autowired
    private DeviceStateInfoService deviceStateInfoService;
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private ReservationDao reservationDao;
    @Autowired
    private CarDao carDao;
    @Autowired
    private StoreDao storeDao;
    @Autowired
    private StaffDao staffDao;
    @Autowired
    private IncomeOrderDao incomeOrderDao;

    @Autowired
    private WebSocket WebSocket;

    // 根据前端之前获取的单据ID调用接口，不需要验证单据不存在
    public String detail(String consumOrderId) {
        ConsumOrder consumOrder = consumOrderDao.findById(consumOrderId);
        if (consumOrder == null) {
            return JsonResFactory.buildOrg(RESCODE.NO_RECORD).toString();
        }
        Set<ProjectInfo> projects = consumOrder.getProjects();
        List<OrderBean> projectsForRemaining = new ArrayList<OrderBean>();
        if (projects != null && !projects.isEmpty()) {
            for (ProjectInfo project : projects) {
                String cardId = project.getCardId();
                CardProjectRemainingInfo cardProjectRemainingInfo = null;
                if (!StringUtils.isEmpty(cardId)) {
                    cardProjectRemainingInfo = cardDao.getProjectRemainingInfo(Integer.parseInt(project.getCardId()), project.getProjectId());
                }
                int remaining = 0;
                if (cardProjectRemainingInfo != null) {
                    remaining = cardProjectRemainingInfo.getRemaining();
                }
                OrderBean orderBean = new OrderBean();
                orderBean.setRemaining(remaining);
                orderBean.setProjectInfo(project);
                projectsForRemaining.add(orderBean);
            }
        }
        System.out.println("<<<<<" + consumOrder);
        JsonConfig config = JsonResFactory.dateConfig();
        config.registerPropertyExclusions(ConsumOrder.class, new String[]{"projects"});
        JSONObject obj = JSONObject.fromObject(consumOrderDao.findById(consumOrderId), config);
        obj.put("projects", projectsForRemaining);
        System.out.println("<<<<<" + obj);
        return JsonResFactory.buildOrg(RESCODE.SUCCESS, Constants.RESPONSE_CONSUMORDER_KEY, obj).toString();

    }

    public String listConsumOrder(int clientId, int page, int number) {
        int from = (page - 1) * number;
        List<ConsumOrder> exist = consumOrderDao.listByClientId(clientId, from, number);
        if (exist == null || exist.isEmpty()) {
            return JsonResFactory.buildOrg(RESCODE.NOT_FOUND).toString();
        }
        long realSize = consumOrderDao.getCountByClientId(clientId);
        int size = (int) Math.ceil(realSize / number);
        JsonConfig config = JsonResFactory.dateConfig();
        JSONArray jsonArray = JSONArray.fromObject(exist, config);
        net.sf.json.JSONObject obj = JsonResFactory.buildNetWithData(RESCODE.SUCCESS, jsonArray);
        obj.put(Constants.RESPONSE_SIZE_KEY, size);
        obj.put(Constants.RESPONSE_REAL_SIZE_KEY, realSize);
        return obj.toString();
    }

    public String detailWXPayOrder(String wxPayOrderId) {
        WXPayOrder wxPayOrder = wxPayOrderDao.findById(wxPayOrderId);
        if (wxPayOrder == null) {
            return JsonResFactory.buildOrg(RESCODE.NO_RECORD).toString();
        } else {
            WXUser wxUser = wxUserDao.findUserByOpenId(wxPayOrder.getOpenId());
            net.sf.json.JSONObject obj = JsonResFactory.buildNetWithData(RESCODE.SUCCESS, JSONObject.fromObject(wxPayOrder, JsonResFactory.dateConfig(WXPayOrder.class)));
            obj.put(Constants.RESPONSE_WXUSER_KEY, wxUser.getName() == null ? wxUser.getNickName() : wxUser.getName());
            return obj.toString();
        }
    }

    public String listWXPayOrder(int clientId, int page, int number) {
        WXUser wxUser = wxUserDao.findUserByPhone(clientDao.findById(clientId).getPhone());
        if (wxUser == null) {
            return JsonResFactory.buildOrg(RESCODE.NO_RECORD).toString();
        }
        //String openId = wxUser.getOpenId();
        int from = (page - 1) * number;
        List<WXPayOrder> exist = wxPayOrderDao.listByClientId(clientId, from, number);
        if (exist == null || exist.isEmpty()) {
            return JsonResFactory.buildOrg(RESCODE.NOT_FOUND).toString();
        }
        long realSize = wxPayOrderDao.getCountByClientId(clientId);
        int size = (int) Math.ceil(realSize / number);
        // JsonConfig config = new JsonConfig();
        // config.registerJsonValueProcessor(Date.class, new
        // DateJsonValueProcessor());
        JsonConfig config = JsonResFactory.dateConfig(Collection.class);
        JSONArray jsonArray = JSONArray.fromObject(exist, config);
        net.sf.json.JSONObject obj = JsonResFactory.buildNetWithData(RESCODE.SUCCESS, jsonArray);
        obj.put(Constants.RESPONSE_SIZE_KEY, size);
        obj.put(Constants.RESPONSE_REAL_SIZE_KEY, realSize);
        return obj.toString();
    }

    public String comment(String consumOrderId, String comment, int stars) {
        log.debug("订单编号:" + consumOrderId + ",评论内容:" + comment + ",星级:" + stars);
        ConsumOrder consumOrder = consumOrderDao.findById(consumOrderId);
        consumOrder.setComment(comment);
        consumOrder.setStars(stars);
        consumOrder.setCommentDate(new Date());
        Client client = clientDao.findById(consumOrder.getClientId());
        WXUser wxUser = wxUserDao.findUserByPhone(client.getPhone());
        consumOrder.setHeadimgurl(wxUser.getHeadimgurl());
        return JsonResFactory.buildNet(RESCODE.SUCCESS).toString();

    }

    public Map<String, Object> modify(ConsumOrder consumOrder) {
        ConsumOrder updateOrderConsutom = consumOrderDao.updateOrderConsutom(consumOrder);
        Map<String, Object> jsonres = RESCODE.SUCCESS.getJSONRES();
        jsonres.put("id", updateOrderConsutom.getId());
        return jsonres;
    }

    public Map<String, Object> list(Map<String, Object> paramMap, int page, int number) {
        int from = (page - 1) * number;
        List<ConsumOrder> list = consumOrderDao.list(paramMap, from, number);
        if (null == list || list.isEmpty()) {
            return RESCODE.NOT_FOUND.getJSONRES();
        }

        long count = consumOrderDao.getConsumOrderCount(paramMap);
        int size = (int) Math.ceil(count / (double) number);
        return RESCODE.SUCCESS.getJSONRES(list, size, count);
    }

    /**
     * @param consumOrder
     * @return
     */
    public Map<String, Object> billing(ConsumOrder consumOrder) {
        log.debug("客户id：" + consumOrder.getClientId() + ", 客户姓名:" + consumOrder.getClientName() + ", 尝试创建消费订单");
        log.debug("开单详情:" + consumOrder);
        String consumOrderId = IDGenerator.generate(IDGenerator.MAINTAIN_CONSUM);
        consumOrder.setId(consumOrderId);
        Date tempDate = new Date();
        if (consumOrder.getPickTime() == null) {
            consumOrder.setPickTime(tempDate);
        }
        consumOrder.setCreateDate(tempDate);

		/*Set<ConsumExtraInventoriesInfo> infos = consumOrder.getInventoryInfos();
		// 比较消耗数量与库存实际数量
		for (ConsumExtraInventoriesInfo info : infos) {
			Inventory inventory = inventoryDao.findById(info.getInventory().getId());
			log.debug("订单需要消耗库存(id:" + inventory.getId() + ", 名称：" + inventory.getName() + ")总计" + info.getNumber()
					+ inventory.getStandard());
			log.debug("实际库存剩余：" + inventory.getAmount() + inventory.getStandard());
			// 如果库存不足，抛出异常，操作回滚
			if (inventory.getAmount() < info.getNumber()) {
				log.debug("实际库存不足，当前操作回滚，订单添加失败");
				throw new ForRollbackException(RESCODE.INVENTORY_NOT_ENOUGH.getMsg(),
						RESCODE.INVENTORY_NOT_ENOUGH.getValue());
			}
		}*/

        //项目预处理
        Set<ProjectInfo> preHandle = consumOrder.getProjects();
        for (ProjectInfo projectInfo : preHandle) {
            projectInfo.setPresentPrice(projectInfo.getPrice());
            projectInfo.setClientId(consumOrder.getClientId());
        }
        this.consumOrderDao.save(consumOrder);
        log.debug("消费订单(id:" + consumOrder.getId() + ")保存成功，准备创建出库订单并保存");
        Map<String, Object> map = RESCODE.SUCCESS.getJSONRES();
        map.put("id", consumOrderId);
        return map;
    }

    /**
     * 技师取出钥匙去进行服务
     *
     * @param reservationId
     * @param deviceId
     * @param staffId
     * @return
     */
    public Map<String, Object> getKeyToService(Integer reservationId, String deviceId, Integer staffId) {
        if (null == reservationId) {
            log.error("技师取钥匙时，开柜失败。原因：参数reservationId为空值。");
            return RESCODE.WRONG_PARAM.getJSONRES();
        }
        if (null == staffId) {
            log.error("技师取钥匙时，开柜失败。原因：参数staffId为空值。");
            return RESCODE.WRONG_PARAM.getJSONRES();
        }
        if (StringUtils.isEmpty(deviceId)) {
            log.error("技师取钥匙时，开柜失败。原因：参数deviceId为空值。");
            return RESCODE.WRONG_PARAM.getJSONRES();
        }
        //查询预约表数据
        Reservation reservation = reservationDao.findById(reservationId);
        if (null == reservation) {
            log.error("技师取钥匙时，开柜失败。原因：数据reservation对象没有找到。");
            return RESCODE.NOT_FOUND.getJSONRES();
        }
        //打开柜子，并等待柜门关上再操作数据
        com.alibaba.fastjson.JSONObject doorResult = deviceStateInfoService.openAppointedDeviceDoor(deviceId);
        if (!DeviceStateInfoService.CLOSED.equals(doorResult.getString("res"))) {
            return RESCODE.REMOTE_OPERATION_FAILURE.getJSONRES();
        }
        String cabinetSN = deviceId.split("-")[0];
        String gridSN = deviceId.split("-")[1];
        //预约状态更改
        reservation.setState(Reservation.IS_THE_SERVICE);
        //获取客户表id
        Integer clientId = reservation.getClientId();
        String licensePlate = reservation.getLicensePlate();
        try {
            //获取客户表信息
            Client client = clientDao.findById(clientId);
            //获取车辆信息
            Car car = carDao.findByLicense(licensePlate);
            //创建订单数据
            ConsumOrder consumOrder = new ConsumOrder();
            Date currentDate = new Date();
            consumOrder.setCarBrand(car.getCarbrand());
            consumOrder.setCarId(car.getId());
            consumOrder.setCarType(car.getCartype());
            consumOrder.setClientId(client.getId());
            consumOrder.setClientName(client.getTrueName());
            consumOrder.setGender(client.getGender());
            consumOrder.setLicensePlate(licensePlate);
            consumOrder.setParkingLocation(reservation.getLocation());
            consumOrder.setCreateDate(currentDate);
            consumOrder.setPayMethod(String.valueOf(Constants.PROJECT_WITH_CARD));
            consumOrder.setPayState(Constants.PAY_UNPAY);
            //设置状态为已接车
            consumOrder.setState(Integer.valueOf(Constants.CAR_SERVICE_START));
            consumOrder.setStars(0);
            //设置服务类别（暂时固定为“美容”）
            consumOrder.setProgramId(1);
            consumOrder.setProgramName("美容");
            //设置服务门店（暂时默认为金奥店）
            consumOrder.setStore(storeDao.findStoreById(1));
            //设定接车人员
            consumOrder.setPickCarStaff(staffDao.findStaffByStaffId(staffId));
            //备注设置为“智能柜自动开单”
            consumOrder.setComment("智能柜自动开单");
            //订单数据中添加一个“预约ID”
            consumOrder.setReservationId(reservationId);
            //设置服务项目
            float totalPrice = 0;
            Set<ProjectInfo> projectInfos = new HashSet<>();
            List<Project> projectList = reservationService.getProjectsWithReservation(reservation);
            for (Project project : projectList) {
                float price = project.getPrice();
                ProjectInfo projectInfo = new ProjectInfo();
                projectInfo.setBrandName(car.getCarbrand());
                projectInfo.setClientName(client.getTrueName());
                projectInfo.setCreateDate(currentDate);
                projectInfo.setLicensePlate(licensePlate);
                projectInfo.setName(project.getName());
                projectInfo.setPrice(price);
                projectInfo.setPresentPrice(project.getPrice());
                projectInfo.setPricePerUnit(project.getPricePerUnit());
                projectInfo.setProjectId(project.getId());
                projectInfo.setClientId(clientId);
                projectInfos.add(projectInfo);
                totalPrice += price;
            }
            consumOrder.setProjects(projectInfos);
            consumOrder.setTotalPrice(totalPrice);
            consumOrder.setPresentPrice(totalPrice);
            consumOrder.setActualPrice(totalPrice);
            log.debug("客户id：" + consumOrder.getClientId() + ", 客户姓名:" + consumOrder.getClientName() + ", 尝试创建消费订单");
            log.debug("开单详情:" + consumOrder);
            String consumOrderId = IDGenerator.generate(IDGenerator.MAINTAIN_CONSUM);
            consumOrder.setId(consumOrderId);
            if (consumOrder.getPickTime() == null) {
                consumOrder.setPickTime(currentDate);
            }
            Set<ProjectInfo> preHandle = consumOrder.getProjects();
            for (ProjectInfo projectInfo : preHandle) {
                projectInfo.setPresentPrice(projectInfo.getPrice());
                projectInfo.setClientId(consumOrder.getClientId());
            }
            this.consumOrderDao.save(consumOrder);
            log.debug("消费订单(id:" + consumOrder.getId() + ")保存成功，准备创建出库订单并保存");
            //设置智能柜状态表标记位
            DeviceStateInfo deviceStateInfo = deviceStateInfoService.getDeviceStateInfoByCabinetSNAndGridSN(cabinetSN, gridSN);
            deviceStateInfo.setState(DeviceStateInfo.EMPTY);
            deviceStateInfo.setOrderId(null);
            deviceStateInfo.setReservationId(null);
            deviceStateInfo.setLicensePlate(null);
            deviceStateInfoService.modifyById(deviceStateInfo);
            //预约数据更新
            reservation.setConsumOrderId(consumOrderId);
            reservation.setOrderTime(currentDate);
            reservationDao.saveOrUpdate(reservation);

            //推送websocket消息：告知客户端有订单的变动，前端进行页面数据刷新
            try {
                WebSocket.sendMessageAll("{\"message\":{\"type\":\"orderChanged\"}}");
                log.debug("技师取出钥匙去进行服务。");
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            try {
                //推送模板消息给用户
                WXUser wxUser = wxUserDao.findUserByPhone(client.getPhone());
                WechatTemplateMessage.orderChanged(consumOrder, wxUser.getOpenId());
            } catch (Exception e) {
                log.error("推送模板消息失败！", e);
            }
        } catch (Exception e) {
            log.error("技师取钥匙时，自动开单失败。", e);
            //更新预约表状态
            reservationDao.saveOrUpdate(reservation);

            return RESCODE.SUCCESS.getJSONRES("技师取车成功，但自动开单失败！");
        }
        return RESCODE.SUCCESS.getJSONRES("技师取车成功，且自动开单成功！");
    }

    /**
     * 服务结束，技师去存钥匙
     */
    public Map<String, Object> serviceFinish(String orderId, String cabinetSN, String parkingLocation) {
        if (null == orderId) {
            log.error("技师取钥匙时，开柜失败。原因：参数orderId为空值。");
            return RESCODE.WRONG_PARAM.getJSONRES();
        }
        if (StringUtils.isEmpty(cabinetSN)) {
            log.error("技师取钥匙时，开柜失败。原因：参数cabinetSN为空值。");
            return RESCODE.WRONG_PARAM.getJSONRES();
        }
        if (StringUtils.isEmpty(parkingLocation)) {
            log.error("技师取钥匙时，开柜失败。原因：参数parkingLocation为空值。");
            return RESCODE.WRONG_PARAM.getJSONRES();
        }
        //查询预约表数据
        Reservation reservation = reservationDao.findByConsumOrderId(orderId);
        if (null == reservation) {
            log.error("技师取钥匙时，开柜失败。原因：数据reservation对象没有找到。");
            return RESCODE.NOT_FOUND.getJSONRES();
        }
        //查询订单表数据
        ConsumOrder consumOrder = consumOrderDao.findById(orderId);
        if (null == consumOrder) {
            log.error("技师取钥匙时，开柜失败。原因：数据consumOrder对象没有找到。");
            return RESCODE.NOT_FOUND.getJSONRES();
        }
        //打开柜子，并等待柜门关上再操作数据
        com.alibaba.fastjson.JSONObject doorResult = deviceStateInfoService.openRandomDeviceDoor(cabinetSN);
        String deviceId = doorResult.getString("deviceId");
        if (null == deviceId) {
            return RESCODE.NOT_HAVE_EMPTY_GRID.getJSONRES(doorResult.getString("msg"));
        }
        if (!DeviceStateInfoService.CLOSED.equals(doorResult.getString("res"))) {
            return RESCODE.REMOTE_OPERATION_FAILURE.getJSONRES(doorResult.getString("msg"));
        }
        String gridSN = deviceId.split("-")[1];
        try {
            //预约状态更改
            Integer reservationId = reservation.getId();
            reservation.setState(Reservation.CAN_PICK_UP);
            reservationDao.saveOrUpdate(reservation);
            Date currentDate = new Date();
            //智能柜状态表状态更新
            DeviceStateInfo deviceStateInfo = deviceStateInfoService.getDeviceStateInfoByCabinetSNAndGridSN(cabinetSN, gridSN);
            deviceStateInfo.setReservationId(reservationId);
            deviceStateInfo.setOrderId(orderId);
            deviceStateInfo.setLicensePlate(reservation.getLicensePlate());
            deviceStateInfo.setState(DeviceStateInfo.STAFF_FINISH);
            deviceStateInfoService.saveOrUpdate(deviceStateInfo);
            //订单的状态更新
            consumOrder.setState(Integer.valueOf(Constants.CAR_SERVICE_COMPLETE));
            consumOrder.setFinishTime(currentDate);
            consumOrder.setParkingLocation(parkingLocation);
            consumOrderDao.saveOrUpdate(consumOrder);

            //推送websocket消息：告知客户端有订单的变动，前端进行页面数据刷新
            try {
                WebSocket.sendMessageAll("{\"message\":{\"type\":\"orderChanged\"}}");
                log.debug("服务结束，技师去存钥匙。");
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            try {
                //推送模板消息给用户
                //获取客户表信息
                Client client = clientDao.findById(reservation.getClientId());
                WXUser wxUser = wxUserDao.findUserByPhone(client.getPhone());
                WechatTemplateMessage.orderChanged(consumOrder, wxUser.getOpenId());
            } catch (Exception e) {
                log.error("推送模板消息失败！", e);
            }
        } catch (Exception e) {
            log.error("技师存钥匙失败！", e);
            return RESCODE.UPDATE_ERROR.getJSONRES("技师存钥匙失败！");
        }
        return RESCODE.SUCCESS.getJSONRES("技师存钥匙成功");
    }

    /**
     * 订单结算页面（会员卡）
     *
     * @param consumOrderId
     * @return
     */
    public String getOrderById(String consumOrderId) {
        ConsumOrder order = this.consumOrderDao.findById(consumOrderId);
        if (order == null) {
            return JsonResFactory.buildOrg(RESCODE.NOT_FOUND).toString();
        }
        Set<ProjectInfo> projectInfos = order.getProjects();
        Set<Card> cards = clientDao.findById(order.getClientId()).getCards();
        Set<Card> canUseCards = new HashSet<>();
        //筛选会员卡（计次卡需要包含本次服务，且剩余次数大于0；储值卡只要是余额大于0即可）
        for (Card card : cards) {
            com.geariot.platform.freelycar_wechat.entities.Service service = card.getService();
            if (null != service) {
                Integer serviceType = service.getType();
                //0,1,2=次卡,组合卡,储值卡
                switch (serviceType) {
                    case 0:
                        //卡里可抵扣的项目与服务项目相同，且剩余次数不为0
                        Set<CardProjectRemainingInfo> cardProjectRemainingInfos = card.getProjectInfos();
                        for (ProjectInfo projectInfo : projectInfos) {
                            for (CardProjectRemainingInfo cardProjectRemainingInfo : cardProjectRemainingInfos) {
                                if (null != cardProjectRemainingInfo) {
                                    int projectId = cardProjectRemainingInfo.getProject().getId();
                                    if (projectId == projectInfo.getProjectId() && cardProjectRemainingInfo.getRemaining() > 0) {
                                        canUseCards.add(card);
                                        break;
                                    }
                                }
                            }
                        }
                        break;
                    case 1:
                        //组合卡暂时不筛选
                        break;
                    case 2:
                        //卡里余额大于此次服务费用
                        if (card.getBalance() > order.getTotalPrice()) {
                            canUseCards.add(card);
                        }
                        break;
                    default:
                }
            }
        }


        List<Ticket> tickets = clientDao.findById(order.getClientId()).getTickets();
        JsonConfig config = JsonResFactory.dateConfig();
        config.registerPropertyExclusion(Project.class, "inventoryInfos");
        config.registerPropertyExclusion(Admin.class, "password");
        JSONObject jsonObject = JsonResFactory.buildNetWithData(RESCODE.SUCCESS, net.sf.json.JSONObject.fromObject(order, config));
        jsonObject.put("card", JSONArray.fromObject(canUseCards, config));
        jsonObject.put("ticket", JSONArray.fromObject(tickets, config));
        return jsonObject.toString();
    }

    /**
     * 用户支付完毕后取出钥匙
     *
     * @param orderId 订单编号
     * @return json
     */
    public Map<String, Object> userTakeOutKey(String orderId) {
        if (StringUtils.isEmpty(orderId)) {
            log.error("用户结算后取回钥匙时，开柜失败。原因：参数orderId为空值。");
            return RESCODE.WRONG_PARAM.getJSONRES("用户结算后取回钥匙时，开柜失败。原因：参数orderId为空值。");
        }
        //查询是否有对应的订单，且是否支付状态已经是“已结算”
        ConsumOrder consumOrder = consumOrderDao.findById(orderId);
        if (null == consumOrder) {
            log.error("用户结算后取回钥匙时，开柜失败。原因：数据consumOrder对象没有找到。");
            return RESCODE.NOT_FOUND.getJSONRES("用户结算后取回钥匙时，开柜失败。原因：数据consumOrder对象没有找到。");
        }

        Integer payState = consumOrder.getPayState();
        DeviceStateInfo deviceStateInfo = deviceStateInfoService.findByOrderId(orderId);
        if (null == deviceStateInfo) {
            log.error("用户结算后取回钥匙时，开柜失败。原因：数据deviceStateInfo对象没有找到。");
            return RESCODE.NOT_FOUND.getJSONRES("用户结算后取回钥匙时，开柜失败。原因：数据deviceStateInfo对象没有找到。");
        }
        Integer deviceState = deviceStateInfo.getState();
        if (deviceState != DeviceStateInfo.STAFF_FINISH) {
            log.error("用户结算后取回钥匙时，开柜失败。原因：对应柜子状态不为“服务完毕（" + DeviceStateInfo.STAFF_FINISH + "）”状态。");
            return RESCODE.CREATE_ERROR.getJSONRES("用户结算后取回钥匙时，开柜失败。原因：对应柜子状态不为“服务完毕（" + DeviceStateInfo.STAFF_FINISH + "）”状态。");
        }

        String cabinetSN = deviceStateInfo.getCabinetSN();
        String gridSN = deviceStateInfo.getGridSN();

        //payState为1，则表明已经结算，可以执行打开柜子的操作
        if (payState == 1) {
            //打开柜子，并等待柜门关上再操作数据
            com.alibaba.fastjson.JSONObject doorResult = deviceStateInfoService.openAppointedDeviceDoor(cabinetSN, gridSN);
            if (!DeviceStateInfoService.CLOSED.equals(doorResult.getString("res"))) {
                return RESCODE.REMOTE_OPERATION_FAILURE.getJSONRES(doorResult.getString("msg"));
            }

            //智能柜状态表信息重置为“空柜子”状态
            deviceStateInfo.setReservationId(null);
            deviceStateInfo.setOrderId(null);
            deviceStateInfo.setLicensePlate(null);
            deviceStateInfo.setState(DeviceStateInfo.EMPTY);
            deviceStateInfoService.saveOrUpdate(deviceStateInfo);

            //更改订单状态：取回车钥匙完毕 值为5
            Reservation reservation = reservationDao.findByConsumOrderId(orderId);
            if (null == reservation) {
                log.error("用户取回车钥匙后，更新订单信息失败。原因：订单对应的预约信息查询失败。");
                return RESCODE.NOT_FOUND.getJSONRES("用户取回车钥匙后，更新订单信息失败。原因：订单对应的预约信息查询失败。");
            }
            reservation.setState(Reservation.ALREADY_PICK_UP);
            reservationDao.saveOrUpdate(reservation);

            //订单表数据状态设置为“已交车”状态
            consumOrder.setState(Integer.valueOf(Constants.CAR_SERVICE_FINISH));
            consumOrder.setDeliverTime(new Date());
            consumOrderDao.saveOrUpdate(consumOrder);

            log.info("用户结算后取回钥匙成功！");
            try {
                //推送模板消息给用户
                Client client = clientDao.findById(consumOrder.getClientId());
                WXUser wxUser = wxUserDao.findUserByPhone(client.getPhone());
                WechatTemplateMessage.orderChanged(consumOrder, wxUser.getOpenId());
            } catch (Exception e) {
                log.error("推送模板消息失败！", e);
            }
            return RESCODE.SUCCESS.getJSONRES("用户结算后取回钥匙成功！");
        }
        log.error("用户结算后取回钥匙时，开柜失败。原因：订单支付状态状态不为“已结算（1）”状态。");
        return RESCODE.CREATE_ERROR.getJSONRES("用户结算后取回钥匙时，开柜失败。原因：订单支付状态状态不为“已结算（1）”状态。");
    }

    /**
     * 微信支付后，调用该接口去更新订单状态和预约表状态
     *
     * @param orderId
     * @return
     */
    public Map<String, Object> updateOrderInfo(String orderId) {
        if (StringUtils.isEmpty(orderId)) {
            log.error("用户结算后，更新订单信息失败。原因：参数orderId为空值。");
            return RESCODE.WRONG_PARAM.getJSONRES("用户结算后，更新订单信息失败。原因：参数orderId为空值。");
        }
        ConsumOrder order = consumOrderDao.findById(orderId);
        if (null == order) {
            return RESCODE.NO_RECORD.getJSONRES();
        }

        int orderPayState = order.getPayState();
        if (Constants.PAY_FINISH == orderPayState) {
            log.error("用户结算后，更新订单信息失败。原因：订单编号为（" + orderId + "）的订单数据已经结算过了。");
            return RESCODE.NO_RECORD.getJSONRES("用户结算后，更新订单信息失败。原因：订单编号为（" + orderId + "）的订单数据已经结算过了。");
        }

        Set<ProjectInfo> infos = order.getProjects();
        log.info("消费订单(id:" + order.getId() + ")进行项目内容更改");
        for (ProjectInfo projectInfo : infos) {
            //微信支付 == 现金  值为1
            projectInfo.setPayMethod(Constants.PROJECT_WITH_CASH);
        }
        log.debug(infos);
        order.setProjects(infos);

        //更改订单状态：支付完毕 值为3
        Reservation reservation = reservationDao.findByConsumOrderId(orderId);
        if (null == reservation) {
            log.error("用户结算后，更新订单信息失败。原因：订单对应的预约信息查询失败。");
            return RESCODE.NOT_FOUND.getJSONRES("用户结算后，更新订单信息失败。原因：订单对应的预约信息查询失败。");
        }
        reservation.setState(Reservation.USER_PAY_FINISH);
        reservationDao.saveOrUpdate(reservation);

        //设置支付方式，此处只能是微信支付 值为3
        order.setPayMethod(String.valueOf(Constants.PAY_BY_WX));
        order.setActualPrice(order.getTotalPrice());
        order.setPayState(Constants.PAY_FINISH);
        consumOrderDao.saveOrUpdate(order);

        //在收入表中添加数据
        IncomeOrder incomeOrder = new IncomeOrder();
        incomeOrder.setPayMethod(Integer.parseInt(order.getPayMethod()));
        incomeOrder.setAmount(order.getActualPrice());
        incomeOrder.setClientId(order.getClientId());
        incomeOrder.setLicensePlate(order.getLicensePlate());
        incomeOrder.setPayDate(new Date());
        incomeOrder.setProgramName(order.getProgramName());

        // 查看客户是否有卡,判断是否属于会员
        Client client = clientDao.findById(order.getClientId());
        if (client.getCards() == null || client.getCards().isEmpty()) {
            incomeOrder.setMember(false);
        } else {
            incomeOrder.setMember(true);
        }
        incomeOrderDao.save(incomeOrder);

        return RESCODE.SUCCESS.getJSONRES(order);
    }

    /**
     * 结账时使用会员卡结账
     *
     * @param orderId
     * @param cardNumber
     * @return
     */
    public Map<String, Object> payWithCard(String orderId, String cardNumber) {
        if (StringUtils.isEmpty(orderId)) {
            log.error("用户会员卡结算失败。原因：参数orderId为空值。");
            return RESCODE.WRONG_PARAM.getJSONRES("用户会员卡结算失败。原因：参数orderId为空值。");
        }
        if (StringUtils.isEmpty(cardNumber)) {
            log.error("用户会员卡结算失败。原因：参数cardNumber为空值。");
            return RESCODE.WRONG_PARAM.getJSONRES("用户会员卡结算失败。原因：参数cardNumber为空值。");
        }

        ConsumOrder order = consumOrderDao.findById(orderId);
        if (null == order) {
            log.error("用户会员卡结算失败。原因：未找到订单编号为（" + orderId + "）的订单数据。");
            return RESCODE.NO_RECORD.getJSONRES("用户会员卡结算失败。原因：未找到订单编号为（" + orderId + "）的订单数据。");
        }

        int orderPayState = order.getPayState();
        if (Constants.PAY_FINISH == orderPayState) {
            log.error("用户会员卡结算失败。原因：订单编号为（" + orderId + "）的订单数据已经结算过了。");
            return RESCODE.NO_RECORD.getJSONRES("用户会员卡结算失败。原因：订单编号为（" + orderId + "）的订单数据已经结算过了。");
        }

        Card card = cardDao.findByCardNumber(cardNumber);
        if (null == card) {
            log.error("用户会员卡结算失败。原因：未找到卡号为（" + cardNumber + "）的会员卡数据。");
            return RESCODE.NO_RECORD.getJSONRES("用户会员卡结算失败。原因：未找到卡号为（" + cardNumber + "）的会员卡数据。");
        }

        Set<CardProjectRemainingInfo> cardProjectRemainingInfos = card.getProjectInfos();
        Set<ProjectInfo> projects = order.getProjects();

        com.geariot.platform.freelycar_wechat.entities.Service serviceObj = card.getService();
        if (null == serviceObj) {
            //服务已删除
            log.error("用户会员卡结算失败。原因：对应的会员卡服务项目数据。");
            return RESCODE.NOT_FOUND.getJSONRES("用户会员卡结算失败。原因：对应的会员卡服务项目数据。");
        }

        Integer serviceType = serviceObj.getType();
        float balance = card.getBalance();
        float totalPrice = order.getTotalPrice();

        //0,1,2=次卡,组合卡,储值卡
        switch (serviceType) {
            case 0:
                //双重遍历，将卡次扣除
                for (CardProjectRemainingInfo cardProjectRemainingInfo : cardProjectRemainingInfos) {
                    for (ProjectInfo projectInfo : projects) {
                        if (cardProjectRemainingInfo.getProject().getId() == projectInfo.getProjectId()) {
                            //扣次
                            cardProjectRemainingInfo.setRemaining(cardProjectRemainingInfo.getRemaining() - 1);
                            //设置服务项目支付相关字段
                            projectInfo.setCardId(String.valueOf(card.getId()));
                            projectInfo.setPayMethod(Constants.PROJECT_WITH_CARD);
                            projectInfo.setCardName(serviceObj.getName());
                            projectInfo.setCardNumber(cardNumber);
                            break;
                        }
                    }
                }
                card.setProjectInfos(cardProjectRemainingInfos);
                break;
            case 1:
                //组合卡暂时不筛选
                break;
            case 2:
                //卡里余额大于此次服务费用
                if (balance > totalPrice) {
                    //扣除金额
                    card.setBalance(balance - totalPrice);
                    for (ProjectInfo projectInfo : projects) {
                        //设置服务项目支付相关字段
                        projectInfo.setCardId(String.valueOf(card.getId()));
                        projectInfo.setPayMethod(Constants.PROJECT_WITH_CARD);
                        projectInfo.setCardName(serviceObj.getName());
                        projectInfo.setCardNumber(cardNumber);
                    }
                }
                break;
            default:
        }

        //更改订单状态：支付完毕 值为3
        Reservation reservation = reservationDao.findByConsumOrderId(orderId);
        if (null == reservation) {
            log.error("用户结算后，更新订单信息失败。原因：订单对应的预约信息查询失败。");
            return RESCODE.NOT_FOUND.getJSONRES("用户结算后，更新订单信息失败。原因：订单对应的预约信息查询失败。");
        }
        reservation.setState(Reservation.USER_PAY_FINISH);
        reservationDao.saveOrUpdate(reservation);

        order.setProjects(projects);

        //设置支付方式，卡抵扣为0，实付金额也为0
        order.setPayMethod(String.valueOf(Constants.PROJECT_WITH_CARD));
        order.setActualPrice(0);
        order.setPayState(Constants.PAY_FINISH);
        consumOrderDao.saveOrUpdate(order);
        //扣卡次的信息需要存入数据库
        cardDao.updateCard(card);
        //在收入表中添加数据
        IncomeOrder incomeOrder = new IncomeOrder();
        incomeOrder.setPayMethod(Integer.parseInt(order.getPayMethod()));
        incomeOrder.setAmount(order.getActualPrice());
        incomeOrder.setClientId(order.getClientId());
        incomeOrder.setLicensePlate(order.getLicensePlate());
        incomeOrder.setPayDate(new Date());
        incomeOrder.setProgramName(order.getProgramName());
        incomeOrderDao.save(incomeOrder);
        return RESCODE.SUCCESS.getJSONRES();
    }
}
