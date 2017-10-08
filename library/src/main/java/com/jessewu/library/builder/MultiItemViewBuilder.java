package com.jessewu.library.builder;
/*
 * ===========================
 * MultiItemViewBuilder     2017/08/29
 *      
 * Created by JesseWu
 * ===========================
 */

/**
 * 多类型item view构造器
 */
public interface MultiItemViewBuilder<T>  {

    /**
     * 通过type 获取item view布局id
     */
    int getLayoutId(int type);

    /**
     * 通过列表的 position 和与 position 对应的数据源获取item type
     */
    int getItemType(int position,T data);



}
