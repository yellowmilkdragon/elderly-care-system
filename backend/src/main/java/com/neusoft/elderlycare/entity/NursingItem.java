package com.neusoft.elderlycare.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.neusoft.elderlycare.common.BaseEntity;

import java.math.BigDecimal;

@TableName("nursecontent")
public class NursingItem extends BaseEntity {
    private String serialNumber;
    private String nursingName;
    private BigDecimal servicePrice;
    private String message;
    private Integer status;
    private String executionCycle;
    private String executionTimes;

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getNursingName() {
        return nursingName;
    }

    public void setNursingName(String nursingName) {
        this.nursingName = nursingName;
    }

    public BigDecimal getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(BigDecimal servicePrice) {
        this.servicePrice = servicePrice;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getExecutionCycle() {
        return executionCycle;
    }

    public void setExecutionCycle(String executionCycle) {
        this.executionCycle = executionCycle;
    }

    public String getExecutionTimes() {
        return executionTimes;
    }

    public void setExecutionTimes(String executionTimes) {
        this.executionTimes = executionTimes;
    }
}
