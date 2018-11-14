package com.geariot.platform.freelycar_wechat.service;

import com.geariot.platform.freelycar_wechat.dao.*;
import com.geariot.platform.freelycar_wechat.entities.*;
import com.geariot.platform.freelycar_wechat.model.RESCODE;
import com.geariot.platform.freelycar_wechat.utils.Constants;
import com.geariot.platform.freelycar_wechat.utils.JsonPropertyFilter;
import com.geariot.platform.freelycar_wechat.utils.JsonResFactory;
import com.geariot.platform.freelycar_wechat.utils.NicknameFilter;
import com.geariot.platform.freelycar_wechat.utils.query.OrderBean;
import com.geariot.platform.freelycar_wechat.utils.query.PointBean;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class WXUserService {
    private static Logger log = Logger.getLogger(WXUserService.class);
    @Autowired
    private CarDao carDao;
    @Autowired
    private WXUserDao wxUserDao;
    @Autowired
    private ClientDao clientDao;
    @Autowired
    private PointDao pointDao;
    @Autowired
    private ConsumOrderDao consumOrderDao;
    @Autowired
    private CardDao cardDao;

    public static int daysbetween(Date now, Date licenseDate) {
        Calendar afterTwoYears = Calendar.getInstance();
        afterTwoYears.setTime(licenseDate);
        afterTwoYears.add(Calendar.YEAR, 2);
        Calendar afterFourYears = Calendar.getInstance();
        afterFourYears.setTime(licenseDate);
        afterFourYears.add(Calendar.YEAR, 4);
        Calendar afterSixYears = Calendar.getInstance();
        afterSixYears.setTime(licenseDate);
        afterSixYears.add(Calendar.YEAR, 6);
        Calendar afterSevenYears = Calendar.getInstance();
        afterSevenYears.setTime(licenseDate);
        afterSevenYears.add(Calendar.YEAR, 7);
        Calendar afterEightYears = Calendar.getInstance();
        afterEightYears.setTime(licenseDate);
        afterEightYears.add(Calendar.YEAR, 8);
        Calendar afterNineYears = Calendar.getInstance();
        afterNineYears.setTime(licenseDate);
        afterNineYears.add(Calendar.YEAR, 9);
        Calendar afterTenYears = Calendar.getInstance();
        afterTenYears.setTime(licenseDate);
        afterTenYears.add(Calendar.YEAR, 10);
        if (now.before(licenseDate)) {
            int days = (int) ((licenseDate.getTime() - now.getTime()) / (1000 * 3600 * 24));
            return days;
        } else {
            if (now.before(afterTwoYears.getTime())) {
                int days = (int) ((afterTwoYears.getTimeInMillis() - now.getTime()) / (1000 * 3600 * 24));
                return days;
            } else {
                if (now.before(afterFourYears.getTime())) {
                    int days = (int) ((afterFourYears.getTimeInMillis() - now.getTime()) / (1000 * 3600 * 24));
                    return days;
                } else {
                    if (now.before(afterSixYears.getTime())) {
                        int days = (int) ((afterSixYears.getTimeInMillis() - now.getTime()) / (1000 * 3600 * 24));
                        return days;
                    } else {
                        if (now.before(afterSevenYears.getTime())) {
                            int days = (int) ((afterSevenYears.getTimeInMillis() - now.getTime()) / (1000 * 3600 * 24));
                            return days;
                        } else {
                            if (now.before(afterEightYears.getTime())) {
                                int days = (int) ((afterEightYears.getTimeInMillis() - now.getTime())
                                        / (1000 * 3600 * 24));
                                return days;
                            } else {
                                if (now.before(afterNineYears.getTime())) {
                                    int days = (int) ((afterNineYears.getTimeInMillis() - now.getTime())
                                            / (1000 * 3600 * 24));
                                    return days;
                                } else {
                                    if (now.before(afterTenYears.getTime())) {
                                        int days = (int) ((afterTenYears.getTimeInMillis() - now.getTime())
                                                / (1000 * 3600 * 24));
                                        return days;
                                    } else {
                                        return 365;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public String deletWXUser(String openId) {
        wxUserDao.deleteUser(openId);
        return JsonResFactory.buildOrg(RESCODE.SUCCESS).toString();
    }

    public boolean isExistUserOpenId(String openId) {
        WXUser wxUser = wxUserDao.findUserByOpenId(openId);
        return wxUser != null;
    }

    public void deleteWXUser(String openId) {
        wxUserDao.deleteUser(openId);
    }

    public JSONObject login(String phone, String openId, String headimgurl, String nickName) {
        nickName = NicknameFilter.filter4BytesUTF8(nickName);
        WXUser wxUser = wxUserDao.findUserByPhone(phone);
        if (wxUser == null) {
            WXUser wxUserNew = new WXUser();
            wxUserNew.setPhone(phone);
            wxUserNew.setHeadimgurl(headimgurl);
            wxUserNew.setNickName(nickName);
            wxUserNew.setOpenId(openId);
            wxUserDao.save(wxUserNew);
        } else {
            wxUser.setHeadimgurl(headimgurl);
            wxUser.setNickName(nickName);
            wxUser.setOpenId(openId);
            wxUserDao.updateUser(wxUser);
        }
        Client exist = clientDao.findByPhone(phone);

        JSONObject obj = new JSONObject();
        if (exist == null) {
            System.out.print(">>>111");
            Client client = new Client();
            client.setPhone(phone);
            client.setName(nickName);
            client.setAge(0);
            client.setCreateDate(new Date());
            client.setConsumAmout(0);
            client.setConsumTimes(0);
            client.setIsMember(false);
            client.setState(0);
            client.setPoints(0);
            System.out.print(">>>" + client);
            clientDao.save(client);
            obj.put(Constants.RESPONSE_CLIENT_KEY,
                    JSONObject.fromObject(client, JsonResFactory.dateConfig(Collection.class)));
            System.out.print(">>>" + obj);
        } else {
            obj.put(Constants.RESPONSE_CLIENT_KEY,
                    JSONObject.fromObject(exist, JsonResFactory.dateConfig(Collection.class)));
        }
        return obj;
        // return JsonResFactory.buildOrg(RESCODE.SUCCESS).toString();
    }

    public String listDiscount(int clientId) {
        Client client = clientDao.findById(clientId);
        if (client == null) {
            return JsonResFactory.buildOrg(RESCODE.NOT_FOUND).toString();
        }
        JsonConfig config = JsonResFactory.dateConfig();
        JsonPropertyFilter filter = new JsonPropertyFilter(Client.class);
        config.setJsonPropertyFilter(filter);
        JSONObject obj = JsonResFactory.buildNet(RESCODE.SUCCESS, Constants.RESPONSE_CLIENT_KEY,
                JSONObject.fromObject(client, config));
        return obj.toString();
    }

    public String setDefaultCar(int carId) {
        Car car = carDao.findById(carId);
        if (car == null) {
            return JsonResFactory.buildOrg(RESCODE.NOT_FOUND).toString();
        }
        for (Car cars : car.getClient().getCars()) {
            cars.setDefaultCar(false);
        }
        car.setDefaultCar(true);
        carDao.update(car);
        return JsonResFactory.buildOrg(RESCODE.SUCCESS).toString();
    }

    public String addCar(Car car) {
        System.out.println("<<<" + car);
        Client client = clientDao.findById((car.getClient()).getId());
        if (client == null) {
            return JsonResFactory.buildOrg(RESCODE.NOT_FOUND).toString();
        }
        Car exist = carDao.findByLicense(car.getLicensePlate());
        if (exist != null) {
            return JsonResFactory.buildOrg(RESCODE.CAR_LICENSE_EXIST).toString();
        }
        car.setCreateDate(new Date());
        if (client.getCars() != null && !client.getCars().isEmpty()) {
            car.setDefaultCar(false);
            client.getCars().add(car);
        } else {
            car.setDefaultCar(true);
            client.getCars().add(car);
        }
        return JsonResFactory.buildOrg(RESCODE.SUCCESS).toString();
    }

    public String deleteCar(int carId) {
        Car car = carDao.findById(carId);
        if (car == null) {
            return JsonResFactory.buildOrg(RESCODE.NO_RECORD).toString();
        } else {
            carDao.deleteById(carId);
            return JsonResFactory.buildOrg(RESCODE.SUCCESS).toString();
        }
    }

    // 返回微信用户信息,client card
    public String detail(int clientId) {
        Client client = clientDao.findById(clientId);
        if (client == null) {
            return JsonResFactory.buildOrg(RESCODE.NOT_FOUND).toString();
        }
        String clientPhone = client.getPhone();

        WXUser wxUser = wxUserDao.findUserByPhone(clientPhone);
        if (null != wxUser) {
            client.setTrueName(wxUser.getName() == null ? "" : wxUser.getName());
            client.setNickName(wxUser.getNickName() == null ? "" : wxUser.getNickName());
        }

        JsonConfig config = JsonResFactory.dateConfig();
        JsonPropertyFilter filter = new JsonPropertyFilter(Client.class);
        config.setJsonPropertyFilter(filter);
        JSONObject obj = JsonResFactory.buildNet(RESCODE.SUCCESS, Constants.RESPONSE_CLIENT_KEY,
                JSONObject.fromObject(client, config));
        List<ConsumOrder> consumOrders = this.consumOrderDao.findWithClientId(clientId);
        if (consumOrders != null) {
            obj.put(Constants.RESPONSE_CONSUMORDER_KEY, JSONArray.fromObject(consumOrders, config));
        }
        return obj.toString();
    }

    public String modifyCar(int clientId, int id, String insuranceCity, String insuranceCompany,
                            String insuranceEndtime, String name, String idNumber) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Client client = clientDao.findById(clientId);
        if (client == null) {
            return JsonResFactory.buildOrg(RESCODE.NOT_FOUND).toString();
        }
        if (name != null && !name.isEmpty() && !name.trim().isEmpty()) {
            client.setName(name);
        }
        if (idNumber != null && !idNumber.isEmpty() && !idNumber.trim().isEmpty()) {
            client.setIdNumber(idNumber);
        }
        Car modify = this.carDao.findById(id);
        modify.setInsuranceCity(insuranceCity);
        modify.setInsuranceCompany(insuranceCompany);
        modify.setInsuranceEndtime(sdf.parse(insuranceEndtime));
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(insuranceEndtime));
        cal.add(Calendar.YEAR, -1);
        modify.setInsuranceStarttime(cal.getTime());
        carDao.update(modify);
        JsonConfig config = JsonResFactory.dateConfig();
        config.registerPropertyExclusions(Car.class, new String[]{"client"});
        JSONObject obj = JsonResFactory.buildNetWithData(RESCODE.SUCCESS, JSONObject.fromObject(modify, config));
        obj.put("name", client.getName());
        obj.put("idNumber", client.getIdNumber());
        log.error(obj.toString());
        return obj.toString();
    }

    public String getPoint(int clientId) {
        Client client = clientDao.findById(clientId);
        if (client == null) {
            return JsonResFactory.buildOrg(RESCODE.NOT_FOUND).toString();
        }
        List<Object[]> exists = pointDao.getPoint(client.getId());
        JsonConfig config = JsonResFactory.dateConfig();
        if (exists == null || exists.isEmpty()) {
            return JsonResFactory.buildOrg(RESCODE.NO_RECORD).toString();
        } else {
            List<PointBean> pointBeans = new ArrayList<>();
            for (Object[] exist : exists) {
                pointBeans.add(new PointBean((int) Math.rint((Double) exist[1]), (Date) exist[0]));
            }
            return JsonResFactory
                    .buildOrg(RESCODE.SUCCESS, Constants.RESPONSE_POINT_KEY, JSONArray.fromObject(pointBeans, config))
                    .toString();
        }
    }

    // point、wxuser、discount,复用性极差
    public String getWXUser(String openId) {
        WXUser wxUser = wxUserDao.findUserByOpenId(openId);
        if (wxUser == null) {
            return JsonResFactory.buildOrg(RESCODE.NOT_FOUND).toString();
        }
        Client client = clientDao.findByPhone(wxUser.getPhone());
        if (client == null) {
            return JsonResFactory.buildOrg(RESCODE.NOT_FOUND).toString();
        }
        // Object favour =
        // favourRemainingsDao.getCountByClientId(client.getId());
        Object point = pointDao.getSumPoint(client.getId());
        JSONObject obj = new JSONObject();
        // if(favour==null){
        // favour=0;
        // }
        if (point == null)
            point = 0;
        else
            point = (int) Math.rint((double) (point));
        // obj.put(Constants.RESPONSE_FAVOUR_KEY, favour);
        JsonConfig config = JsonResFactory.dateConfig();
        obj.put("point", point);
        obj.put(Constants.RESPONSE_WXUSER_KEY, JSONObject.fromObject(wxUser, config));

        return JsonResFactory.buildNetWithData(RESCODE.SUCCESS, obj).toString();
    }

    /**
     * 根据手机号获取WXUser
     * @param phone 手机号
     * @return  json
     */
    public String getWXUserByPhone(String phone) {
        WXUser wxUser = wxUserDao.findUserByPhone(phone);
        if (wxUser == null) {
            return JsonResFactory.buildOrg(RESCODE.NOT_FOUND).toString();
        }
        Client client = clientDao.findByPhone(wxUser.getPhone());
        if (client == null) {
            return JsonResFactory.buildOrg(RESCODE.NOT_FOUND).toString();
        }
        Object point = pointDao.getSumPoint(client.getId());
        JSONObject obj = new JSONObject();
        if (point == null) {
            point = 0;
        } else {
            point = (int) Math.rint((double) (point));
        }
        JsonConfig config = JsonResFactory.dateConfig();
        obj.put("point", point);
        obj.put(Constants.RESPONSE_WXUSER_KEY, JSONObject.fromObject(wxUser, config));
        return JsonResFactory.buildNetWithData(RESCODE.SUCCESS, obj).toString();
    }

    public String listCard(int clientId) {
        List<Card> Cards = cardDao.listCardByClientId(clientId);
        System.out.println(Cards.size());
        JsonConfig config = JsonResFactory.dateConfig();
        JSONArray array = JSONArray.fromObject(Cards, config);
        return JsonResFactory.buildNetWithData(RESCODE.SUCCESS, array).toString();
    }

    public String listCar(int clientId) {
        List<Car> cars = carDao.findByClientId(clientId);
        JsonConfig config = JsonResFactory.dateConfig();
        config.registerPropertyExclusions(Car.class, new String[]{"client"});
        // JsonPropertyFilter filter = new JsonPropertyFilter();
        // config.setJsonPropertyFilter(filter);
        List<Object> list = new ArrayList<Object>();

        for (Car car : cars) {

            JSONObject obj = new JSONObject();
            Date today = new Date();
            long result = 0;
            if (car.getInsuranceStarttime() == null) {
                result = 365;
            } else {
                long intervalMilli = today.getTime() - car.getInsuranceStarttime().getTime();
                result = 365 - (intervalMilli / (24 * 60 * 60 * 1000));
                System.out.println(">>>>>" + result);
            }
            obj.put("car", JSONObject.fromObject(car, config));
            int day = -1;
            if (car.getLicenseDate() == null) {
                day = -1;
            } else {
                day = daysbetween(today, car.getLicenseDate());
            }
            obj.put("time", result);
            obj.put("day", day);
            obj.put("defaultCar", car.isDefaultCar());
            list.add(obj);
        }
        JSONArray.fromObject(list, config);
        return JsonResFactory.buildNetWithData(RESCODE.SUCCESS, list).toString();
    }

    public String carDetail(int carId) {
        Car exist = carDao.findById(carId);
        if (exist == null) {
            return JsonResFactory.buildOrg(RESCODE.NOT_FOUND).toString();
        } else {
            Client client = exist.getClient();
            JsonConfig config = JsonResFactory.dateConfig();
            config.registerPropertyExclusions(Car.class, new String[]{"client"});
            JSONObject obj = JsonResFactory.buildNetWithData(RESCODE.SUCCESS, JSONObject.fromObject(exist, config));
            obj.put("name", client.getName());
            obj.put("idNumber", client.getIdNumber());
            return obj.toString();
        }
    }

    public String addWXUser(String phone, String name, Date birthday, String gender) throws ParseException {
        WXUser wxUser = wxUserDao.findUserByPhone(phone);
        if (wxUser == null) {
            return JsonResFactory.buildNet(RESCODE.NOT_FOUND_WXUSER).toString();
        } else {
            if (birthday == null) {
                wxUser.setBirthday(null);
            } else {
                wxUser.setBirthday(birthday);
            }
            if (name == null) {
                wxUser.setName(wxUser.getNickName());
            } else {
                wxUser.setName(name);
            }
            wxUser.setGender(gender);
            return JsonResFactory.buildNet(RESCODE.SUCCESS).toString();
        }
    }

    public String annualCheck(int clientId, int id, String licenseDate) throws ParseException {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Client client = clientDao.findById(clientId);
        if (client == null) {
            return JsonResFactory.buildOrg(RESCODE.NOT_FOUND).toString();
        }
        Car modify = this.carDao.findById(id);
        modify.setLicenseDate(sdf.parse(licenseDate));
        carDao.update(modify);
        JsonConfig config = JsonResFactory.dateConfig();
        config.registerPropertyExclusions(Car.class, new String[]{"client"});
        JSONObject obj = JsonResFactory.buildNetWithData(RESCODE.SUCCESS, JSONObject.fromObject(modify, config));
        obj.put("day", daysbetween(now, sdf.parse(licenseDate)));
        return obj.toString();
    }

    public String quickOrder(int clientId) {
        ConsumOrder consumOrder = consumOrderDao.getRecentlyOrder(clientId);
        if (consumOrder == null) {
            return JsonResFactory.buildOrg(RESCODE.NO_RECORD).toString();
        } else {
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
            JsonConfig config = JsonResFactory.dateConfig();
            config.registerPropertyExclusions(ConsumOrder.class, new String[]{"projects"});
            JSONObject obj = JSONObject.fromObject(consumOrder, config);
            obj.put("projects", projectsForRemaining);
            return JsonResFactory.buildOrg(RESCODE.SUCCESS, Constants.RESPONSE_CONSUMORDER_KEY, obj).toString();
        }
    }

    public String insuranceRemind(int carId, boolean check) {
        //
        Car car = carDao.findById(carId);
        if (car == null) {
            return JsonResFactory.buildOrg(RESCODE.NO_RECORD).toString();
        } else {
            car.setNeedInsuranceRemind(check);
            return JsonResFactory.buildOrg(RESCODE.SUCCESS).toString();
        }
    }

    public String annualRemind(int carId, boolean check) {
        Car car = carDao.findById(carId);
        if (car == null) {
            return JsonResFactory.buildOrg(RESCODE.NO_RECORD).toString();
        } else {
            car.setNeedInspectionRemind(check);
            return JsonResFactory.buildOrg(RESCODE.SUCCESS).toString();
        }
    }
}
