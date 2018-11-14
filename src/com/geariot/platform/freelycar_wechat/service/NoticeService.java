package com.geariot.platform.freelycar_wechat.service;

import com.geariot.platform.freelycar_wechat.dao.NoticeDao;
import com.geariot.platform.freelycar_wechat.entities.Notice;
import com.geariot.platform.freelycar_wechat.model.RESCODE;
import com.geariot.platform.freelycar_wechat.utils.hibernate.BaseServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * @author 唐炜
 */
@Service
@Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
public class NoticeService {
    private static final Logger log = LogManager.getLogger(NoticeService.class);

    @Autowired
    private NoticeDao noticeDao;

    public Notice addNotice(Notice notice) {
        if (null != notice) {
            return noticeDao.saveOrUpdate(notice);
        }
        return null;
    }

    public Map<String, Object> readNotice(String id) {
        if (StringUtils.isEmpty(id)) {
            log.error("参数id为空！");
            return RESCODE.WRONG_PARAM.getJSONRES();
        }
        Notice notice = noticeDao.findById(id);
        if (null == notice) {
            log.error(RESCODE.NOT_FOUND);
            return RESCODE.NOT_FOUND.getJSONRES();
        }
        notice.setIsRead(1L);
        Notice noticeHasRead = noticeDao.update(notice);
        return RESCODE.SUCCESS.getJSONRES(noticeHasRead);
    }

    /**
     * 根据表名和对应主键，删除通知
     * @param tableName
     * @param dataId
     * @return
     */
    public boolean deleteNoticeByTableNameAndDataId(String tableName, String dataId) {
        if (StringUtils.isEmpty(tableName)) {
            log.error("删除通知失败：参数tableName为空！");
            return false;
        }
        if (StringUtils.isEmpty(dataId)) {
            log.error("删除通知失败：参数dataId为空！");
            return false;
        }
        Notice notice = noticeDao.findNoticeByTableNameAndDataId(tableName, dataId);
        if (null != notice) {
            notice.setDelStatus(Notice.DELETE);
            noticeDao.update(notice);
            return true;
        }
        return false;
    }
}
