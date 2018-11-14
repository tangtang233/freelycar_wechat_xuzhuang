package com.geariot.platform.freelycar_wechat.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import net.sf.json.JsonConfig;

import com.geariot.platform.freelycar_wechat.utils.query.FavourOrderBean;
import com.geariot.platform.freelycar_wechat.wxutils.IdentifyOrder;
import com.geariot.platform.freelycar_wechat.wxutils.WechatTemplateMessage;
import com.geariot.platform.freelycar_wechat.utils.JsonResFactory;
import com.geariot.platform.freelycar_wechat.utils.Constants;
import com.geariot.platform.freelycar_wechat.utils.DateHandler;
import com.geariot.platform.freelycar_wechat.dao.*;
import com.geariot.platform.freelycar_wechat.entities.*;
import com.geariot.platform.freelycar_wechat.model.RESCODE;
import com.geariot.platform.freelycar_wechat.utils.*;

@org.springframework.stereotype.Service
@Transactional
public class PayService {

	@Autowired
	private WXUserDao wxUserDao;
	
	@Autowired
	private ServiceDao serviceDao;
	
	@Autowired
	private WXPayOrderDao wxPayOrderDao;
	
	@Autowired
	private ConsumOrderDao consumOrderDao;
	
	@Autowired
	private ClientDao clientDao;
	
	@Autowired
	private IncomeOrderDao incomeOrderDao;
	
	@Autowired
	private CardDao cardDao;
	
	@Autowired
	private FavourDao favourDao;

	private static final Logger log = LogManager.getLogger(PayService.class);

	// 创建card订单
	public org.json.JSONObject createCardOrder(String openId, float totalPrice, int serviceId) {
		log.debug("create new order");
		WXPayOrder wxPayOrder = buildBasivOrders(openId, totalPrice);
		log.debug("id" + wxPayOrder.getId() + "总金额" + wxPayOrder.getTotalPrice() + "openId" + wxPayOrder.getOpenId()
				+ "Date" + wxPayOrder.getCreateDate());
		Service service = serviceDao.findServiceById(serviceId);
		wxPayOrder.setService(service);
		wxPayOrder.setProductName(service.getName());
		wxPayOrderDao.saveWXPayOrder(wxPayOrder);

		org.json.JSONObject order = new org.json.JSONObject();
		order.put(Constants.RESPONSE_DATA_KEY, wxPayOrder.getId());
		return order;

	}

	// create favour order
	public org.json.JSONObject createFavourOrder(FavourOrderBean favourOrderBean) {
		String openId = favourOrderBean.getOpenId();
		float totalPrice = favourOrderBean.getTotalPrice();
		Set<FavourToOrder> favours = favourOrderBean.getFavours();
		WXPayOrder wxPayOrder = buildBasivOrders(openId, totalPrice);
		String productName = "";
		for (FavourToOrder favour : favours){
			Favour obj = favourDao.findById(favour.getFavour().getId());
			log.error(obj.getName());
			productName += obj.getName() + "*" + favour.getCount() + ",";
		}
		wxPayOrder.setProductName(productName);
		wxPayOrder.setFavours(favours);
		wxPayOrderDao.saveWXPayOrder(wxPayOrder);
		org.json.JSONObject order = new org.json.JSONObject();
		order.put(Constants.RESPONSE_DATA_KEY, wxPayOrder.getId());
		return order;
		// return
		// JsonResFactory.buildNetWithData(RESCODE.SUCCESS,net.sf.json.JSONObject.fromObject(obj,
		// config)).toString();

	}

	private WXPayOrder buildBasivOrders(String openId, float totalPrice) {
		WXPayOrder wxPayOrder = new WXPayOrder();
		WXUser wxUser = wxUserDao.findUserByOpenId(openId);
		Client client = clientDao.findByPhone(wxUser.getPhone());
		wxPayOrder.setId(IDGenerator.generate(IDGenerator.WX_CONSUM));
		System.out.println(">>>>" + IDGenerator.generate(IDGenerator.WX_CONSUM));
		wxPayOrder.setCreateDate(new Date());
		wxPayOrder.setOpenId(openId);
		wxPayOrder.setPayMethod(Constants.PAY_BY_WX);
		wxPayOrder.setPayState(Constants.PAY_UNPAY);
		wxPayOrder.setTotalPrice(totalPrice);
		wxPayOrder.setClientId(client.getId());
		wxPayOrderDao.saveWXPayOrder(wxPayOrder);
		return wxPayOrder;
	}

