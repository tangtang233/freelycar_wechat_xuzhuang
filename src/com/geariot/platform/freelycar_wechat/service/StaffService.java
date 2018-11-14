package com.geariot.platform.freelycar_wechat.service;

import com.geariot.platform.freelycar_wechat.dao.StaffDao;
import com.geariot.platform.freelycar_wechat.entities.*;
import com.geariot.platform.freelycar_wechat.model.RESCODE;
import com.geariot.platform.freelycar_wechat.utils.Constants;
import com.geariot.platform.freelycar_wechat.utils.JsonPropertyFilter;
import com.geariot.platform.freelycar_wechat.utils.JsonResFactory;
import com.geariot.platform.freelycar_wechat.utils.MD5;
import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class StaffService {
    private static final Logger log = LogManager.getLogger(StaffService.class);

    @Autowired
    private StaffDao staffDao;

    public Map<String, Object> login(String account, String password) {
        if (StringUtils.isEmpty(account)) {
            log.error("登录失败，参数account为空！");
            return RESCODE.WRONG_PARAM.getJSONRES("登录失败，参数account为空！");
        }
        if (StringUtils.isEmpty(password)) {
            log.error("登录失败，参数password为空！");
            return RESCODE.WRONG_PARAM.getJSONRES("登录失败，参数password为空！");
        }
        Staff staff = staffDao.getStaffByAccountAndPassword(account, password);
        if (null != staff) {
            return RESCODE.SUCCESS.getJSONRES(staff);
        }
        return RESCODE.NOT_FOUND.getJSONRES();
    }


    public String addStaff(Staff staff) {
        Staff exist = staffDao.findStaffByPhone(staff.getPhone());
        if (exist != null) {
            return JsonResFactory.buildOrg(RESCODE.PHONE_EXIST).toString();
        } else {
            staff.setCreateDate(new Date());
            staff.setTechniciansAccount(0);
            staffDao.saveStaff(staff);
            JsonConfig config = JsonResFactory.dateConfig();
            return JsonResFactory.buildNetWithData(RESCODE.SUCCESS, net.sf.json.JSONObject.fromObject(staff, config)).toString();
        }
    }

    public String getStaffList(int page, int number) {
        int from = (page - 1) * number;
        List<Staff> list = staffDao.listStaffs(from, number);
        if (list == null || list.isEmpty()) {
            return JsonResFactory.buildOrg(RESCODE.NO_RECORD).toString();
        }
        long realSize = staffDao.getCount();
        int size = (int) Math.ceil(realSize / (double) number);
        JsonConfig config = JsonResFactory.dateConfig();
        JSONArray jsonArray = JSONArray.fromObject(list, config);
        net.sf.json.JSONObject obj = JsonResFactory.buildNetWithData(RESCODE.SUCCESS, jsonArray);
        obj.put(Constants.RESPONSE_SIZE_KEY, size);
        obj.put(Constants.RESPONSE_REAL_SIZE_KEY, realSize);
        return obj.toString();
    }

    public Map<String, Object> getSelectStaff(String staffId, String staffName, int page, int number) {
        int from = (page - 1) * number;
        List<Staff> list = staffDao.getConditionQuery(staffId, staffName, from, number);
        if (list == null || list.isEmpty()) {
            return RESCODE.NO_RECORD.getJSONRES();
        }
        long count = (long) staffDao.getConditionCount(staffId, staffName);
        int size = (int) Math.ceil(count / (double) number);
        return RESCODE.SUCCESS.getJSONRES(list, size, count);
    }

    public String modifyStaff(Staff staff) {
        Staff exist = staffDao.findStaffByStaffId(staff.getId());
        if (exist == null) {
            return JsonResFactory.buildOrg(RESCODE.NO_RECORD).toString();
        } else {
            if (!org.springframework.util.StringUtils.isEmpty(staff.getName())) {
                exist.setName(staff.getName());
            }
            if (!org.springframework.util.StringUtils.isEmpty(staff.getGender())) {
                exist.setGender(staff.getGender());
            }
            if (!org.springframework.util.StringUtils.isEmpty(staff.getPhone())) {
                exist.setPhone(staff.getPhone());
            }
            if (!org.springframework.util.StringUtils.isEmpty(staff.getPosition())) {
                exist.setPosition(staff.getPosition());
            }
            if (!org.springframework.util.StringUtils.isEmpty(staff.getLevel())) {
                exist.setLevel(staff.getLevel());
            }
            if (!org.springframework.util.StringUtils.isEmpty(staff.getComment())) {
                exist.setComment(staff.getComment());
            }
            if (!org.springframework.util.StringUtils.isEmpty(staff.getTechniciansAccount())) {
                exist.setTechniciansAccount(staff.getTechniciansAccount());
            }
            if (!org.springframework.util.StringUtils.isEmpty(staff.getAccount())) {
                exist.setAccount(staff.getAccount());
            }
            if (!org.springframework.util.StringUtils.isEmpty(staff.getPassword())) {
                exist.setPassword(MD5.compute(staff.getPassword()));
            }
        }
        return JsonResFactory.buildOrg(RESCODE.SUCCESS).toString();
    }

    public String staffServiceDetail(int staffId, int page, int number) {
        Staff exist = staffDao.findStaffByStaffId(staffId);
        if (exist == null) {
            return JsonResFactory.buildOrg(RESCODE.NO_RECORD).toString();
        } else {
            int from = (page - 1) * number;
            List<ProjectInfo> list = staffDao.staffServiceDetails(staffId, from, number);
            if (list == null || list.isEmpty()) {
                net.sf.json.JSONObject obj = JsonResFactory.buildNet(RESCODE.NOT_FOUND);
                obj.put("staffInfo", exist);
                return obj.toString();
            }
            JsonConfig config = JsonResFactory.dateConfig();
            JsonPropertyFilter filter = new JsonPropertyFilter();
            filter.setColletionProperties(Car.class, Card.class, Staff.class, Inventory.class);
            config.setJsonPropertyFilter(filter);
            JSONArray jsonArray = JSONArray.fromObject(list, config);
            net.sf.json.JSONObject obj = JsonResFactory.buildNetWithData(RESCODE.SUCCESS, jsonArray);
            obj.put(Constants.RESPONSE_REAL_SIZE_KEY, list.size());
            obj.put("staffInfo", exist);
            return obj.toString();
        }
    }
}
	

