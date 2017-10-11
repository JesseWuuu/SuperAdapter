package com.jessewu.library.builder;
/*
 * ===========================
 * FooterBuilder     2017/09/02
 *
 * Created by JesseWu
 * ===========================
 */

import com.jessewu.library.view.ViewHolder;


/**
 * 列表底部构建器
 *
 * 用于配合分页加载数据，控制footer的不同状态
 *
 */
public interface FooterBuilder extends Builder {

    /**
     * 获取footer的布局文件id
     */
    int getFooterLayoutId();

    /**
     *  正常显示的底部栏，并非用于加载更多数据
     */
    void onNormal(ViewHolder holder);

    /**
     * 正在加载数据时footer布局界面的逻辑处理
     */
    void onLoading(ViewHolder holder);

    /**
     * 加载数据失败时footer布局界面逻辑处理
     * @param msg 错误信息
     */
    void onLoadingFailure(ViewHolder holder,String msg);

    /**
     *  数据全部加载完毕或没有更多数据时footer布局界面逻辑处理
     */
    void onNoMoreData(ViewHolder holder);

}
