package com.neusoft.elderlycare.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.neusoft.elderlycare.common.BaseEntity;

import java.time.LocalDateTime;

@TableName("nurserecord")
public class NursingRecord extends BaseEntity {
    private Long customerId;
    private Long itemId;
    private LocalDateTime nursingTime;
    private String nursingContent;
    private Integer nursingCount;
    private Long userId;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public LocalDateTime getNursingTime() {
        return nursingTime;
    }

    public void setNursingTime(LocalDateTime nursingTime) {
        this.nursingTime = nursingTime;
    }

    public String getNursingContent() {
        return nursingContent;
    }

    public void setNursingContent(String nursingContent) {
        this.nursingContent = nursingContent;
    }

    public Integer getNursingCount() {
        return nursingCount;
    }

    public void setNursingCount(Integer nursingCount) {
        this.nursingCount = nursingCount;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
