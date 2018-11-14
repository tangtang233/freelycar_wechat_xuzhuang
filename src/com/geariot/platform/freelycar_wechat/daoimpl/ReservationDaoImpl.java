package com.geariot.platform.freelycar_wechat.daoimpl;

import com.geariot.platform.freelycar_wechat.dao.ReservationDao;
import com.geariot.platform.freelycar_wechat.entities.Reservation;
import com.geariot.platform.freelycar_wechat.utils.Constants;
import com.geariot.platform.freelycar_wechat.utils.hibernate.BaseDaoImpl;
import com.geariot.platform.freelycar_wechat.utils.query.QueryUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 唐炜
 */
@Repository
public class ReservationDaoImpl extends BaseDaoImpl<Integer, Reservation> implements ReservationDao {

    private static final String ORDER_IDENTIFICATION = "ASC";
    private static final String INVERTED_ORDER_IDENTIFICATION = "DESC";

    public ReservationDaoImpl() {
        super(Reservation.class);
    }

    @Override
    public Reservation findById(Integer reservationId) {
        String hql = "from Reservation where id = :id";
        return (Reservation) this.getSession().createQuery(hql).setInteger("id", reservationId).setCacheable(Constants.SELECT_CACHE).uniqueResult();
    }

    @Override
    public Reservation findByConsumOrderId(String consumOrderId) {
        String hql = "from Reservation where consumOrderId = :consumOrderId";
        return (Reservation) this.getSession().createQuery(hql).setString("consumOrderId", consumOrderId).setCacheable(Constants.SELECT_CACHE).uniqueResult();
    }

    @Override
    public List<Reservation> query(Map<String, Object> paramMap, Integer from, Integer pageSize) {
        String licensePlate = null;
        String name = null;
        String openId = null;
        Integer state = null;
        String sortColumn = null;
        String sortType = null;
        if (null != paramMap) {
            licensePlate = (String) paramMap.get("licensePlate");
            name = (String) paramMap.get("name");
            openId = (String) paramMap.get("openId");
            state = (Integer) paramMap.get("state");
            sortColumn = (String) paramMap.get("sortColumn");
            sortType = (String) paramMap.get("sortType");
        }

        String hql = "from Reservation";
        QueryUtils qutils = new QueryUtils(getSession(), hql);

        //查询条件
        if (StringUtils.isNotEmpty(licensePlate)) {
            qutils.addStringLike("licensePlate", licensePlate);
        }
        if (StringUtils.isNotEmpty(name)) {
            qutils.addStringLike("name", name);
        }
        if (StringUtils.isNotEmpty(openId)) {
            qutils.addStringLike("openId", openId);
        }
        if (null != state) {
            qutils.addInteger("state", state);
        }

        //排序
        if (StringUtils.isNotEmpty(sortColumn)) {
            if (StringUtils.isNotEmpty(sortType) && ORDER_IDENTIFICATION.equalsIgnoreCase(sortType)) {
                qutils.addOrderByAsc(sortColumn);
            } else {
                qutils.addOrderByDesc(sortColumn);
            }
        } else {
            if (StringUtils.isNotEmpty(sortType) && ORDER_IDENTIFICATION.equalsIgnoreCase(sortType)) {
                qutils.addOrderByAsc("createTime");
            } else {
                qutils.addOrderByDesc("createTime");
            }
        }

        //如果没有分页参数，则全部查询出来
        if (null == from || null == pageSize) {
            return qutils.getQuery().list();
        }
        Query query = qutils.setFirstResult(from).setMaxResults(pageSize).getQuery();
        return query.list();
    }

    @Override
    public List<Reservation> queryAll(Map<String, Object> paramMap) {
        return query(paramMap, null, null);
    }

    @Override
    public long getReservationCount(Map<String, Object> paramMap) {
        String licensePlate = null;
        String name = null;
        String openId = null;
        Integer state = null;
        if (null != paramMap) {
            licensePlate = (String) paramMap.get("licensePlate");
            name = (String) paramMap.get("name");
            openId = (String) paramMap.get("openId");
            state = (Integer) paramMap.get("state");
        }

        String hql = "select count(*) from Reservation";
        QueryUtils qutils = new QueryUtils(getSession(), hql);
        //查询条件
        if (StringUtils.isNotEmpty(licensePlate)) {
            qutils.addStringLike("licensePlate", licensePlate);
        }
        if (StringUtils.isNotEmpty(name)) {
            qutils.addStringLike("name", name);
        }
        if (StringUtils.isNotEmpty(openId)) {
            qutils.addStringLike("openId", openId);
        }
        if (null != state) {
            qutils.addInteger("state", state);
        }
        Query query = qutils.getQuery();
        return (long) query.uniqueResult();
    }

    @Override
    public List<Reservation> loadTheBookingOrder(String openId) {
        String hql = "from Reservation where state not in(4,5) and openId=:openId order by id desc";
        Map<String, Object> params = new HashMap<>(1);
        params.put("openId", openId);
        return findList(hql, params);
    }

    @Override
    public List<Reservation> loadTheBookingOrderByClientId(String clientId) {
        String hql = "from Reservation where state not in(4,5) and clientId=:clientId order by id desc";
        Map<String, Object> params = new HashMap<>(1);
        params.put("clientId", Integer.valueOf(clientId));
        return findList(hql, params);
    }

    /**
     * 指定的智能柜正在进行的预约的数量
     * @param cabinetSN 智能柜编号
     * @return long 数量
     */
    @Override
    public long theCountInProgress(String cabinetSN) {
        String sql = "select count(id) as num from Reservation where cabinetSN=:cabinetSN and state in (0,1,2)";
        QueryUtils queryUtils = new QueryUtils(getSession(), sql);
        queryUtils.addString("cabinetSN", cabinetSN);
        Query query = queryUtils.getQuery();
        return (long) query.uniqueResult();
    }
}
