package com.geariot.platform.freelycar_wechat.controller;

import com.alibaba.fastjson.JSONObject;
import com.geariot.platform.freelycar_wechat.service.CabinetService;
import com.geariot.platform.freelycar_wechat.wsutils.WSClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author 唐炜
 */
@RestController
@RequestMapping(value = "/cabinet")
public class CabinetController {

    @Autowired
    private CabinetService cabinetService;

    /**
     * 显示某个网关下所有设备的状态
     *
     * @param sn 网关编号
     * @return map(json)
     */
    @ResponseBody
    @RequestMapping(value = "/showGridsInfo", method = RequestMethod.GET)
    public List<JSONObject> showGridsInfo(
            @RequestParam(name = "sn") String sn,
            @RequestParam(name = "specification") Integer specification
    ) {
        return cabinetService.showGridsInfoBySnAndSpecification(sn, specification);
    }

    /**
     * 显示某个柜门的状态
     * @param deviceId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/showGridInfo", method = RequestMethod.GET)
    public JSONObject showGridsInfo(
            @RequestParam(name = "deviceId") String deviceId
    ) {
        return JSONObject.parseObject(WSClient.getDeviceStateByID(deviceId));
    }

    /**
     * 打开某个设备的门
     *
     * @param deviceId 设备号
     * @return map(json)
     */
    @ResponseBody
    @RequestMapping(value = "/openDeviceById", method = RequestMethod.GET)
    public Map<String, Object> openDeviceById(@RequestParam(name = "deviceId") String deviceId) {
        return cabinetService.openDeviceById(deviceId);
    }

    /**
     * 关闭某个设备的门
     *
     * @param deviceId 设备号
     * @return map(json)
     */
    @ResponseBody
    @RequestMapping(value = "/closeDeviceById", method = RequestMethod.GET)
    public Map<String, Object> closeDeviceById(@RequestParam(name = "deviceId") String deviceId) {
        return cabinetService.closeDeviceById(deviceId);
    }
}
