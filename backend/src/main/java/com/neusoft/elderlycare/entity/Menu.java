package com.neusoft.elderlycare.entity;

import com.baomidou.mybatisplus.annotation.TableName;

@TableName("menu")
public class Menu {
    private Long id;
    private String menusIndex;
    private String title;
    private String icon;
    private String path;
    private Long parentId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMenusIndex() {
        return menusIndex;
    }

    public void setMenusIndex(String menusIndex) {
        this.menusIndex = menusIndex;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}
