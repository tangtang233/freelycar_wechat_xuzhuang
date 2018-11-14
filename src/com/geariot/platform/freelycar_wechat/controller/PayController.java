package com.geariot.platform.freelycar_wechat.controller;

import com.geariot.platform.freelycar_wechat.dao.ConsumOrderDao;
import com.geariot.platform.freelycar_wechat.dao.WXPayOrderDao;
import com.geariot.platform.freelycar_wechat.entities.ConsumOrder;
import com.geariot.platform.freelycar_wechat.entities.ProjectInfo;
import com.geariot.platform.freelycar_wechat.entities.WXPayOrder;
import com.geariot.platform.freelycar_wechat.model.RESCODE;
import com.geariot.platform.freelycar_wechat.service.PayService;
import com.geariot.platform.freelycar_wechat.utils.Constants;
import com.geariot.platform.freelycar_wechat.utils.HttpRequest;
import com.geariot.platform.freelycar_wechat.utils.RandomStringGenerator;
import com.geariot.platform.freelycar_wechat.utils.query.FavourOrderBean;
import com.geariot.platform.freelycar_wechat.wxutils.*;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@Transactional
@RequestMapping(value = "/pay")
public class PayController {
    private static Logger log = LogManager.getLogger();

    @Autowired
    private PayService payService;

    @Autowired
    private WXPayOrderDao wxPayOrderDao;

    @Autowired
    private ConsumOrderDao consumOrderDao;

    @RequestMapping(value = "favour", method = RequestMethod.POST)
    public String wechatFavour(@RequestBody FavourOrderBean favourOrderBean, HttpServletRequest request) {
        log.info("购买券");
        float totalPrice = favourOrderBean.getTotalPrice();
        String openId = favourOrderBean.getOpenId();
        org.json.JSONObject res = payService.createFavourOrder(favourOrderBean);
        String orderId = res.getString(Constants.RESPONSE_DATA_KEY);
        log.info(orderId);
        return wechatPay(openId, orderId, totalPrice, request);
    }

    @RequestMapping(value = "membershipCard", method = RequestMethod.GET)
    public String wechatCard(String openId, float totalPrice, int serviceId, HttpServletRequest request) {
        log.info("购买卡");
        org.json.JSONObject res = payService.createCardOrder(openId, totalPrice, serviceId);
        String orderId = res.getString(Constants.RESPONSE_DATA_KEY);
        log.info(orderId);
        return wechatPay(openId, orderId, totalPrice, request);
    }

    @RequestMapping(value = "payment", method = RequestMethod.GET)
    public String wechatPay(String openId, String orderId, float totalPrice, HttpServletRequest request) {

        //微信支付
        log.debug("微信支付");
        JSONObject jsonObject = new JSONObject();
        Map<String, Object> map = new HashMap<String, Object>(16);
        //微信接口配置
        String ip = request.getHeader("x-forwarded-for") == null ? request.getRemoteAddr() : request.getHeader("x-forwarded-for");
        //判断订单
        if (IdentifyOrder.identify(orderId)) {
            ConsumOrder consumOrder = consumOrderDao.findById(orderId);
            String productName = null;
            for (ProjectInfo project : consumOrder.getProjects()) {
                productName += project.getName() + " ";
            }
//            map.put("body", productName);
            map.put("body", "productName");
            map.put("out_trade_no", consumOrder.getId());
        } else {
            WXPayOrder wxPayOrder = wxPayOrderDao.findById(orderId);
            //map.put("body", wxPayOrder.getProductName());
            map.put("body", "productName");
            map.put("out_trade_no", wxPayOrder.getId());
        }

        map.put("appid", WechatConfig.APP_ID);

        map.put("device_info", "WEB");
        map.put("mch_id", WechatConfig.MCH_ID);// 商户号，微信商户平台里获取
//随机32位			
        map.put("nonce_str", RandomStringGenerator.getRandomStringByLength(32));

//返回结果	自己掉自己的接口		
        map.put("notify_url", "http://www.freelycar.com/freelycar_wechat/api/pay/wechatresult");
        map.put("openid", openId);
        map.put("spbill_create_ip", ip);
        map.put("total_fee", (int) (totalPrice * 100));
        map.put("trade_type", "JSAPI");
        // 签名
        String sig = WeChatSignatureUtil.getSig(map);
        map.put("sign", sig);
        // 请求微信支付接口
//		HttpPost post = new HttpPost(uri);
//		post.setEntity(new StringEntity(str,"utf-8"));
        HttpEntity entity = HttpRequest.getEntity(XMLParser.getXmlFromMap(map));
        log.debug("entity: " + XMLParser.getXmlFromMap(map));
        String result = HttpRequest.postCall(WechatConfig.ORDER_URL, entity,
                null);
//第一步请求完成

        log.debug("请求微信支付结果：" + result);

        Map<String, Object> resultMap = XMLParser.getMapFromXML(result);
        if (!resultMap.isEmpty()) {
            if (resultMap.get("return_code").toString().equals("SUCCESS")) {
                // 预支付id
                String prepareId = resultMap.get("prepay_id").toString();
                Map<String, Object> payMap = new HashMap<String, Object>();
                payMap.put("appId", WechatConfig.APP_ID);
                payMap.put("timeStamp", Long.toString(System.currentTimeMillis()));
                payMap.put("nonceStr", RandomStringGenerator.getRandomStringByLength(32));
                payMap.put("package", "prepay_id=" + prepareId);
                payMap.put("signType", "MD5");
                // 签名
                String pagSign = WeChatSignatureUtil.getSig(payMap);
                payMap.put("paySign", pagSign);
                jsonObject.put(Constants.RESPONSE_CODE_KEY, RESCODE.SUCCESS.getValue());
                jsonObject.put(Constants.RESPONSE_MSG_KEY,
                        RESCODE.SUCCESS.getMsg());
                jsonObject.put(Constants.RESPONSE_DATA_KEY, payMap);
            } else {
                jsonObject
                        .put(Constants.RESPONSE_CODE_KEY, RESCODE.ORDER_ERROR);
                jsonObject.put(Constants.RESPONSE_MSG_KEY,
                        RESCODE.ORDER_ERROR.getMsg());
            }
        } else {
            jsonObject
                    .put(Constants.RESPONSE_CODE_KEY, RESCODE.CALL_PORT_ERROR);
            jsonObject.put(Constants.RESPONSE_MSG_KEY,
                    RESCODE.CALL_PORT_ERROR.getMsg());
        }
        log.error("结果：return_code: " + jsonObject.toString());
        return jsonObject.toString();

    }

