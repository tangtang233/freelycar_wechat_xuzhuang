package com.geariot.platform.freelycar_wechat.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.geariot.platform.freelycar_wechat.utils.JsonDateDeserialize;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Project {
    private int id;
    private String name;
    private float price;
    private int referWorkTime;
    private float pricePerUnit;
    private String comment;
    private int useTimes;
    private List<ProjectInventoriesInfo> inventoryInfos;
    @JsonDeserialize(using = JsonDateDeserialize.class)
    private Date createDate;
    private Program program;

    /**
     * 智能柜上架状态
     */
    private Integer saleStatus;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinColumn(name = "projectId", foreignKey = @ForeignKey(name = "none"))
    public List<ProjectInventoriesInfo> getInventoryInfos() {
        return inventoryInfos;
    }

    public void setInventoryInfos(List<ProjectInventoriesInfo> inventoryInfos) {
        this.inventoryInfos = inventoryInfos;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(float pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    @ManyToOne
    @JoinColumn(name = "programId", foreignKey = @ForeignKey(name = "none"))
    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public int getReferWorkTime() {
        return referWorkTime;
    }

    public void setReferWorkTime(int referWorkTime) {
        this.referWorkTime = referWorkTime;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public int getUseTimes() {
        return useTimes;
    }

    public void setUseTimes(int useTimes) {
        this.useTimes = useTimes;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getSaleStatus() {
        return saleStatus;
    }

    public void setSaleStatus(Integer saleStatus) {
        this.saleStatus = saleStatus;
    }

    @Override
    public String toString() {
        return "Project [id=" + id + ", name=" + name + ", price=" + price + ", referWorkTime=" + referWorkTime
                + ", pricePerUnit=" + pricePerUnit + ", comment=" + comment + ", inventoryInfos=" + inventoryInfos
                + ", createDate=" + createDate + ", program=" + program + "]";
    }

    /**
     * inner class
     */
    public static class ProjectInner {
        private int id;
        private String name;

        public ProjectInner() {
        }

        public ProjectInner(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
