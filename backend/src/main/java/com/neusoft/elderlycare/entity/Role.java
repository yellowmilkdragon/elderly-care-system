package com.neusoft.elderlycare.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.neusoft.elderlycare.common.BaseEntity;

@TableName("role")
public class Role extends BaseEntity {
    private String name;
    private String code;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
