package com.geariot.platform.freelycar_wechat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.geariot.platform.freelycar_wechat.service.MembershipCardService;

@RestController
@RequestMapping(value = "/service")
public class MembershipCardController {
	@Autowired
	MembershipCardService menmbershipCardService;
	
	@RequestMapping(value = "/list" , method = RequestMethod.GET)
	public String getMembershipCardList(int page , int number){
		return menmbershipCardService.getMembershipCardList(page, number);
	}
	
	@RequestMapping(value = "/detail" , method = RequestMethod.GET)
	public String getCardDetail(int cardId){
		return menmbershipCardService.getCardDetail(cardId);
	}
}
