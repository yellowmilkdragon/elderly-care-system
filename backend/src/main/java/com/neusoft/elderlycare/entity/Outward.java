package com.neusoft.elderlycare.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.neusoft.elderlycare.common.BaseEntity;

import java.time.LocalDate;

@TableName("outward")
public class Outward extends BaseEntity {
    private String remarks;
    private Long customerId;
    private String outgoingReason;
    private LocalDate outgoingTime;
    private LocalDate expectedReturnTime;
    private LocalDate actualReturnTime;
    private String escorted;
    private String relation;
    private String escortedTel;
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

    public String getOutgoingReason() {
        return outgoingReason;
    }

    public void setOutgoingReason(String outgoingReason) {
        this.outgoingReason = outgoingReason;
    }

    public LocalDate getOutgoingTime() {
        return outgoingTime;
    }

    public void setOutgoingTime(LocalDate outgoingTime) {
        this.outgoingTime = outgoingTime;
    }

    public LocalDate getExpectedReturnTime() {
        return expectedReturnTime;
    }

    public void setExpectedReturnTime(LocalDate expectedReturnTime) {
        this.expectedReturnTime = expectedReturnTime;
    }

    public LocalDate getActualReturnTime() {
        return actualReturnTime;
    }

    public void setActualReturnTime(LocalDate actualReturnTime) {
        this.actualReturnTime = actualReturnTime;
    }

    public String getEscorted() {
        return escorted;
    }

    public void setEscorted(String escorted) {
        this.escorted = escorted;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getEscortedTel() {
        return escortedTel;
    }

    public void setEscortedTel(String escortedTel) {
        this.escortedTel = escortedTel;
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
