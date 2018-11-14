package com.geariot.platform.freelycar_wechat.controller;

import com.geariot.platform.freelycar_wechat.entities.Car;
import com.geariot.platform.freelycar_wechat.model.InfoBean;
import com.geariot.platform.freelycar_wechat.model.RESCODE;
import com.geariot.platform.freelycar_wechat.service.WXUserService;
import com.geariot.platform.freelycar_wechat.utils.JsonResFactory;
import com.geariot.platform.freelycar_wechat.wxutils.WechatLoginUse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;

@Controller
@RequestMapping("/user")
public class WXUserController {
    private static final Logger log = LogManager
            .getLogger(WXUserController.class);
    /**
     * 生产环境地址
     */
    private static String BASEURL = "http://www.freelycar.com/freelycar_wechat/index.html#/";

    @Autowired
    private WXUserService wxUserService;


    /*微信菜单转发*/

    //个人中心
    @RequestMapping(value = "/wechatlogin")
    public String wechatLogin(String htmlPage, String code, boolean isAuth) {
        return getWechatInfo(htmlPage, code, isAuth);
    }

    //直接内部跳转
    @RequestMapping(value = "/menuRedirect")
    public String menuRedirect(String htmlPage, String code) {
        String ret = BASEURL + htmlPage;
        return "redirect:" + ret;
    }

