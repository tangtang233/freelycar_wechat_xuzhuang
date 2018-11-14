package com.geariot.platform.freelycar_wechat.controller;

import com.geariot.platform.freelycar_wechat.entities.ConsumOrder;
import com.geariot.platform.freelycar_wechat.service.ConsumOrderService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 唐炜
 */
@RestController
@RequestMapping(value = "/orders")
public class ConsumOrderController {

    private static final Logger log = LogManager.getLogger(ConsumOrderController.class);

    @Autowired
    private ConsumOrderService consumOrderService;

    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public String detail(String consumOrderId) {
        return consumOrderService.detail(consumOrderId);
    }

    @RequestMapping(value = "/listConsumOrder", method = RequestMethod.GET)
    public String listConsumOrder(int clientId, int page, int number) {
        return consumOrderService.listConsumOrder(clientId, page, number);
    }

    @RequestMapping(value = "/detailWXPayOrder", method = RequestMethod.GET)
    public String detailWXPayOrder(String wxPayOrderId) {
        return consumOrderService.detailWXPayOrder(wxPayOrderId);
    }

    @RequestMapping(value = "/listWXPayOrder", method = RequestMethod.GET)
    public String listWXPayOrder(int clientId, int page, int number) {
        return consumOrderService.listWXPayOrder(clientId, page, number);
    }

    @RequestMapping(value = "/comment", method = RequestMethod.GET)
    public String comment(String consumOrderId, String comment, int stars) {
        log.debug("**************************");
        log.debug("-------------" + "订单编号:" + consumOrderId + ",评论内容:" + comment + ",星级:" + stars);
        return consumOrderService.comment(consumOrderId, comment, stars);
    }

    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    public Map<String, Object> modify(@RequestBody ConsumOrder consumOrder) {
        return consumOrderService.modify(consumOrder);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Map<String, Object> list(
            @RequestParam(required = false) Integer clientId,
            @RequestParam(required = false) Integer state,
            int page, int number
    ) {
        Map<String, Object> paramMap = new HashMap<>(2);
        if (null != clientId) {
            paramMap.put("clientId", clientId);
        }
        if (null != state) {
            paramMap.put("state", state);
        }
        return consumOrderService.list(paramMap, page, number);
    }

    /**
     * 技师取出钥匙去进行服务
     *
     * @param reservationId
     * @param deviceId
     * @param staffId
     * @return
     */
    @RequestMapping(value = "/getKeyToService", method = RequestMethod.GET)
    public Map<String, Object> getKeyToService(
            @RequestParam(value = "reservationId", required = false) Integer reservationId,
            @RequestParam(value = "deviceId", required = false) String deviceId,
            @RequestParam(value = "staffId", required = false) Integer staffId
    ) {
        return consumOrderService.getKeyToService(reservationId, deviceId, staffId);
    }

    /**
     * 服务结束，技师去存钥匙
     */
    @RequestMapping(value = "/serviceFinish", method = RequestMethod.GET)
    public Map<String, Object> serviceFinish(
            @RequestParam(value = "orderId", required = false) String orderId,
            @RequestParam(value = "cabinetSN", required = false) String cabinetSN,
            @RequestParam(value = "parkingLocation", required = false) String parkingLocation
    ) {
        return consumOrderService.serviceFinish(orderId, cabinetSN, parkingLocation);
    }

    /**
     * 订单结算页面（会员卡）
     *
     * @param consumOrderId
     * @return
     */
    @RequestMapping(value = "/queryById", method = RequestMethod.GET)
    public String queryById(String consumOrderId) {
        return consumOrderService.getOrderById(consumOrderId);
    }

    /**
     * 用户支付完毕后取出钥匙
     *
     * @param orderId 订单编号
     * @return json
     */
    @RequestMapping(value = "/userTakeOutKey", method = RequestMethod.GET)
    public Map<String, Object> userTakeOutKey(
            @RequestParam(value = "orderId") String orderId
    ) {
        return consumOrderService.userTakeOutKey(orderId);
    }

    /**
     * 微信支付后，调用该接口去更新订单状态
     *
     * @param orderId 订单编号
     * @return json
     */
    @RequestMapping(value = "/updateOrderInfo", method = RequestMethod.GET)
    public Map<String, Object> updateOrderInfo(
            @RequestParam(value = "orderId") String orderId
    ) {
        return consumOrderService.updateOrderInfo(orderId);
    }

    /**
     * 结账时使用会员卡结账
     * @param orderId 订单编号
     * @param cardNumber 结算卡号
     * @return  json
     */
    @RequestMapping(value = "/payWithCard", method = RequestMethod.GET)
    public Map<String, Object> payWithCard(
            @RequestParam(value = "orderId") String orderId,
            @RequestParam(value = "cardNumber") String cardNumber
    ) {
        return consumOrderService.payWithCard(orderId, cardNumber);
    }
}