    @RequestMapping(value = "/wx/getJSSDKConfig", method = RequestMethod.GET)
    public String getJsSDKConfig(String targetUrl) {
        log.debug("JSSDK Url:" + targetUrl);
        if (targetUrl == null || targetUrl.isEmpty()) {
            throw new RuntimeException("jsapiTicket获取失败,当前url为空！！");
        }

        String noncestr = UUID.randomUUID().toString();
        org.json.JSONObject ticketJson = WechatConfig.getJsApiTicketByWX();
        String ticket = ticketJson.getString("ticket");
        String timestamp = String.valueOf(System.currentTimeMillis());

        int index = targetUrl.indexOf("#");
        if (index > 0) {
            targetUrl = targetUrl.substring(0, index);
        }

        // 对给定字符串key手动排序
        String param = "jsapi_ticket=" + ticket + "&noncestr=" + noncestr
                + "&timestamp=" + timestamp + "&url=" + targetUrl;

        String signature = MD5.encode("SHA1", param);

        JSONObject jsSDKConfig = new JSONObject();

        jsSDKConfig.put("appId", WechatConfig.APP_ID);
        jsSDKConfig.put("nonceStr", noncestr);
        jsSDKConfig.put("timestamp", timestamp);
        jsSDKConfig.put("signature", signature);
        return jsSDKConfig.toString();

    }


    @RequestMapping(value = "wechatresult")
    public void wechatResult(HttpServletRequest request,
                             HttpServletResponse response) {
        log.error("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!准备回调!!!!!!!!!");
        log.info("callback wechatPay");
        //Map
        Map<String, Object> map = XMLParser.requestToXml(request);
        Map<String, Object> responseMap = new HashMap<String, Object>();
        if (map != null && !map.isEmpty()) {
            if ("SUCCESS".equals(map.get("return_code"))) {
                if (!WeChatSignatureUtil.isCorrect(map)) {
                    responseMap.put("return_code", "FAIL");
                    responseMap.put("return_msg", "签名失败");
                } else {
                    responseMap.put("return_code", "SUCCESS");
                    responseMap.put("return_msg", "OK");
                    String orderId = map.get("out_trade_no").toString();
                    // 支付成功，处理支付结果，同步到数据库

                    org.json.JSONObject obj = payService.paySuccess(orderId);
                    log.debug("paySuccess本地处理结果:" + obj.toString());
                    if ((RESCODE) obj.get(Constants.RESPONSE_CODE_KEY) != RESCODE.SUCCESS) {
                        responseMap.put(Constants.RESPONSE_CODE_KEY, (RESCODE) obj.get(Constants.RESPONSE_CODE_KEY));
                    }
                }
                log.debug(responseMap);
                try {
                    OutputStream os = response.getOutputStream();
                    os.write(XMLParser.getXmlFromMap(responseMap).getBytes(
                            "UTF-8"));
                    os.flush();
                    os.close();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @RequestMapping(value = "/activity", method = RequestMethod.GET)
    public String activityPay(int clientId) {
        System.out.println("我被调用了!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        return payService.activityPay(clientId);
    }
}
