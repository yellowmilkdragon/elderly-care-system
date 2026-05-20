package com.neusoft.elderlycare.entity;

import com.baomidou.mybatisplus.annotation.TableName;

@TableName("room")
public class Room {
    private Long id;
    private String roomFloor;
    private Integer roomNo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoomFloor() {
        return roomFloor;
    }

    public void setRoomFloor(String roomFloor) {
        this.roomFloor = roomFloor;
    }

    public Integer getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(Integer roomNo) {
        this.roomNo = roomNo;
    }
}