	public org.json.JSONObject paySuccess(String orderId) {
		log.debug("付款成功，进入paySuccess后续处理。");
		float amount;
		int clientId;
		String licensePlate = null;
		Date payDate = new Date();
		int payMethod = Constants.PAY_BY_WX;
		String programName;
		if (IdentifyOrder.identify(orderId)) {
			ConsumOrder order = null;
			synchronized (PayService.class) {
				order = consumOrderDao.findById(orderId);
				if (order.getState() >= 1) {
					log.debug("已处理微信回调，订单已处理。直接返回成功。");
					org.json.JSONObject res = new org.json.JSONObject();
					res.put(Constants.RESPONSE_CODE_KEY, RESCODE.SUCCESS);
					res.put(Constants.RESPONSE_MSG_KEY, RESCODE.SUCCESS.getMsg());
					return res;
				}
				order.setPayState(1); // 支付状态
				amount = (float) order.getTotalPrice();
				clientId = order.getClientId();
				licensePlate = order.getLicensePlate();
				programName = order.getProgramName();
			}
			String openId = wxUserDao.findUserByPhone(clientDao.findById(clientId).getPhone()).getOpenId();
			WechatTemplateMessage.paySuccess(order, openId);
		} else {
			WXPayOrder order = null;
			synchronized (PayService.class) {
				order = wxPayOrderDao.findById(orderId);
				if (order.getPayState() >= 1) {
					log.debug("已处理微信回调，订单已处理。直接返回成功。");
					org.json.JSONObject res = new org.json.JSONObject();
					res.put(Constants.RESPONSE_CODE_KEY, RESCODE.SUCCESS);
					res.put(Constants.RESPONSE_MSG_KEY, RESCODE.SUCCESS.getMsg());
					return res;
				}
				order.setPayState(1); // 支付状态
				order.setFinishDate(new Date());
				amount = (float) order.getTotalPrice();
				clientId = clientDao.findByPhone(wxUserDao.findUserByOpenId(order.getOpenId()).getPhone()).getId();
				programName = Constants.WX_CARDANDFAVOUR;
				// 更新card表和favour表
				org.json.JSONObject result;
				Service service = order.getService();
				//List<FavourToOrder> favourToOrders = new ArrayList<FavourToOrder>(order.getFavours());
				if (service != null) {
					Card card = new Card();
					card.setPayDate(payDate);
					card.setService(service);
					card.setExpirationDate(getExpiration(service, payDate));
					card.setPayMethod(payMethod);
					String cardNumber = String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
					while (cardDao.findByCardNumber(cardNumber) != null) {
						cardNumber = String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
					}
					card.setCardNumber("e"+cardNumber);
					result = buyCard(clientId, card);
					if ((RESCODE) result.get(Constants.RESPONSE_CODE_KEY) != RESCODE.SUCCESS) {
						// WechatTemplateMessage.
					}
				} else {
					// buy tickets
					List<Ticket> tickets = new ArrayList<Ticket>();
					for (FavourToOrder favourToOrder : order.getFavours()) {
						Favour favour = favourToOrder.getFavour();
						int count = favourToOrder.getCount();
						for(int i=0;i<count;i++){
							// ticket created
							Ticket ticket = new Ticket();
							ticket.setExpirationDate(getExpiration(favour, payDate));
							ticket.setFailed(false);
							ticket.setFavour(favour);
							Set<FavourProjectRemainingInfo> listRemainingInfos = new HashSet<FavourProjectRemainingInfo>();
							for (FavourProjectInfos favourProjectInfos : favour.getSet()) {
								FavourProjectRemainingInfo remainingInfos = new FavourProjectRemainingInfo();
								remainingInfos.setProject(favourProjectInfos.getProject());
								remainingInfos.setRemaining(favourProjectInfos.getTimes());
								listRemainingInfos.add(remainingInfos);
							}
							ticket.setRemainingInfos(listRemainingInfos);
							tickets.add(ticket);
						}
					}
					Client client = clientDao.findById(clientId);
					
					List<Ticket> list = client.getTickets();
					if (list == null) {
						list = new ArrayList<>();
						client.setTickets(tickets);
					}
					for (Ticket add : tickets) {
						list.add(add);
					}
					client.setConsumTimes(client.getConsumTimes() + 1);
					client.setConsumAmout(client.getConsumAmout() + order.getTotalPrice());
					client.setLastVisit(new Date());
				}
				WechatTemplateMessage.paySuccess(order);
			}
		}

		// 更新IncomeOrder表
		IncomeOrder incomeOrder = new IncomeOrder();
		incomeOrder.setAmount(amount);
		incomeOrder.setClientId(clientId);
		incomeOrder.setLicensePlate(licensePlate);
		incomeOrder.setPayDate(payDate);
		incomeOrder.setPayMethod(payMethod);
		incomeOrder.setProgramName(programName);
		incomeOrderDao.save(incomeOrder);
		org.json.JSONObject res = new org.json.JSONObject();
		res.put(Constants.RESPONSE_CODE_KEY, RESCODE.SUCCESS);
		res.put(Constants.RESPONSE_MSG_KEY, RESCODE.SUCCESS.getMsg());
		return res;
	}

