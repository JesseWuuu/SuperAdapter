package com.jessewu.library.builder;
/*
 * ===========================
 * FooterBuilder     2017/09/02
 *      
 * Created by JesseWu
 * ===========================
 */

import com.jessewu.library.status.LoadDataStatus;
import com.jessewu.library.view.ViewHolder;


/**
 * 列表底部构建器
 *
 * 用于配合分页加载数据，控制footer的不同状态
 *
 * @param <T> 数据源类型
 */
public interface FooterBuilder<T> extends Builder {

    /**
     * 获取footer的布局文件id
     */
    int getFooterLayoutId();

    /**
     * 加载更多数据
     *
     * 当列表滑动到底部时调用该方法自动加载更多数据
     *
     * @param loadPage 需要请求数据的页数
     * @param loadDataStatus 加载数据的状态控制器
     */
    void onLoadingData(int loadPage, final LoadDataStatus<T> loadDataStatus);

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
