package com.jessewu.library.paging;
/*
 * ===========================
 * LoadDataStatus     2017/09/03
 *      
 * Created by JesseWu
 * ===========================
 */

import java.util.List;

/**
 * 分页加载数据 - 状态控制器
 *
 * 手动调用状态控制器中的方法来控制列表的显示状态
 *
 * 注意：请求数据后一定要至少调用其中一个方法
 *
 * @param <T> 数据源类型
 */
public interface LoadDataStatus<T>{

    /**
     * 当获取到新加载的数据后调用该方法通知 SuperAdapter 将新数据添加到列表中并关闭正在加载数据的状态
     * @param data 获取到数据
     */
    void onSuccess(List<T> data);

    /**
     * 当加载数据失败时调用该方法通知 SuperAdapter 关闭正在加载状态并切换到加载数据失败状态
     * @param msg 加载数据失败的原因提示
     */
    void onFailure(String msg);

    /**
     * 当没用更多的数据时调用该方法通知 SuperAdapter 关闭正在加载状态并切换到没有更多数据状态
     *
     * 注意：调用该方法后，SuperAdapter将不会自动加载新数据
     */
    void onNoMoreData();
}