package com.neusoft.elderlycare.entity;

import com.baomidou.mybatisplus.annotation.TableName;

@TableName("bed")
public class Bed {
    private Long id;
    private Integer roomNo;
    private Integer bedStatus;
    private String remarks;
    private String bedNo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(Integer roomNo) {
        this.roomNo = roomNo;
    }

    public Integer getBedStatus() {
        return bedStatus;
    }

    public void setBedStatus(Integer bedStatus) {
        this.bedStatus = bedStatus;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getBedNo() {
        return bedNo;
    }

    public void setBedNo(String bedNo) {
        this.bedNo = bedNo;
    }
}
