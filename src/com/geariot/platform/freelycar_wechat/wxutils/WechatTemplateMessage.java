package com.geariot.platform.freelycar_wechat.wxutils;

import com.geariot.platform.freelycar_wechat.entities.ConsumOrder;
import com.geariot.platform.freelycar_wechat.entities.ProjectInfo;
import com.geariot.platform.freelycar_wechat.entities.Staff;
import com.geariot.platform.freelycar_wechat.entities.WXPayOrder;
import com.geariot.platform.freelycar_wechat.utils.Constants;
import org.apache.commons.lang.StringUtils;
import org.apache.http.entity.StringEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

public class WechatTemplateMessage {

    private static final String PAY_SUCCESS_TEMPLATE_ID = "F5CyYJ5u9_1wRcapK1ECOYRrjxcLcL3rjB0xUg_VQn0";
    private static final String PAY_FAIL_ID = "o0TOjg7KkxoL4CtQ91j--TVVmxYDSNk-dLWqoUVd8mw";
    private static final String ORDER_CHANGED_ID = "Au2k3CSXdYZu7ujvagXT6GxTzjDGUmQTkI8xutL30Fc";

    private static final Logger log = LogManager.getLogger(WechatTemplateMessage.class);

    private static final String PAY_ERROR_DATABASE_FAIL = "服务异常";

    private static String invokeTemplateMessage(JSONObject params) {
        //解决中文乱码问题
        StringEntity entity = new StringEntity(params.toString(), "utf-8");
        String result = HttpRequest.postCall(WechatConfig.WECHAT_TEMPLATE_MESSAGE_URL +
                        WechatConfig.getAccessTokenForInteface().getString("access_token"),
                entity, null);
        log.debug("微信模版消息结果：" + result);
        return result;
    }


    //{{first.DATA}}
//类型：{{keyword1.DATA}}
//金额：{{keyword2.DATA}}
//状态：{{keyword3.DATA}}
//时间：{{keyword4.DATA}}
//备注：{{keyword5.DATA}}
//{{remark.DATA}}
    public static void paySuccess(WXPayOrder wxPayOrder) {
        log.debug("准备支付成功模版消息。。。");
        SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        JSONObject params = new JSONObject();
        JSONObject data = new JSONObject();
        params.put("touser", wxPayOrder.getOpenId());
        params.put("template_id", PAY_SUCCESS_TEMPLATE_ID);
//		params.put("url", "http://www.geariot.com/fitness/class.html");
        data.put("first", keywordFactory("支付成功", "#173177"));
        data.put("keyword1", keywordFactory(wxPayOrder.getProductName(), "#173177"));
        data.put("keyword2", keywordFactory((float) (Math.round(wxPayOrder.getTotalPrice() * 100)) / 100 + "元", "#173177"));
        data.put("keyword3", keywordFactory("成功", "#173177"));
        data.put("keyword4", keywordFactory(df.format(wxPayOrder.getFinishDate()), "#173177"));
        data.put("keyword5", keywordFactory(""));
        data.put("remark", keywordFactory(""));
        params.put("data", data);
        String result = invokeTemplateMessage(params);
        log.debug("微信支付成功模版消息结果：" + result);
//		return result;
    }

    public static void paySuccess(ConsumOrder consumOrder, String openId) {
        log.debug("准备支付成功模版消息。。。");
        SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        JSONObject params = new JSONObject();
        JSONObject data = new JSONObject();
        params.put("touser", openId);
        params.put("template_id", PAY_SUCCESS_TEMPLATE_ID);
//		params.put("url", "http://www.geariot.com/fitness/class.html");
        data.put("first", keywordFactory("支付成功", "#173177"));
        data.put("keyword1", keywordFactory(getConsumOrderProductName(consumOrder), "#173177"));
        data.put("keyword2", keywordFactory((float) (Math.round(consumOrder.getTotalPrice() * 100)) / 100 + "元", "#173177"));
        data.put("keyword3", keywordFactory("成功", "#173177"));
        data.put("keyword4", keywordFactory(df.format(consumOrder.getFinishTime()), "#173177"));
        data.put("keyword5", keywordFactory(""));
        data.put("remark", keywordFactory(""));
        params.put("data", data);
        String result = invokeTemplateMessage(params);
        log.debug("微信支付成功模版消息结果：" + result);
//		return result;
    }


