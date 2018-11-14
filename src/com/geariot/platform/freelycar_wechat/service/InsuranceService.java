package com.geariot.platform.freelycar_wechat.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.geariot.platform.freelycar_wechat.model.RESCODE;
import com.geariot.platform.freelycar_wechat.utils.JsonResFactory;
import com.geariot.platform.freelycar_wechat.dao.InsuranceOrderDao;
import com.geariot.platform.freelycar_wechat.entities.InsuranceOrder;

import net.sf.json.JsonConfig;

@Service
@Transactional
public class InsuranceService {

	@Autowired
	private InsuranceOrderDao insuranceOrderDao;
	
	public String askInsurance(InsuranceOrder insuranceOrder){
		InsuranceOrder exist = this.insuranceOrderDao.findByLicensePlate(insuranceOrder.getLicensePlate());
		if(exist == null){
			insuranceOrder.setCreateDate(new Date());
			this.insuranceOrderDao.save(insuranceOrder);
			JsonConfig config = JsonResFactory.dateConfig();
			return JsonResFactory.buildNetWithData(RESCODE.SUCCESS,net.sf.json.JSONObject.fromObject(insuranceOrder, config)).toString();
		}
		else{
			if(new Date().getTime() - exist.getCreateDate().getTime() > 15 * 86400000){
				exist.setCreateDate(new Date());
				this.insuranceOrderDao.update(exist);
				JsonConfig config = JsonResFactory.dateConfig();
				return JsonResFactory.buildNetWithData(RESCODE.SUCCESS,net.sf.json.JSONObject.fromObject(insuranceOrder, config)).toString();
			}
			else{
				return JsonResFactory.buildOrg(RESCODE.INQUIRY_ALREADY).toString();
			}
		}
	}
	
}
