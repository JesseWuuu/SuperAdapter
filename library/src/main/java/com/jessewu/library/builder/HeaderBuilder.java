package com.jessewu.library.builder;
/*
 * ===========================
 * HeaderBuilder     2017/09/02
 *      
 * Created by JesseWu
 * ===========================
 */

import com.jessewu.library.view.ViewHolder;

/**
 * 列表唯一头部构造器
 *
 * 与分类头部不同，该布局在列表中只有一个且永远处在列表的最上方
 */
public interface HeaderBuilder extends Builder {

    /**
     *  获取头部布局文件id
     */
    int getHeaderLayoutId();

    /**
     *  header布局界面逻辑处理
     */
    void bindHeaderView(ViewHolder holder);

}
