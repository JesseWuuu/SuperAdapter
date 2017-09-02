package com.jessewu.superadapter.data;
/*
 * ===========================
 * TestEntity     2017/09/02
 *      
 * Created by JesseWu
 * ===========================
 */

public class TestEntity {

    private String title;
    private String subTitle;
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }


    @Override
    public String toString() {
        return "TestEntity{" +
                "title='" + title + '\'' +
                ", subTitle='" + subTitle + '\'' +
                ", type=" + type +
                '}';
    }
}
