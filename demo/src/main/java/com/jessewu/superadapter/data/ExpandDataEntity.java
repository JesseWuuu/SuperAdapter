package com.jessewu.superadapter.data;

import java.util.List;

/*
 * ===========================
 * ExpandDataEntity     2017/10/24
 *      
 * Created by Jesse Wu
 * ===========================
 */
public class ExpandDataEntity {

    private String title;

    private List<TestEntity> children;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<TestEntity> getChildren() {
        return children;
    }

    public void setChildren(List<TestEntity> children) {
        this.children = children;
    }
}
