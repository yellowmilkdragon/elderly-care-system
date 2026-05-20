package com.neusoft.elderlycare.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.neusoft.elderlycare.common.BaseEntity;

import java.time.LocalDate;

@TableName("backdown")
public class Backdown extends BaseEntity {
    private String remarks;
    private Long customerId;
    private LocalDate retreatTime;
    private Integer retreatType;
    private String retreatReason;
    private Integer auditStatus;
    private String auditPerson;
    private LocalDate auditTime;

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public LocalDate getRetreatTime() {
        return retreatTime;
    }

    public void setRetreatTime(LocalDate retreatTime) {
        this.retreatTime = retreatTime;
    }

    public Integer getRetreatType() {
        return retreatType;
    }

    public void setRetreatType(Integer retreatType) {
        this.retreatType = retreatType;
    }

    public String getRetreatReason() {
        return retreatReason;
    }

    public void setRetreatReason(String retreatReason) {
        this.retreatReason = retreatReason;
    }

    public Integer getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getAuditPerson() {
        return auditPerson;
    }

    public void setAuditPerson(String auditPerson) {
        this.auditPerson = auditPerson;
    }

    public LocalDate getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(LocalDate auditTime) {
        this.auditTime = auditTime;
    }
}
