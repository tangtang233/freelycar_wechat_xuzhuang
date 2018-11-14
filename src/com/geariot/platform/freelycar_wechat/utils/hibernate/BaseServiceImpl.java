package com.geariot.platform.freelycar_wechat.utils.hibernate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;

/**
 * BaseServiceImpl：service基础类
 * @author 唐炜
 * @param <K>
 * @param <T>
 */
@Transactional(propagation=Propagation.REQUIRED,rollbackFor = Exception.class)
public abstract class BaseServiceImpl<K extends Serializable,T extends Serializable> implements BaseServiceInter<K,T> {

    /**
     * 从容器中注入session工厂【无需get,set方法】
     */
    @Autowired
    private BaseDaoInter<K,T> baseDao;

    @Override
    public K save(T t) {
        return baseDao.save(t);
    }

    @Override
    public void saveAll(Collection<T> ct) {
        baseDao.saveAll(ct);
    }

    @Override
    public T findById(K id) {
        return baseDao.findById(id);
    }

    @Override
    public T update(T t) {
        return baseDao.update(t);
    }

    @Override
    public T saveOrUpdate(T t) {
        return baseDao.saveOrUpdate(t);
    }

    @Override
    public void delete(T t) {
        baseDao.delete(t);
    }

    @Override
    public void deleteAll(Collection<T> ct) {
        baseDao.deleteAll(ct);
    }

    @Override
    public boolean deleteById(K id) {
        return baseDao.deleteById(id);
    }

    @Override
    public QueryResult<T> loadAll() {
        return baseDao.loadAll();
    }

    @Override
    public QueryResult<T> load(int page, int rows) {
        return baseDao.load(page, rows);
    }

    @Override
    public long getTotalCount() {
        return baseDao.getTotalCount();
    }
    /******************************HQL******************************/

    @Override
    public QueryResult<T> getScrollData() {
        return baseDao.getScrollData();
    }

    @Override
    public QueryResult<T> getScrollData(int firstResult, int maxResult) {
        return baseDao.getScrollData(firstResult, maxResult);
    }

    @Override
    public QueryResult<T> getScrollData(int firstResult, int maxResult,
                                        LinkedHashMap<String, String> orderBy) {
        return baseDao.getScrollData(firstResult, maxResult, orderBy);
    }

    @Override
    public QueryResult<T> getScrollData(int firstResult, int maxResult,
                                        String where, Object[] params) {
        return baseDao.getScrollData(firstResult, maxResult, where, params);
    }

    @Override
    public QueryResult<T> getScrollData(int firstResult, int maxResult,
                                        String where, Object[] params, LinkedHashMap<String, String> orderBy) {
        return baseDao.getScrollData(firstResult, maxResult, where, params, orderBy);
    }

}

