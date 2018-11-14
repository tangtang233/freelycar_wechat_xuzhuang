package com.geariot.platform.freelycar_wechat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.geariot.platform.freelycar_wechat.entities.InsuranceOrder;
import com.geariot.platform.freelycar_wechat.service.InsuranceService;

@RestController
@RequestMapping(value = "/insurance")
public class InsuranceController {

	@Autowired
	private InsuranceService insuranceService;
	
	@RequestMapping(value = "/ask", method = RequestMethod.POST)
	public String askInsurance(InsuranceOrder insuranceOrder){
		return insuranceService.askInsurance(insuranceOrder);
	}
}
