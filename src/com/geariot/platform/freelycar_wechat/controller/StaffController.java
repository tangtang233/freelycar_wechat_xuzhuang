package com.geariot.platform.freelycar_wechat.controller;

import com.geariot.platform.freelycar_wechat.entities.Staff;
import com.geariot.platform.freelycar_wechat.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/staff")
public class StaffController {

	@Autowired
	private StaffService staffService;

	@RequestMapping(value="/login", method=RequestMethod.POST)
	public Map<String,Object> login(@RequestBody Staff staff){
		String account = null;
		String password = null;
		if (null != staff) {
			account = staff.getAccount();
			password = staff.getPassword();
		}
		return staffService.login(account, password);
	}

	@RequestMapping(value="/openStaffLoginAccount", method=RequestMethod.POST)
	public String openStaffLoginAccount(Staff staff){
		return staffService.modifyStaff(staff);
	}
}
