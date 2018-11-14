package com.geariot.platform.freelycar_wechat.controller;

import com.geariot.platform.freelycar_wechat.service.CabinetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
     * @param sn 网关编号
     * @return  map(json)
     */
    @ResponseBody
    @RequestMapping(value = "/showGridsInfo", method = RequestMethod.GET)
    public Map<String, Object> showGridsInfo(
            @RequestParam(name = "sn") String sn
    ) {
        return cabinetService.showGridsInfo(sn);
    }

    /**
     * 打开某个设备的门
     * @param deviceId 设备号
     * @return  map(json)
     */
    @ResponseBody
    @RequestMapping(value = "/openDeviceById", method = RequestMethod.GET)
    public Map<String, Object> openDeviceById(@RequestParam(name = "deviceId") String deviceId) {
        return cabinetService.openDeviceById(deviceId);
    }

    /**
     * 关闭某个设备的门
     * @param deviceId 设备号
     * @return  map(json)
     */
    @ResponseBody
    @RequestMapping(value = "/closeDeviceById", method = RequestMethod.GET)
    public Map<String, Object> closeDeviceById(@RequestParam(name = "deviceId") String deviceId) {
        return cabinetService.closeDeviceById(deviceId);
    }
}
