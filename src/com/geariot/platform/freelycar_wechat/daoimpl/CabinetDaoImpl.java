package com.geariot.platform.freelycar_wechat.daoimpl;

import com.geariot.platform.freelycar_wechat.dao.CabinetDao;
import com.geariot.platform.freelycar_wechat.entities.Cabinet;
import com.geariot.platform.freelycar_wechat.utils.Constants;
import com.geariot.platform.freelycar_wechat.utils.query.QueryUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author 唐炜
 */
@Repository
public class CabinetDaoImpl implements CabinetDao {
    @Autowired
    private SessionFactory sessionFactory;

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public int save(Cabinet cabinet) {
        return (int) this.getSession().save(cabinet);
    }

    @Override
    public void saveOrUpdate(Cabinet cabinet) {
        this.getSession().saveOrUpdate(cabinet);
    }

    @Override
    public void delete(Cabinet cabinet) {
        this.getSession().delete(cabinet);
    }

    @Override
    public Cabinet findById(int id) {
        String hql = "from Cabinet where id = :id";
        return (Cabinet) this.getSession().createQuery(hql).setInteger("id", id).setCacheable(Constants.SELECT_CACHE).uniqueResult();
    }

    @Override
    public Cabinet findBySn(String sn) {
        String hql = "from Cabinet where sn = :sn";
        return (Cabinet) this.getSession().createQuery(hql).setString("sn", sn).setCacheable(Constants.SELECT_CACHE).uniqueResult();
    }

    @Override
    public List<Cabinet> query(Map<String, Object> paramMap, int from, int pageSize) {
        String sn = null;
        if (null != paramMap && !paramMap.isEmpty()) {
            sn = (String) paramMap.get("sn");
        }

        String hql = "from Cabinet";
        QueryUtils queryUtils = new QueryUtils(getSession(), hql);

        //查询条件
        if (StringUtils.isNotEmpty(sn)) {
            queryUtils.addStringLike("sn", sn);
        }

        //排序
        queryUtils.addOrderByAsc("createTime");
        Query query = queryUtils.setFirstResult(from).setMaxResults(pageSize).getQuery();
        return query.list();
    }

    @Override
    public long getCabinetCount(Map<String, Object> paramMap) {
        String sn = null;
        if (null != paramMap && !paramMap.isEmpty()) {
            sn = (String) paramMap.get("sn");
        }

        String hql = "select count(*) from Cabinet";
        QueryUtils queryUtils = new QueryUtils(getSession(), hql);
        //查询条件
        if (StringUtils.isNotEmpty(sn)) {
            queryUtils.addStringLike("sn", sn);
        }
        Query query = queryUtils.getQuery();
        return (long) query.uniqueResult();
    }
}
