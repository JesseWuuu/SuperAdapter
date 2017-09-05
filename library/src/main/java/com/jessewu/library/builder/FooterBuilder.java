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

public interface FooterBuilder<T> extends Builder {

    int getFooterLayoutId();

    void onLoadingData(ViewHolder holder, int loadPage, final LoadDataStatus<T> loadDataStatus);

    void onLoadingFailure(ViewHolder holder,String msg);

    void onNoMoreData(ViewHolder holder);

}
