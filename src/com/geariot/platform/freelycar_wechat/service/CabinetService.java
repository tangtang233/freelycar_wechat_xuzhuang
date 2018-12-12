package com.geariot.platform.freelycar_wechat.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.geariot.platform.freelycar_wechat.model.RESCODE;
import com.geariot.platform.freelycar_wechat.wsutils.WSClient;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 唐炜
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CabinetService {

    /**
     * 显示某个网关下所有设备的状态
     *
     * @param sn 网关编号
     * @return map(json)
     */
    public Map<String, Object> showGridsInfo(String sn) {
        if (StringUtils.isEmpty(sn)) {
            return RESCODE.WRONG_PARAM.getJSONRES();
        }
        String res = WSClient.getAllDevicesState();
        if (StringUtils.isEmpty(res)) {
            return RESCODE.UPDATE_ERROR.getJSONRES();
        }

        JSONObject resJSONObject = JSONObject.parseObject(res);
        if (WSClient.RESULT_SUCCESS.equalsIgnoreCase(resJSONObject.getString("res"))) {
            JSONArray list = resJSONObject.getJSONArray("value");
            JSONArray resultList = new JSONArray();
            for (int i = 0; i < list.size(); i++) {
                JSONObject deviceInfo = list.getJSONObject(i);
                String deviceId = deviceInfo.getString("id");
                if (StringUtils.isNotEmpty(deviceId) && deviceId.contains(sn)) {
                    resultList.add(deviceInfo);
                }
            }
            resJSONObject.remove("value");
            resJSONObject.put("value", resultList);
        }
        return resJSONObject.toJavaObject(Map.class);
    }

    /**
     * 打开指定设备的门
     *
     * @param deviceId 设备号
     * @return map(json)
     */
    public Map<String, Object> openDeviceById(String deviceId) {
        String res = WSClient.controlDevices(deviceId, WSClient.DOOR_STATE_OPEN);
        if (StringUtils.isEmpty(res)) {
            return RESCODE.UPDATE_ERROR.getJSONRES();
        }
        JSONObject resJSONObject = JSONObject.parseObject(res);
        return resJSONObject.toJavaObject(Map.class);
    }

    /**
     * 关闭指定设备的门
     *
     * @param deviceId 设备号
     * @return map(json)
     */
    public Map<String, Object> closeDeviceById(String deviceId) {
        String res = WSClient.controlDevices(deviceId, WSClient.DOOR_STATE_CLOSE);
        if (StringUtils.isEmpty(res)) {
            return RESCODE.UPDATE_ERROR.getJSONRES();
        }
        JSONObject resJSONObject = JSONObject.parseObject(res);
        return resJSONObject.toJavaObject(Map.class);
    }

    public List<JSONObject> showGridsInfoBySnAndSpecification(String sn, Integer specification) {
        List<JSONObject> resultList = new ArrayList<>();
        for (int i = 0; i < specification; i++) {
            int number = i + 1;
            String deviceId = sn + "-" + number;
            String resultJsonString = WSClient.getDeviceStateByID(deviceId);
            JSONObject resultJson = JSONObject.parseObject(resultJsonString);
            resultList.add(resultJson);
        }
        return resultList;
    }
}