	public Date getExpiration(Object object, Date payDate) {
		Calendar curr = Calendar.getInstance();
		curr.setTime(payDate);
		if (object instanceof Service)
			curr.set(Calendar.MONTH, curr.get(Calendar.MONTH) + ((Service) object).getValidTime());
		if (object instanceof Favour)
			curr.set(Calendar.MONTH, curr.get(Calendar.MONTH) + ((Favour) object).getValidTime());
		Date date = curr.getTime();
		return date;
	}

	public org.json.JSONObject buyCard(int clientId, Card card) {
		Client client = clientDao.findById(clientId);
		Service service = this.serviceDao.findServiceById(card.getService().getId());
		if (client == null || service == null) {
			return JsonResFactory.buildOrg(RESCODE.NOT_FOUND);
		}
		// 将优惠券信息添加到客户卡列表中
		List<Ticket> tickets = new ArrayList<>();
		for (FavourInfos favourInfos : service.getFavourInfos()) {
			Set<FavourProjectRemainingInfo> remainingInfos = new HashSet<>();
			Ticket ticket = new Ticket();
			ticket.setFavour(favourInfos.getFavour());
			ticket.setExpirationDate(DateHandler
					.addValidMonth(DateHandler.toCalendar(new Date()), favourInfos.getFavour().getValidTime())
					.getTime());
			FavourProjectRemainingInfo projectRemainingInfo = new FavourProjectRemainingInfo();
			for (FavourProjectInfos projectInfos : favourInfos.getFavour().getSet()) {
				projectRemainingInfo.setProject(projectInfos.getProject());
				projectRemainingInfo.setRemaining(projectInfos.getTimes());
				remainingInfos.add(projectRemainingInfo);
				log.debug("***************************" + projectRemainingInfo.toString());
			}
			ticket.setRemainingInfos(remainingInfos);
			tickets.add(ticket);
		}
		List<Ticket> list = client.getTickets();
		if (list == null) {
			list = new ArrayList<>();
			client.setTickets(tickets);
		}
		for (Ticket add : tickets) {
			list.add(add);
		}
		// 将服务信息次数复制到卡中
		Set<CardProjectRemainingInfo> cardInfos = new HashSet<>();
		List<ServiceProjectInfo> spi = serviceDao.getListByServiceId(service.getId());
		log.error(spi.size()+"............................."+"spi");
		for (ServiceProjectInfo info : spi) {
			CardProjectRemainingInfo cardInfo = new CardProjectRemainingInfo();
			cardInfo.setProject(info.getProject());
			cardInfo.setRemaining(info.getTimes());
			cardInfos.add(cardInfo);
		}
		card.setProjectInfos(cardInfos);
		// 将新增卡增加到客户卡列表中
		Set<Card> cards = client.getCards();
		if (cards == null) {
			cards = new HashSet<>();
			client.setCards(cards);
		}
		card.setPayDate(new Date());
		Calendar exp = Calendar.getInstance();
		exp.setTime(new Date());
		exp.add(Calendar.YEAR, service.getValidTime());
		card.setExpirationDate(exp.getTime());
		cards.add(card);
		// 更新客户的消费次数与消费情况信息。
		client.setConsumTimes(client.getConsumTimes() + 1);
		client.setConsumAmout(client.getConsumAmout() + service.getPrice());
		client.setLastVisit(new Date());
		client.setIsMember(true);
		return JsonResFactory.buildOrg(RESCODE.SUCCESS);
	}

	public String activityPay(int clientId) {
		Service service = serviceDao.findServiceById(7);
		Client client = clientDao.findById(clientId);
		if (service == null) {
			return JsonResFactory.buildOrg(RESCODE.NOT_FOUND).toString();
		} else {
			// 将优惠券信息添加到客户卡列表中
			List<Ticket> tickets = new ArrayList<>();
			for (FavourInfos favourInfos : service.getFavourInfos()) {
				for (int i = 0; i < favourInfos.getCount(); i++) {
					Set<FavourProjectRemainingInfo> remainingInfos = new HashSet<>();
					Ticket ticket = new Ticket();
					ticket.setFavour(favourInfos.getFavour());
					ticket.setExpirationDate(DateHandler
							.addValidMonth(DateHandler.toCalendar(new Date()), favourInfos.getFavour().getValidTime())
							.getTime());
					FavourProjectRemainingInfo projectRemainingInfo = new FavourProjectRemainingInfo();
					for (FavourProjectInfos projectInfos : favourInfos.getFavour().getSet()) {
						projectRemainingInfo.setProject(projectInfos.getProject());
						projectRemainingInfo.setRemaining(projectInfos.getTimes());
						remainingInfos.add(projectRemainingInfo);
						log.debug("***************************" + projectRemainingInfo.toString());
					}
					ticket.setRemainingInfos(remainingInfos);
					tickets.add(ticket);
				}
			}
			List<Ticket> list = client.getTickets();
			if (list == null) {
				list = new ArrayList<>();
				client.setTickets(tickets);
				;
			}
			for (Ticket add : tickets) {
				list.add(add);
			}
			return JsonResFactory.buildOrg(RESCODE.SUCCESS).toString();
		}
	}
}