    //	{{first.DATA}}
//	支付金额：{{keyword1.DATA}}
//	商品信息：{{keyword2.DATA}}
//	失败原因：{{keyword3.DATA}}
//	{{remark.DATA}}
    public static void errorCancel(WXPayOrder wxPayOrder) {
        log.debug("支付成功，数据库更新失败！");
        JSONObject params = new JSONObject();
        JSONObject data = new JSONObject();
        params.put("touser", wxPayOrder.getId());
        params.put("template_id", PAY_FAIL_ID);

        data.put("first", keywordFactory("支付失败", "#173177"));
        data.put("keyword1", keywordFactory((float) (Math.round(wxPayOrder.getTotalPrice() * 100)) / 100 + "元", "#173177"));
        data.put("keyword2", keywordFactory(wxPayOrder.getProductName(), "#173177"));
        data.put("keyword3", keywordFactory("服务异常", "#173177"));
        data.put("remark", keywordFactory("请妥善保存单号，联系客服人员"));
        params.put("data", data);
        String result = invokeTemplateMessage(params);
        log.debug("微信支付失败结果：" + result);
    }

    public static void errorWXCancel(ConsumOrder consumOrder, String openId) {
        log.debug("支付成功，数据库更新失败！");
        JSONObject params = new JSONObject();
        JSONObject data = new JSONObject();
        params.put("touser", openId);
        params.put("template_id", PAY_FAIL_ID);
        data.put("first", keywordFactory("支付失败", "#173177"));
        data.put("keyword1", keywordFactory((float) (Math.round(consumOrder.getTotalPrice() * 100)) / 100 + "元", "#173177"));
        data.put("keyword2", keywordFactory(getConsumOrderProductName(consumOrder), "#173177"));
        data.put("keyword3", keywordFactory("服务异常", "#173177"));
        data.put("remark", keywordFactory("请妥善保存单号，联系客服人员"));
        params.put("data", data);
        String result = invokeTemplateMessage(params);
        log.debug("微信支付失败结果：" + result);
    }

    private static String getConsumOrderProductName(ConsumOrder consumOrder) {
        String productName = "";
        for (ProjectInfo projectInfo : consumOrder.getProjects())
            productName += projectInfo.getName();
        return productName;
    }

    private static JSONObject keywordFactory(String value) {
        JSONObject keyword = new JSONObject();
        keyword.put("value", value);
        return keyword;
    }

    private static JSONObject keywordFactory(String value, String color) {
        JSONObject keyword = keywordFactory(value);
        keyword.put("color", color);
        return keyword;
    }

    /**
     * {{first.DATA}}
     * 订单编号： {{OrderSn.DATA}}
     * 订单状态： {{OrderStatus.DATA}}
     * {{remark.DATA}}
     */
    public static void orderChanged(ConsumOrder consumOrder, String openId) {
        log.info("准备订单更新模版消息。。。");
        Staff staff = consumOrder.getPickCarStaff();
        String staffName = null;
        if (null != staff) {
            staffName = staff.getName();
        }
        int state = consumOrder.getState();
        String first;
        String stateString;
        String parkingLocation = consumOrder.getParkingLocation();
        String remark = "";
        String remarkSuffix = "小易爱车竭诚为您服务！";
        switch (String.valueOf(state)) {
            case Constants.CAR_SERVICE_START:
                stateString = "已接车";
                first = "已接到您的爱车" + consumOrder.getLicensePlate() + "，我们将马上为您服务。";
                if (StringUtils.isNotEmpty(staffName)) {
                    remark += "服务人员：" + staffName + "\\/r\\/n";
                }
                break;
            case Constants.CAR_SERVICE_COMPLETE:
                stateString = "已完工";
                first = "您的爱车" + consumOrder.getLicensePlate() + "已服务完成，等待您的取回。";
                if (StringUtils.isNotEmpty(staffName)) {
                    remark += "服务人员：" + staffName + "\\/r\\/n";
                }
                if (StringUtils.isNotEmpty(parkingLocation)) {
                    remark += "停车位置：" + parkingLocation + "\\/r\\/n";
                }
                break;
            case Constants.CAR_SERVICE_FINISH:
                stateString = "已交车";
                first = "您的爱车" + consumOrder.getLicensePlate() + "已交车，评价领积分。";
                break;
            default:
                stateString = "已交车";
                first = "您的爱车" + consumOrder.getLicensePlate() + "已交车，评价领积分。";
        }

        JSONObject params = new JSONObject();
        JSONObject data = new JSONObject();
        params.put("touser", openId);
        params.put("template_id", ORDER_CHANGED_ID);
        params.put("url", "http://localhost:8000/index.html#/ordertrack?orderId=" + consumOrder.getId());
        data.put("first", keywordFactory(first, "#173177"));
        data.put("OrderSn", keywordFactory(consumOrder.getId(), "#173177"));
        data.put("OrderStatus", keywordFactory(stateString, "#173177"));
        data.put("remark", keywordFactory(remark + remarkSuffix));
        params.put("data", data);
        String result = invokeTemplateMessage(params);
        log.info("微信订单更新模版消息结果：" + result);
    }


}
