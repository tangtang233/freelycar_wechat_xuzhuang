package com.geariot.platform.freelycar_wechat.daoimpl;


import com.geariot.platform.freelycar_wechat.dao.NoticeDao;
import com.geariot.platform.freelycar_wechat.entities.Notice;
import com.geariot.platform.freelycar_wechat.utils.hibernate.BaseDaoImpl;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class NoticeDaoImpl extends BaseDaoImpl<String, Notice> implements NoticeDao {
    public NoticeDaoImpl() {
        super(Notice.class);
    }

    /**
     * 通过表名和对应主键查找唯一的通知实体对象
     * @param tableName 表名
     * @param dataId 对应的主键ID
     * @return Notice
     */
    @Override
    public Notice findNoticeByTableNameAndDataId(String tableName, String dataId) {
        String hql = "from Notice where delStatus=0 and tableName = :tableName and dataId = :dataId";
        Map<String, Object> params = new HashMap<>(2);
        params.put("tableName", tableName);
        params.put("dataId", dataId);
        return this.unique(hql, params);
    }
}
