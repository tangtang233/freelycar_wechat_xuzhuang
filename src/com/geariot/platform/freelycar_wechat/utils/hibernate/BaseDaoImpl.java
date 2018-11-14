package com.geariot.platform.freelycar_wechat.utils.hibernate;

import org.hibernate.*;
import org.hibernate.criterion.Projections;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.SessionFactoryUtils;

import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.*;
import java.util.*;

/**
 * 不一定必须是abstract类型的， 请不要对BaseDaoImpl使用@Repository注解，因为无法直接指定clazz属性值
 * class值由继承类来指定
 *
 * BaseDaoImpl：dao实现
 *
 * @author 唐炜
 *
 * @param <T>
 */
public abstract class BaseDaoImpl<K extends Serializable, T extends Serializable> implements BaseDaoInter<K, T> {

    /**
     * 从容器中注入session工厂【无需get,set方法】
     */
    @Autowired
    private SessionFactory sessionFactory;

    /**
     * 【实体类对应的Class对象】
     */
    private Class<T> clazz;

    /**
     * 保留指定clazz值的接口【通过子类显示调用父类的构造函数来指定】
     *
     * @param clazz
     */
    public BaseDaoImpl(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * 设置查询参数
     *
     * @param query  查询对象
     * @param params 参数值
     */
    public static void setQueryParameter(Query query, Object[] params) {
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                query.setParameter(i, params[i]);
            }
        }
    }

    // @SuppressWarnings("unchecked")
    // public BaseDaoImpl() {//另外一种方式指定clazz值，要求类必须是abstract类型
    // ParameterizedType parameterizedType =
    // (ParameterizedType)this.getClass().getGenericSuperclass();
    // clazz= (Class<T>)(parameterizedType.getActualTypeArguments()[0]);
    // }

    /**
     * 构建排序语句
     *
     * @param orderBy 排序属性与asc/desc, Key为属性,Value为asc/desc
     * @return
     */
    public static String buildOrderby(LinkedHashMap<String, String> orderBy) {
        StringBuilder sb = new StringBuilder();
        if (orderBy != null && !orderBy.isEmpty()) {
            sb.append(" order by ");
            for (Map.Entry<String, String> entry : orderBy.entrySet()) {
                sb.append("o.").append(entry.getKey()).append(" ")
                        .append(entry.getValue()).append(',');
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    /**
     * //向子类暴露的接口获用来获取sessionFactory
     *
     * @return sessionFactory
     */
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    @Override
    public Session getSession() {
        return getSessionFactory().getCurrentSession();
    }

    @SuppressWarnings("unchecked")
    @Override
    public K save(T t) {
        Session session = getSession();
        return (K) session.save(t);
    }

    @Override
    public T findById(K id) {
        Session session = getSession();
        @SuppressWarnings("unchecked")
        T t = (T) session.get(clazz, id);
        return t;
    }

    @Override
    public void saveAll(Collection<T> ct) {
        Session session = getSession();
        for (T t : ct) {
            session.save(t);
        }
    }

    @Override
    public T update(T t) {
        Session session = getSession();
        session.update(t);
        return t;
    }

    @Override
    public void deleteAll(Collection<T> ct) {
        Session session = getSession();
        for (T t : ct) {
            session.delete(t);
        }
    }

    @Override
    public T saveOrUpdate(T t) {
        Session session = getSession();
        session.saveOrUpdate(t);
        return t;
    }

    @Override
    public void delete(T t) {
        Session session = getSession();
        session.delete(t);
    }

    @Override
    public boolean deleteById(K id) {
        Session session = getSession();
        @SuppressWarnings("unchecked")
        T t = (T) session.get(clazz, id);
        if (t == null) {
            return false;
        }
        session.delete(t);
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public QueryResult<T> loadAll() {
        Session session = getSession();
        Criteria criteria = session.createCriteria(clazz);
        QueryResult<T> result = new QueryResult<>();
        result.setData(criteria.list());
        result.setTotalCount(Long.parseLong(criteria
                .setProjection(Projections.rowCount()).uniqueResult()
                .toString()));
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public QueryResult<T> load(int page, int rows) {
        Session session = getSession();
        Criteria criteria = session.createCriteria(clazz);
        criteria.setFirstResult((page - 1) * rows);
        criteria.setMaxResults(rows);
        QueryResult<T> result = new QueryResult<>();
        result.setData(criteria.list());
        result.setTotalCount(Long.parseLong(criteria
                .setProjection(Projections.rowCount()).uniqueResult()
                .toString()));
        return result;
    }

    @Override
    public long getTotalCount() {
        Session session = getSession();
        Criteria criteria = session.createCriteria(clazz);
        Object object = criteria.setProjection(Projections.rowCount())
                .uniqueResult();
        long totalCount = 0;
        if (object != null) {
            totalCount = Long.parseLong(object.toString());
        }
        return totalCount;
    }

    /****************************** HQL ******************************/
    @Override
    public QueryResult<T> getScrollData() {
        return getScrollData(-1, -1, null, null, null);
    }

    @Override
    public QueryResult<T> getScrollData(int firstResult, int maxResult) {
        return getScrollData(firstResult, maxResult, null, null, null);
    }

    @Override
    public QueryResult<T> getScrollData(int firstResult, int maxResult,
                                        LinkedHashMap<String, String> orderBy) {
        return getScrollData(firstResult, maxResult, null, null, orderBy);
    }

    @Override
    public QueryResult<T> getScrollData(int firstResult, int maxResult,
                                        String where, Object[] params) {
        return getScrollData(firstResult, maxResult, where, params, null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public QueryResult<T> getScrollData(int firstResult, int maxResult,
                                        String where, Object[] params, LinkedHashMap<String, String> orderBy) {
        String entityName = clazz.getSimpleName();
        String whereql = where != null && !"".equals(where.trim()) ? " where "
                + where : "";
        Session session = getSession();
        Query query = session.createQuery("select o from " + entityName + " o"
                + whereql + buildOrderby(orderBy));
        if (firstResult != -1 && maxResult != -1) {
            query.setFirstResult(firstResult).setMaxResults(maxResult);
        }
        setQueryParameter(query, params);

        QueryResult<T> qr = new QueryResult<T>();
        // qr.setResultlist(query.getResultList());
        Query queryCount = session.createQuery("select count(o) from "
                + entityName + " o" + whereql);
        setQueryParameter(queryCount, params);
        long count = (Long) queryCount.uniqueResult();
        qr.setTotalCount(count);
        qr.setData(query.list());
        return qr;
    }

    /****************************HQL***************************/
    /**
     * 返回存储此对象的主键
     */
    public <T> Serializable save(T o){
        return this.getSession().save(o);
    }

    public <T> void saveByCollection(Collection<T> collection){
        for(T t:collection){
            this.save(t);
        }
    }

    public <T> void update(T o) {
        this.getSession().update(o);
    }


    public <T> void saveOrUpdate(T o) {
        this.getSession().saveOrUpdate(o);
    }
    /**更新一个实体中指定的字段
     * 这里columnNames和columnsValues的名字和值得顺序必须一致;
     * @param t
     * @param columnNames
     * @param columnValues
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchFieldException
     */
    public <T> void updateByColumns(T t,List<String> columnNames,List<?> columnValues) throws NoSuchFieldException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
        String tableNameString=t.getClass().getSimpleName();
        String hqlString="update "+tableNameString+" table_ set ";
        for(int i=0;i<columnNames.size();i++){
            hqlString+=columnNames.get(i)+"="+columnValues.get(i);
        }
        hqlString+=" where table_."+this.getPkName(t.getClass())+"="+this.getPkValue(t).toString();
        this.executeHql(hqlString);
    }

    /**更新一个实体中除开指定的字段之外的字段
     * 这里columnNames和columnsValues的名字和值得顺序必须一致;
     * @param t
     * @param exceptColumnNames
     * @param columnValues
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchFieldException
     */
    public <T> void updateByExceptColumns(T t,List<String> exceptColumnNames,List<?> columnValues) throws NoSuchFieldException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
        List<String> columnNames=this.getEntityColumnNames(t.getClass(), exceptColumnNames);
        String tableNameString=t.getClass().getSimpleName();
        String hqlString="update "+tableNameString+" table_ set ";
        for(int i=0;i<columnNames.size();i++){
            hqlString+=columnNames.get(i)+"="+columnValues.get(i);
        }
        hqlString+=" where table_."+this.getPkName(t.getClass())+"="+this.getPkValue(t).toString();
        this.executeHql(hqlString);
    }

    public <T> T get(Class<T> c, Serializable id) {
        return (T) this.getSession().get(c,id);
    }

    public <T> T get(String hql) {
        return this.get(hql, null);
    }

    public <T> T get(String hql, Map<String, Object> params) {
        Query q = this.getSession().createQuery(hql);
        this.setParameterToQuery(q, params);
        List<T> l = q.list();
        if (l != null && l.size() > 0) {
            return l.get(0);
        }
        return null;
    }

    public <T> void delete(T o) {
        this.getSession().delete(o);
    }
    /**
     * 从数据库中找出此id对象并删除
     * @param entityClass
     * @param id
     */
    public <T, PK extends Serializable> void delete(Class<T> entityClass, PK id) {
        getSession().delete(get(entityClass, id));
    }

    /**hql语句,"delete from "+tableName+" where "+columnName+" in ("+columnValues+")"
     * 	用in语句删除指定表中,包含有指定值得指定列的记录;
     * @param tableName
     * @param columnName
     * @param columnValues 如1,3,4这种in语句参数需要的内容
     * @throws Exception
     */
    public void deleteByColumns(String tableName,String columnName,String columnValues) throws Exception {
        String hql="delete from "+tableName+" where "+columnName+" in ("+columnValues+")";
        this.executeHql(hql);
    }

    /**
     * hql语句,"delete from "+tableName+" where "+columnName+" in ("+columnValues+")"
     * 用in语句删除指定表中,包含有指定值得指定列的记录;
     * @param tableName
     * @param columnName
     * @param columnValues 一个参数值的集合
     */
    public void deleteByColumns(String tableName,String columnName,Collection<?> columnValues) {
        String hql="delete from "+tableName+" where "+columnName+" in (:columnValues)";
        Map<String,Object> params=new HashMap<String, Object>();
        params.put("columnValues",columnValues);
        this.executeHql(hql,params);
    }

    /**
     * 如果有id并存在于数据库中,则更新,否则保存
     * @param model
     */
    public <T> void merge(T model) {
        getSession().merge(model);
    }

    public <T> List<T> findList(String hql) {
        Query q = this.getSession().createQuery(hql);
        return q.list();
    }

    public <T> List<T> findList(String hql, Map<String, Object> params) {
        Query q = this.getSession().createQuery(hql);
        this.setParameterToQuery(q, params);
        return q.list();
    }

    /**
     *
     * @param hql
     * @param params
     * @return
     */
    public T unique(String hql, Map<String, Object> params) {
        Query q = this.getSession().createQuery(hql);
        this.setParameterToQuery(q, params);
        return (T) q.uniqueResult();
    }
    /**
     *
     * @param hql
     * @param topCount 返回前topCount条记录
     * @return
     */
    public <T> List<T> findTopList(String hql, int topCount) {
        // 获取当前页的结果集
        Query query = this.getSession().createQuery(hql);
        query.setFirstResult(0);
        if(topCount<0) {
            topCount=0;
        }
        query.setMaxResults(topCount);
        return  query.list();
    }
    /**
     * 用hql语句,得到当前表的所有记录
     * @param tableName
     * @return
     */
    public <T> List<T> findAll(String tableName){
        String hqlString="select * from "+tableName;
        return this.findList(hqlString);
    }

    /**
     *
     * @param hql
     * @param params
     * @param page 当前页码
     * @param rows 每页显示的记录数量
     * @return
     */
    public <T> List<T> findList(String hql, Map<String, Object> params, int page, int rows) {
        Query q = this.getSession().createQuery(hql);
        this.setParameterToQuery(q, params);
        if(page<1) {
            page=1;
        }
        if(rows<0) {
            rows=0;
        }
        return q.setFirstResult((page - 1) * rows).setMaxResults(rows).list();
    }

    public <T> List<T> findList(String hql, int page, int rows) {
        return this.findList(hql, null, page,rows);
    }

    public Long getCountByHql(String hql) {
        Query q = this.getSession().createQuery(hql);
        return (Long) q.uniqueResult();
    }


    public Long getCountByHql(String hql, Map<String, Object> params) {
        Query q = this.getSession().createQuery(hql);
        this.setParameterToQuery(q, params);
        return (Long) q.uniqueResult();
    }
    /**
     * 根据HQL语句返回一个值,如分布获取总页数
     */
    public Object getCountByHql(String hql, Object... params) {
        Query query = getSession().createQuery(hql);
        this.setParameterToQuery(query, params);
        return query.uniqueResult();
    }

    /**
     * 根据HQL语句，获得查找总记录数的HQL语句 如： select ... from Orgnization o where o.parent is
     * null 经过转换，可以得到： select count(*) from Orgnization o where o.parent is null
     * @param hql
     * @return
     */
    protected String getCountQuery(String hql) {
        int index = hql.toLowerCase().indexOf("from");
        int last = hql.toLowerCase().indexOf("order by");
        if (index != -1) {
            if (last != -1) {
                return "select count(*) " + hql.substring(index, last);
            }
            return "select count(*) " + hql.substring(index);
        }
        return null;
    }

    public  int executeHql(String hql) {
        Query q = this.getSession().createQuery(hql);
        return q.executeUpdate();
    }
    public int executeHql(String hql, Map<String, Object> params) {
        Query q = this.getSession().createQuery(hql);
        this.setParameterToQuery(q, params);
        return q.executeUpdate();
    }

    public int executeHql(String hql,Object... objects) {
        Query q = this.getSession().createQuery(hql);
        this.setParameterToQuery(q, objects);
        return q.executeUpdate();
    }
    /**
     *
     * @param hql
     * @param objects 参数,其顺序应该和?占位符一一对应
     * @return
     */
    public int executeHql(String hql,List<?> objects) {
        Query q = this.getSession().createQuery(hql);
        this.setParameterToQuery(q, objects);
        return q.executeUpdate();
    }
    /**
     * @param q
     * @param params 当前支持普通对象,数组,集合三种类型的参数
     */
    protected void setParameterToQuery(Query q,Map<String, Object> params){
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                if(params.get(key) instanceof Object[]){
                    Object[] objs=(Object[]) params.get(key);
                    q.setParameterList(key, objs);
                }else if(params.get(key) instanceof Collection<?>){
                    Collection<?> collection=(Collection<?>) params.get(key);
                    q.setParameterList(key, collection);
                }else{
                    q.setParameter(key, params.get(key));
                }
            }
        }
    }

    /**
     * @param q
     * @param params 当前支持普通对象,不支持集合与数组
     */
    protected void setParameterToQuery(Query q,Object... params){
        if (params != null && params.length>0) {
            for (int i=0;i<params.length;i++) {
                Object object=params[i];
                q.setParameter(i,object);
            }
        }
    }

    /**
     * @param q
     * @param params 当前支持普通对象,不支持集合与数组
     */
    protected void setParameterToQuery(Query q,List<?> params){
        if (params != null && !params.isEmpty()) {
            for (int i=0;i<params.size();i++) {
                Object object=params.get(i);
                q.setParameter(i,object);
            }
        }
    }

    /****************************************************************
     ******* 上面是和hql相关的操作,下面是和sql相关的操作****************
     ****************************************************************/

    public <T> T getCountBySql(String sql) {
        return this.getCountBySql(sql,new HashMap<String, Object>());
    }
    /**
     * 根据SQL语句返回一个值,如分布获取总页数
     */
    public <T> T getCountBySql(String sql, Object... params) {
        Query query = getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        this.setParameterToQuery(query, params);
        return (T) query.uniqueResult();
    }
    /**
     * 根据SQL语句返回一个值,如分布获取总页数
     */
    public <T> T getCountBySql(String sql,Map<String,Object> params) {
        Query query = getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        this.setParameterToQuery(query, params);
        return (T) query.uniqueResult();
    }

    public List<Map<String, Object>> findListBySql(String sql) {
        return this.findListBySql(sql, new HashMap<String, Object>());
    }
    public List<Map<String, Object>> findListBySql(String sql,Map<String,Object> params) {
        SQLQuery query = this.getSession().createSQLQuery(sql);
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        this.setParameterToQuery(query, params);
        return query.list();
    }
    /**
     * 根据SQL语句返回一个集合
     */
    public List<Map<String, Object>> findListBySql(String sql, Object... params) {
        Query query = getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        this.setParameterToQuery(query, params);
        return query.list();
    }

    /**
     *调用存储过程
     */
    public <T> List<T> execProc(String hql) {
        Query q = this.getSession().getNamedQuery(hql);
        return q.list();
    }

    /**
     * <b>function:</b> 执行原生态的sql语句，添加、删除、修改语句
     * @createDate 2010-8-2 下午05:33:42
     * @author hoojo
     * @param sql
     *            将要执行的sql语句
     * @return int
     * @throws Exception
     */
    public int executeBySql(String sql) throws Exception {
        try {
            return this.getSession().createSQLQuery(sql).executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <T> List<T> callProcedure(String procString, List<Object> params) throws Exception {

        ResultSet rs = null;
        CallableStatement stmt = null;
        try {
            stmt = (CallableStatement)SessionFactoryUtils.getDataSource(this.getSessionFactory()).getConnection()
                    .prepareCall(procString);
            if (params != null) {
                int idx = 1;
                for (Object obj : params) {
                    if (obj != null) {
                        stmt.setObject(idx, obj);
                    } else {
                        stmt.setNull(idx, Types.NULL);
                    }
                    idx++;
                }
            }
            rs = stmt.executeQuery();
            List list = new ArrayList();
            // 得到结果集(rs)的结构信息，比如字段数、字段名等
            ResultSetMetaData md = rs.getMetaData();
            // 返回此 ResultSet 对象中的列数
            int columnCount = md.getColumnCount();
            Map rowData = new HashMap();
            while (rs.next()) {
                rowData = new HashMap(columnCount);
                for (int i = 1; i <= columnCount; i++) {
                    rowData.put(md.getColumnName(i), rs.getObject(i));
                }
                list.add(rowData);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("调用存储过程的时候发生错误[sql = " + procString + "]", e);
        } finally {
            rs.close();
            stmt.close();
        }
    }

    /**
     * 返回此类的列的属性名称,不包含静态属性和Transient
     * @param
     * @return
     */
    private List<String> getEntityColumnNameList(Class<?> cls){
        List<String> list=new ArrayList<String>();
        Class<?> clazz=cls;
        Field[] fs=clazz.getDeclaredFields();
        String filedName=null;
        for(Field field:fs){
            boolean isStatic = Modifier.isStatic(field.getModifiers());
            if(isStatic) {
                continue;
            }
            field.setAccessible(true);
            filedName=field.getName();
            Annotation[] as=field.getAnnotations();
            boolean isTransaction=false;
            for(int i=0;i<as.length;i++){
                Annotation a=as[i];
                if(a instanceof Transient){
                    isTransaction=true;
                    break;
                }
            }
            if(!isTransaction){
                list.add(filedName);
            }
        }
        return list;
    }

    /**
     * 得到除开指定名称的属性列
     */
    protected List<String> getEntityColumnNames(Class<?> cls,String... exceptCoulumns){
        List<String> nameList=getEntityColumnNameList(cls);
        if(exceptCoulumns!=null){
            for(String s:exceptCoulumns){
                nameList.remove(s);
            }
        }
        return nameList;
    }

    /**
     * 得到除开指定名称的属性列
     */
    protected List<String> getEntityColumnNames(Class<?> cls,List<String> exceptCoulumns){
        List<String> nameList=getEntityColumnNameList(cls);
        if(exceptCoulumns!=null){
            for(String s:exceptCoulumns){
                nameList.remove(s);
            }
        }
        return nameList;
    }

    /**
     * 获取主键名称
     *
     * @return 没有逐渐则返回null;
     */
    private String getPkName(Class<?> cls) {
        String pkname=null;
        // 标注在getter方法上
        Method[] methods = cls.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().startsWith("get")) {
                if (method.isAnnotationPresent(Id.class)) {
                    String temp = method.getName().replaceAll("^get", "");
                    // 将第一个字母变成小写
                    pkname = this.firstLetterToLower(temp);
                    break;
                }
            }
        }
        if(pkname==null){
            Field[] fields=cls.getDeclaredFields();
            for(Field field:fields){
                if(field.isAnnotationPresent(Id.class)){
                    return field.getName();
                }
            }
        }
        return pkname;
    }

    private Object getPkValue(Object t) throws NoSuchFieldException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Field field=t.getClass().getField(this.getPkName(t.getClass()));
        try {
            //此方法不需要参数，如：getName(),getAge()
            Method method = t.getClass().getMethod("get"+ this.firstLetterToLower(field.getName()));
            return method.invoke(t);
        } catch (NoSuchMethodException e) {
            return field.get(t);
        }
    }

    private  String firstLetterToLower(String srcString) {
        StringBuilder sb = new StringBuilder();
        sb.append(Character.toLowerCase(srcString.charAt(0)));
        sb.append(srcString.substring(1));
        return sb.toString();
    }

}

