package com.geariot.platform.freelycar_wechat.daoimpl;

import com.geariot.platform.freelycar_wechat.dao.ConsumOrderDao;
import com.geariot.platform.freelycar_wechat.entities.ConsumExtraInventoriesInfo;
import com.geariot.platform.freelycar_wechat.entities.ConsumOrder;
import com.geariot.platform.freelycar_wechat.entities.ProjectInfo;
import com.geariot.platform.freelycar_wechat.model.ORDER_CON;
import com.geariot.platform.freelycar_wechat.utils.Constants;
import com.geariot.platform.freelycar_wechat.utils.query.QueryUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public class ConsumOrderDaoImpl implements ConsumOrderDao {

    private static final Logger log = LogManager.getLogger(ConsumOrderDaoImpl.class);

    @Autowired
    private SessionFactory sessionFactory;

    private Session getSession() {
        return this.sessionFactory.getCurrentSession();
    }

    @Override
    public void save(ConsumOrder consumOrder) {
        log.info(consumOrder);
        Session session = this.getSession();
        session.save(consumOrder);
    }

    @Override
    public void saveOrUpdate(ConsumOrder consumOrder) {
        log.info(consumOrder);
        this.getSession().saveOrUpdate(consumOrder);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ConsumOrder> list(int from, int pageSize) {
        String hql = "from ConsumOrder order by createDate desc";
        return this.getSession().createQuery(hql).setFirstResult(from).setMaxResults(pageSize)
                .setCacheable(Constants.SELECT_CACHE).list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ConsumOrder> listByClientId(int clientId, int from, int pageSize) {
        String hql = "from ConsumOrder where clientId= :clientId order by createDate desc";
        return this.getSession().createQuery(hql).setInteger("clientId", clientId).setFirstResult(from).setMaxResults(pageSize)
                .setCacheable(Constants.SELECT_CACHE).list();
    }

    @Override
    public long getCount() {
        String hql = "select count(*) from ConsumOrder";
        return (long) this.getSession().createQuery(hql).setCacheable(Constants.SELECT_CACHE).uniqueResult();
    }

    @Override
    public long getCountByClientId(int clientId) {
        String hql = "select count(*) from ConsumOrder where clientId = :clientId";
        return (long) this.getSession().createQuery(hql).setInteger("clientId", clientId).setCacheable(Constants.SELECT_CACHE).uniqueResult();
    }

    @Override
    public ConsumOrder findById(String consumOrderId) {
        String hql = "from ConsumOrder where id = :id";
        return (ConsumOrder) this.getSession().createQuery(hql).setString("id", consumOrderId)
                .setCacheable(Constants.SELECT_CACHE).uniqueResult();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ConsumOrder> query(String andCondition, int from, int pageSize) {
        StringBuffer basic = new StringBuffer("from ConsumOrder");
        StringBuffer temp = QueryUtils.createQueryString(basic, andCondition, ORDER_CON.NO_ORDER);
        temp.append(" order by createDate desc");
        String hql = temp.toString();
        log.debug("订单查询最后生成查询语句：" + hql);
        return this.getSession().createQuery(hql).setFirstResult(from).setMaxResults(pageSize)
                .setCacheable(Constants.SELECT_CACHE).list();
    }

    @Override
    public long getQueryCount(String andCondition) {
        StringBuffer basic = new StringBuffer("select count(*) from ConsumOrder");
        String hql = QueryUtils.createQueryString(basic, andCondition, ORDER_CON.NO_ORDER).toString();
        return (long) this.getSession().createQuery(hql).setCacheable(Constants.SELECT_CACHE).uniqueResult();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> getConsumOrderIdsByStaffId(int staffId) {
        String sql = "select consumOrdersId from consumorders_staff where staffId=:id";
        return this.getSession().createSQLQuery(sql).setInteger("id", staffId).setCacheable(Constants.SELECT_CACHE).list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ConsumOrder> findWithClientId(int clientId) {
        String hql = "from ConsumOrder where clientId = :clientId order by createDate desc";
        return this.getSession().createQuery(hql).setInteger("clientId", clientId)
                .setCacheable(Constants.SELECT_CACHE).list();
    }


    @SuppressWarnings("unchecked")
    @Override
    public List<ConsumOrder> findByMakerAccount(String account) {
        String hql = "from ConsumOrder where orderMaker.account = :account order by createDate desc";
        return this.getSession().createQuery(hql).setString("account", account)
                .setCacheable(Constants.SELECT_CACHE).list();
    }

    @Override
    public long countInventoryInfoByIds(List<String> inventoryIds) {
        String hql = "select count(*) from ConsumExtraInventoriesInfo where inventory.id in :list";
        return (long) this.getSession().createQuery(hql).setParameterList("list", inventoryIds)
                .setCacheable(Constants.SELECT_CACHE).uniqueResult();
    }

    @Override
    public void removeStaffInConsumOrderStaffs(int staffId) {
        String sql = "delete from projectinfo_staff where staffId = :id";
        this.getSession().createSQLQuery(sql).setInteger("id", staffId).executeUpdate();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> programNameToday() {
        Date date = new Date();
        Calendar cal1 = Calendar.getInstance();
        cal1.setTimeInMillis(date.getTime());
        cal1.set(Calendar.HOUR_OF_DAY, 0);
        cal1.set(Calendar.MINUTE, 0);
        cal1.set(Calendar.SECOND, 0);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTimeInMillis(date.getTime());
        cal2.set(Calendar.HOUR_OF_DAY, 0);
        cal2.set(Calendar.MINUTE, 0);
        cal2.set(Calendar.SECOND, 0);
        cal2.add(Calendar.DAY_OF_MONTH, 1);
        String hql = "select sum(amount) , programName , count(*) from IncomeOrder where payDate >= :date1 and payDate < :date2 group by programName";
        return this.getSession().createSQLQuery(hql).setTimestamp("date1", cal1.getTime()).setTimestamp("date2", cal2.getTime())
                .setCacheable(Constants.SELECT_CACHE).list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> programNameMonth() {
        Date date = new Date();
        Calendar cal1 = Calendar.getInstance();
        cal1.setTimeInMillis(date.getTime());
        cal1.set(Calendar.DATE, 1);
        cal1.set(Calendar.HOUR_OF_DAY, 0);
        cal1.set(Calendar.MINUTE, 0);
        cal1.set(Calendar.SECOND, 0);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTimeInMillis(date.getTime());
        cal2.set(Calendar.HOUR_OF_DAY, 0);
        cal2.set(Calendar.MINUTE, 0);
        cal2.set(Calendar.SECOND, 0);
        cal2.set(Calendar.DATE, 1);
        cal2.add(Calendar.MONTH, 1);
        String hql = "select sum(amount) , programName , count(*) from IncomeOrder where payDate >= :date1 and payDate < :date2 group by programName";
        return this.getSession().createSQLQuery(hql).setTimestamp("date1", cal1.getTime()).setTimestamp("date2", cal2.getTime())
                .setCacheable(Constants.SELECT_CACHE).list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> programNameRange(Date startTime, Date endTime) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTimeInMillis(startTime.getTime());
        cal1.set(Calendar.DATE, 1);
        cal1.set(Calendar.HOUR_OF_DAY, 0);
        cal1.set(Calendar.MINUTE, 0);
        cal1.set(Calendar.SECOND, 0);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTimeInMillis(endTime.getTime());
        cal2.set(Calendar.HOUR_OF_DAY, 23);
        cal2.set(Calendar.MINUTE, 59);
        cal2.set(Calendar.SECOND, 59);
        String hql = "select sum(amount) , programName , count(*) from IncomeOrder where payDate >= :date1 and payDate < :date2 group by programName";
        return this.getSession().createSQLQuery(hql).setTimestamp("date1", cal1.getTime()).setTimestamp("date2", cal2.getTime())
                .setCacheable(Constants.SELECT_CACHE).list();
    }


    @Override
    public List<ConsumOrder> findByPickCarStaffId(int staffId) {
        String hql = "from ConsumOrder where pickCarStaff.id = :id";
        @SuppressWarnings("unchecked")
        List<ConsumOrder> res = this.getSession().createQuery(hql).setInteger("id", staffId).setCacheable(Constants.SELECT_CACHE).list();
        return res;
    }

    @Override
    public void update(ConsumOrder consumOrder) {
        this.getSession().update(consumOrder);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ConsumOrder> findCommentByStoreId(int storeId) {
        String hql = "from ConsumOrder where storeId = :storeId and commentDate != null order by commentDate desc";
        List<ConsumOrder> res = this.getSession().createQuery(hql).setInteger("storeId", storeId).setCacheable(Constants.SELECT_CACHE).list();
        return res;
    }

    @Override
    public ConsumOrder getRecentlyOrder(int clientId) {
        String hql = "from ConsumOrder where clientId = :clientId order by createDate desc";
        return (ConsumOrder) this.getSession().createQuery(hql).setInteger("clientId", clientId).setMaxResults(1).setCacheable(Constants.SELECT_CACHE).uniqueResult();
    }

    @Override
    public void updateOrder(ConsumOrder consumOrder) {
        getSession().update(consumOrder);
    }

    @Override
    public ConsumOrder updateOrderConsutom(ConsumOrder consumOrder) {
        ConsumOrder update = getSession().get(consumOrder.getClass(), consumOrder.getId());

        if (update != null) {
            update.setProgramId(consumOrder.getProgramId());
            update.setProgramName(consumOrder.getProgramName());
            update.setTotalPrice(consumOrder.getTotalPrice());
            update.setPresentPrice(consumOrder.getPresentPrice());
            update.setFaultDesc(consumOrder.getFaultDesc());
            update.setRepairAdvice(consumOrder.getRepairAdvice());
            update.setActualPrice(consumOrder.getActualPrice());
            update.setPickCarStaff(consumOrder.getPickCarStaff());

            for (ProjectInfo p : consumOrder.getProjects()) {
                p.setId(0);
            }
            update.getProjects().clear();
            update.getProjects().addAll(consumOrder.getProjects());

            for (ConsumExtraInventoriesInfo p : consumOrder.getInventoryInfos()) {
                p.setId(0);
            }

            update.getInventoryInfos().clear();
            update.getInventoryInfos().addAll(consumOrder.getInventoryInfos());

            updateOrder(update);
        }
        return update;
    }

    @Override
    public List<ConsumOrder> list(Map<String, Object> paramMap, int from, int pageSize) {
        Integer clientId = null;
        Integer state = null;
        if (null != paramMap && !paramMap.isEmpty()) {
            clientId = (Integer) paramMap.get("clientId");
            state = (Integer) paramMap.get("state");
        }

        String hql = "from ConsumOrder order";

        QueryUtils queryUtils = new QueryUtils(getSession(), hql);
        if (null != clientId) {
            queryUtils.addInteger("clientId", clientId);
        }
        if (null != state) {
            queryUtils.addInteger("state", state);
        }
        queryUtils.addOrderByDesc("createDate");

        Query resultQuery = queryUtils.setFirstResult(from).setMaxResults(pageSize).getQuery();

        return resultQuery.list();
    }
    @Override
    public long getConsumOrderCount(Map<String, Object> paramMap) {
        Integer clientId = null;
        Integer state = null;
        if (null != paramMap && !paramMap.isEmpty()) {
            clientId = (Integer) paramMap.get("clientId");
            state = (Integer) paramMap.get("state");
        }

        String hql = "select count(*) from ConsumOrder";

        QueryUtils queryUtils = new QueryUtils(getSession(), hql);
        if (null != clientId) {
            queryUtils.addInteger("clientId", clientId);
        }
        if (null != state) {
            queryUtils.addInteger("state", state);
        }
        queryUtils.addOrderByDesc("createDate");

        Query resultQuery = queryUtils.getQuery();

        return (long) resultQuery.uniqueResult();
    }

}

