package com.geariot.platform.freelycar_wechat.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.geariot.platform.freelycar_wechat.entities.ConsumOrder;
import com.geariot.platform.freelycar_wechat.entities.Favour;
import com.geariot.platform.freelycar_wechat.entities.Project;
import com.geariot.platform.freelycar_wechat.entities.Store;
import com.geariot.platform.freelycar_wechat.entities.StoreProject;
import com.geariot.platform.freelycar_wechat.model.RESCODE;
import com.geariot.platform.freelycar_wechat.utils.JsonResFactory;
import com.geariot.platform.freelycar_wechat.utils.StoreUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import com.geariot.platform.freelycar_wechat.utils.Constants;
import com.geariot.platform.freelycar_wechat.dao.ConsumOrderDao;
import com.geariot.platform.freelycar_wechat.dao.FavourDao;
import com.geariot.platform.freelycar_wechat.dao.StoreDao;



@Service
@Transactional
public class StoreService {

	@Autowired
	private StoreDao storeDao;
	
	@Autowired
	private ConsumOrderDao consumOrderDao;
	
	@Autowired
	private FavourDao favourDao;
	
	public String listStore(int page, int number){
		int from = (page - 1) * number;
		List<Store> list = storeDao.listStore(from , number);
		if(list == null || list.isEmpty()){
			return JsonResFactory.buildOrg(RESCODE.NO_RECORD).toString();
		}
		else{
			long realSize = (long) storeDao.getCount();
			int size=(int) Math.ceil(realSize/(double)number);
			JsonConfig config = JsonResFactory.dateConfig();
			List<JSONObject> array = new ArrayList<JSONObject>();
			for(Store store:list){
				JSONObject obj = new JSONObject();
				obj.put(Constants.RESPONSE_STORE_KEY,JSONObject.fromObject(store, config));
				obj.put(Constants.RESPONSE_STAR_KEY,StoreUtil.getCommentStar(store.getId()));
				System.out.println(">>>"+obj);
				array.add(obj);
			}
			net.sf.json.JSONObject obj = JsonResFactory.buildNetWithData(RESCODE.SUCCESS, array);
			obj.put(Constants.RESPONSE_SIZE_KEY, size);
			obj.put(Constants.RESPONSE_REAL_SIZE_KEY,realSize);
			return obj.toString();
		}
		
	}
	
	public String detail(int storeId) {
		Store exist = storeDao.findStoreById(storeId);
		if (exist == null) {
			return JsonResFactory.buildOrg(RESCODE.NO_RECORD).toString();
		} else {
			JsonConfig config = JsonResFactory.dateConfig();
			JSONObject obj = new JSONObject();
			List<StoreProject> projects =  new ArrayList<StoreProject>(exist.getStoreProjects());
			ArrayList<Project> array1 = new ArrayList<Project>();
			ArrayList<Project> array2 = new ArrayList<Project>();
			for(StoreProject storeproject:projects){
				Project project = storeproject.getProject();
				if(project.getProgram().getId()==3) {
					array1.add(project);
				} else {
					array2.add(project);
				}
			}
			obj.put("beauty", array1);
			obj.put("fix", array2);
			config.registerPropertyExclusion(Store.class, "storeProjects");
			obj.put(Constants.RESPONSE_STORE_KEY,JSONObject.fromObject(exist, config));
			obj.put(Constants.RESPONSE_STAR_KEY,StoreUtil.getCommentStar(exist.getId()));
			return JsonResFactory.buildNetWithData(RESCODE.SUCCESS, obj)
					.toString();
		}
	}
	
	public String listComment(int storeId){
		List<ConsumOrder> consumOrders = consumOrderDao.findCommentByStoreId(storeId);
		JsonConfig config = JsonResFactory.dateConfig();
		return JsonResFactory.buildNetWithData(RESCODE.SUCCESS, JSONArray.fromObject(consumOrders, config)).toString();
	}
	
	public String favourDetail(int favourId){
		Favour favour = favourDao.findById(favourId);
		if(favour == null){
			return JsonResFactory.buildOrg(RESCODE.NO_RECORD).toString();
		}else{
			return JsonResFactory.buildNetWithData(RESCODE.SUCCESS, JSONObject.fromObject(favour,JsonResFactory.dateConfig())).toString();
		}
	}
}
