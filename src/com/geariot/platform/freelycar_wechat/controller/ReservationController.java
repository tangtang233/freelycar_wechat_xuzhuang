package com.geariot.platform.freelycar_wechat.controller;

import com.geariot.platform.freelycar_wechat.entities.Reservation;
import com.geariot.platform.freelycar_wechat.service.ProjectService;
import com.geariot.platform.freelycar_wechat.service.ReservationService;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 预约表相关方法
 *
 * @author 唐炜
 */
@RestController
@RequestMapping(value = "/reservation")
public class ReservationController {
    private static Logger log = LogManager.getLogger(ReservationController.class);

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ProjectService projectService;

    /**
     * 添加一条预约
     *
     * @param reservation 预约数据实体对象
     * @return string
     */
    @ResponseBody
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String add(@RequestBody Reservation reservation) {
        log.debug("获取到前端传来的预约信息对象：", reservation);
        return reservationService.add(reservation);
    }

    /**
     * 取消一条预约
     *
     * @param reservationId 主键ID
     * @return string
     */
    @ResponseBody
    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    public String cancel(@RequestParam(name = "id") Integer reservationId) {
        return reservationService.cancel(reservationId);
    }

    /**
     * 查找预约列表
     *
     * @param licensePlate 车牌
     * @param name         姓名
     * @param openId       openId
     * @param state        订单状态
     * @param sortColumn   排序字段
     * @param sortType     排序规则
     * @param page         页码
     * @param number       数量
     * @return map/json
     */
    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Map<String, Object> list(
            @RequestParam(name = "licensePlate", required = false) String licensePlate,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "openId", required = false) String openId,
            @RequestParam(name = "state", required = false) Integer state,
            @RequestParam(name = "sortColumn", required = false) String sortColumn,
            @RequestParam(name = "sortType", required = false) String sortType,
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "number", required = false) Integer number
    ) {
        Map<String, Object> paramMap = new HashMap<>(6);
        if (StringUtils.isNotEmpty(licensePlate)) {
            paramMap.put("licensePlate", licensePlate);
        }
        if (StringUtils.isNotEmpty(name)) {
            paramMap.put("name", name);
        }
        if (StringUtils.isNotEmpty(openId)) {
            paramMap.put("openId", openId);
        }
        if (null != state) {
            paramMap.put("state", state);
        }
        if (StringUtils.isNotEmpty(sortColumn)) {
            paramMap.put("sortColumn", sortColumn);
        }
        if (StringUtils.isNotEmpty(sortType)) {
            paramMap.put("sortType", sortType);
        }
        return reservationService.list(paramMap, page, number);
    }

    /**
     * 根据id查找某条预约数据
     *
     * @param id 主键ID
     * @return json
     */
    @ResponseBody
    @RequestMapping(value = "/findById", method = RequestMethod.GET)
    public Map<String, Object> findById(@RequestParam Integer id) {
        return reservationService.findById(id);
    }


    /**
     * 查询在智能柜上架销售的服务
     *
     * @return map
     */
    @ResponseBody
    @RequestMapping(value = "/listProjectOnSale", method = RequestMethod.GET)
    public Map<String, Object> listProjectOnSale() {
        return projectService.listProjectOnSale();
    }

    /**
     * 加载“本次预约订单”
     *
     * @param clientId 用户clientId
     * @return json/map
     */
    @ResponseBody
    @RequestMapping(value = "/loadTheBookingOrder", method = RequestMethod.GET)
    public Map<String, Object> loadTheBookingOrder(@RequestParam String clientId) {
        return reservationService.loadTheBookingOrderByClientId(clientId);
    }


}
