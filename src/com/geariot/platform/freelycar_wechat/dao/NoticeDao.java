package com.geariot.platform.freelycar_wechat.dao;


import com.geariot.platform.freelycar_wechat.entities.Notice;
import com.geariot.platform.freelycar_wechat.utils.hibernate.BaseDaoInter;

public interface NoticeDao extends BaseDaoInter<String, Notice> {

    Notice findNoticeByTableNameAndDataId(String tableName, String dataId);
}
