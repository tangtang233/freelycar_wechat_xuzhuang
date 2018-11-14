package com.geariot.platform.freelycar_wechat.utils.hibernate;

import java.util.List;

/**
 * 查询结果集
 * @author 唐炜
 * @param <T>
 */
public class QueryResult<T> {
    private List<T> data;
    private Long totalCount;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }
}
