package com.neusoft.elderlycare.common;

import com.baomidou.mybatisplus.annotation.TableField;

import java.time.LocalDateTime;

public abstract class BaseEntity {

    private Long id;
    @TableField("create_time")
    private LocalDateTime createTime;
    @TableField("create_by")
    private Long createBy;
    @TableField("update_time")
    private LocalDateTime updateTime;
    @TableField("update_by")
    private Long updateBy;
    @TableField("is_deleted")
    private Integer isDeleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
}
