package com.jessewu.library.paging;

/*
 * ===========================
 * LoadDataListener     2017/10/10
 *      
 * Created by Jesse Wu
 * ===========================
 */

/**
 * 分页加载数据 - 加载数据监听器
 *
 * @param <T> 数据源类型
 */
public interface LoadDataListener<T> {

    /**
     * 加载更多数据
     *
     * 当列表滑动到底部时调用该方法自动加载更多数据
     *
     * @param loadPage 需要请求数据的页数
     * @param loadDataStatus 加载数据的状态控制器
     */
    void onLoadingData(int loadPage, final LoadDataStatus<T> loadDataStatus);
}
