package com.geariot.platform.freelycar_wechat.dao;

import com.geariot.platform.freelycar_wechat.entities.Cabinet;

import java.util.List;
import java.util.Map;

/**
 * @author 唐炜
 */
public interface CabinetDao {

    int save(Cabinet cabinet);

    void saveOrUpdate(Cabinet cabinet);

    void delete(Cabinet cabinet);

    Cabinet findById(int id);

    Cabinet findBySn(String sn);

    List<Cabinet> query(Map<String, Object> paramMap, int from, int pageSize);

    long getCabinetCount(Map<String, Object> paramMap);
}
