package com.geariot.platform.freelycar_wechat.service;

import com.geariot.platform.freelycar_wechat.dao.ProjectDao;
import com.geariot.platform.freelycar_wechat.entities.Project;
import com.geariot.platform.freelycar_wechat.model.RESCODE;
import com.geariot.platform.freelycar_wechat.utils.JsonResFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Service
@Transactional
public class ProjectService {

    public final static int SOLD_OUT = 0;
    public final static int ON_SALE = 1;

    @Autowired
    private ProjectDao projectDao;

    /**
     * 查询在智能柜上架销售的服务
     * @return map
     */
    public Map<String, Object> listProjectOnSale() {
        List<Project> list = projectDao.listProjectOnSale();
        return JsonResFactory.buildNetWithData(RESCODE.SUCCESS, list);
    }

}