    public String getWechatInfo(String htmlPage, String code, boolean isAuth) {
        String wechatInfo = WechatLoginUse.wechatInfo(code);
        JSONObject resultJson;
        try {
            resultJson = new JSONObject(wechatInfo);
            if (resultJson.get("message").equals("success")) {
                String openid = resultJson.getString("openid");
                String nickname = resultJson.getString("nickname");
                String headimgurl = resultJson.getString("headimgurl");
                nickname = URLEncoder.encode(nickname, "utf-8");
                headimgurl = URLEncoder.encode(headimgurl, "utf-8");
                String ret = BASEURL + htmlPage;
                //是否重新授权
                if (isAuth) {
                    ret = BASEURL + "login?openid=" + openid + "&nickname=" + nickname + "&headimgurl=" + headimgurl + "&directUrl=" + htmlPage;
                } else {
                    boolean wxUser = wxUserService.isExistUserOpenId(openid);
                    log.error(wxUser);
                    if (!wxUser) {
                        ret = BASEURL + "login?openid=" + openid + "&nickname=" + nickname + "&headimgurl=" + headimgurl + "&directUrl=" + htmlPage;
                    } else {
                        if ("indexPage".equals(htmlPage)) {
                            ret = BASEURL + "indexPage";
                        }
                    }
                }
                return "redirect:" + ret;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //重新授权
        //return "redirect:https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxfd188f8284ee297b&redirect_uri=http%3a%2f%2fwww.freelycar.com%2ffreelycar%2fapi%2fwechat%2ftlogin&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";    //重定向到失败页面
        return "redirect:https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx89ac1808e298928d&redirect_uri=http%3a%2f%2fwww.freelycar.com%2ffreelycar_wechat%2fapi%2fuser%2fwechatlogin%3FhtmlPage%3DpersonalInfo&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";

    }


    @RequestMapping(value = "/wechatLoginForArk")
    public String wechatLoginForArk(String htmlPage, String code, boolean isAuth, String cabinetSN) {
        return getWechatInfoForArk(htmlPage, code, isAuth, cabinetSN);
    }

    public String getWechatInfoForArk(String htmlPage, String code, boolean isAuth, String cabinetSN) {
        String wechatInfo = WechatLoginUse.wechatInfo(code);
        JSONObject resultJson;
        try {
            resultJson = new JSONObject(wechatInfo);
            if (resultJson.get("message").equals("success")) {
                String openid = resultJson.getString("openid");
                String nickname = resultJson.getString("nickname");
                String headimgurl = resultJson.getString("headimgurl");
                nickname = URLEncoder.encode(nickname, "utf-8");
                headimgurl = URLEncoder.encode(headimgurl, "utf-8");
                String ret = BASEURL + htmlPage;
                //是否重新授权
                if (isAuth) {
                    ret = BASEURL + "chooseid?openid=" + openid + "&nickname=" + nickname + "&headimgurl=" + headimgurl + "&cabinetSN=" + cabinetSN;
                } else {
                    boolean wxUser = wxUserService.isExistUserOpenId(openid);
                    log.error(wxUser);
                    if (!wxUser) {
                        ret = BASEURL + "chooseid?openid=" + openid + "&nickname=" + nickname + "&headimgurl=" + headimgurl + "&cabinetSN=" + cabinetSN;
                    } else {
                        if ("chooseid".equals(htmlPage)) {
                            ret = BASEURL + "chooseid?openid=" + openid + "&nickname=" + nickname + "&headimgurl=" + headimgurl + "&cabinetSN=" + cabinetSN;
                        }
                    }
                }
                return "redirect:" + ret;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //重新授权
        return "redirect:https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx89ac1808e298928d&redirect_uri=http%3A%2F%2Fwww.freelycar.com%2Ffreelycar_wechat%2Fapi%2Fuser%2FwechatLoginForArk%3FhtmlPage%3DpersonalInfo%26cabinetSN%3D862643037702838&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";

    }

    @ResponseBody
    @RequestMapping(value = "/updateWXUser", method = RequestMethod.POST)
    public String addWxUser(@RequestBody InfoBean infoBean) {
        try {
            return wxUserService.addWXUser(infoBean.getPhone(), infoBean.getName(), infoBean.getBirthday(), infoBean.getGender());
        } catch (ParseException e) {
            return JsonResFactory.buildOrg(RESCODE.DATE_FORMAT_ERROR).toString();
        }
    }

    @ResponseBody
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(String openId) {
        return wxUserService.deletWXUser(openId);
    }

    @ResponseBody
    @RequestMapping(value = "/myDiscount", method = RequestMethod.GET)
    public String myDiscount(int clientId) {
        return wxUserService.listDiscount(clientId);
    }

    // 要显示积分记录查project
    @ResponseBody
    @RequestMapping(value = "/points", method = RequestMethod.GET)
    public String points(int clientId) {
        return wxUserService.getPoint(clientId);
    }

    // 设置时间
    @ResponseBody
    @RequestMapping(value = "/defaultCar", method = RequestMethod.GET)
    public String defaultCar(int carId) {
        System.out.println(carId);
        return wxUserService.setDefaultCar(carId);
    }

    @ResponseBody
    @RequestMapping(value = "/carInfo", method = RequestMethod.GET)
    public String carInfo(int clientId, int id, String insuranceCity,
                          String insuranceCompany, String insuranceEndtime, String name, String idNumber)
            throws ParseException {
        return wxUserService.modifyCar(clientId, id, insuranceCity,
                insuranceCompany, insuranceEndtime, name, idNumber);
    }

    @ResponseBody
    @RequestMapping(value = "/annualCheck", method = RequestMethod.GET)
    public String annualCheck(int clientId, int id, String licenseDate) throws ParseException {
        return wxUserService.annualCheck(clientId, id, licenseDate);
    }

    @ResponseBody
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public String detail(int clientId) {
        return wxUserService.detail(clientId);
    }

    @ResponseBody
    @RequestMapping(value = "/listCar", method = RequestMethod.GET)
    public String listCar(int clientId) {
        return wxUserService.listCar(clientId);
    }

    @ResponseBody
    @RequestMapping(value = "/addCar", method = RequestMethod.POST)
    public String addcar(@RequestBody Car car) {
        return wxUserService.addCar(car);
    }

    @ResponseBody
    @RequestMapping(value = "/delCar", method = RequestMethod.GET)
    public String delCar(int carId) {
        return wxUserService.deleteCar(carId);
    }

    @ResponseBody
    @RequestMapping(value = "/wxInfo", method = RequestMethod.GET)
    public String wxInfo(String openId) {
        return wxUserService.getWXUser(openId);
    }

    @ResponseBody
    @RequestMapping(value = "/getWXUserByPhone", method = RequestMethod.GET)
    public String getWXUserByPhone(String phone) {
        return wxUserService.getWXUserByPhone(phone);
    }

    @ResponseBody
    @RequestMapping(value = "/listCard", method = RequestMethod.GET)
    public String listCard(int clientId) {
        return wxUserService.listCard(clientId);
    }

    @ResponseBody
    @RequestMapping(value = "/carDetail", method = RequestMethod.GET)
    public String carDetail(int carId) {
        return wxUserService.carDetail(carId);
    }

    @ResponseBody
    @RequestMapping(value = "/quickOrder", method = RequestMethod.GET)
    public String quickOrder(int clientId) {
        return wxUserService.quickOrder(clientId);
    }

    @ResponseBody
    @RequestMapping(value = "/insurance", method = RequestMethod.GET)
    public String insuranceRemind(int carId, boolean check) {
        return wxUserService.insuranceRemind(carId, check);
    }

    @ResponseBody
    @RequestMapping(value = "/annual", method = RequestMethod.GET)
    public String annualRemind(int carId, boolean check) {
        return wxUserService.annualRemind(carId, check);
    }

}
