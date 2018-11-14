package com.geariot.platform.freelycar_wechat.dao;

import com.geariot.platform.freelycar_wechat.entities.Reservation;
import com.geariot.platform.freelycar_wechat.utils.hibernate.BaseDaoInter;

import java.util.List;
import java.util.Map;

/**
 * @author 唐炜
 */
public interface ReservationDao extends BaseDaoInter<Integer,Reservation> {
//    void saveOrUpdate(Reservation reservation);

//    Reservation save(Reservation reservation);

//    Reservation findById(Integer reservationId);

    Reservation findByConsumOrderId(String consumOrderId);

    List<Reservation> query(Map<String, Object> paramMap, Integer from, Integer pageSize);

    List<Reservation> queryAll(Map<String, Object> paramMap);

    long getReservationCount(Map<String, Object> paramMap);

    List<Reservation> loadTheBookingOrder(String openId);

    List<Reservation> loadTheBookingOrderByClientId(String clientId);

    long theCountInProgress(String cabinetSN);
}
