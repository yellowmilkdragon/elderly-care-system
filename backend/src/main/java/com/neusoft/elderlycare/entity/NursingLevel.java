package com.neusoft.elderlycare.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.neusoft.elderlycare.common.BaseEntity;

@TableName("nurselevel")
public class NursingLevel extends BaseEntity {
    private String levelName;
    private Integer levelStatus;

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public Integer getLevelStatus() {
        return levelStatus;
    }

    public void setLevelStatus(Integer levelStatus) {
        this.levelStatus = levelStatus;
    }
}
