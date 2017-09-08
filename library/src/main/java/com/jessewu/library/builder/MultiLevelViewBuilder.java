package com.jessewu.library.builder;

/**
 * Created by Jesse Wu on 2017/09/08.
 */

public interface MultiLevelViewBuilder<T> extends Builder {

    /**
     * 获取分类header的布局文件id
     */
    int getSubHeaderViewLayout();

    /**
     * 获取itemView的布局文件id
     */
    int getItemViewLayout();


    int getItemViewType();


}
