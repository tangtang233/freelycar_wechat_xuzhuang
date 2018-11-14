package com.geariot.platform.freelycar_wechat.entities;


import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 实体类：通知表
 * @author 唐炜
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "NOTICE")
public class Notice implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 0：有效（未删除状态）；1：无效（删除状态）
     */
    public final static long UNDELETE = 0L;
    public final static long DELETE = 1L;

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name = "ID")
    private String id;

    @Column(name = "DELSTATUS")
    private Long delStatus;

    @Column(name = "ISREAD")
    private Long isRead;

    @Column(name = "CONTENT")
    private String content;

    @Column(name = "ADMINID")
    private String adminId;

    @Column(name = "ADMINNAME")
    private String adminName;

    @Column(name = "TABLENAME")
    private String tableName;

    @Column(name = "DATAID")
    private String dataId;

    @Column(name = "ADMINACCOUNT")
    private String adminAccount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(Long delStatus) {
        this.delStatus = delStatus;
    }

    public Long getIsRead() {
        return isRead;
    }

    public void setIsRead(Long isRead) {
        this.isRead = isRead;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getAdminAccount() {
        return adminAccount;
    }

    public void setAdminAccount(String adminAccount) {
        this.adminAccount = adminAccount;
    }
}
