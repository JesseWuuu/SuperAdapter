package com.jessewu.library.builder;
/*
 * ===========================
 * HeaderBuilder     2017/09/02
 *      
 * Created by JesseWu
 * ===========================
 */

import com.jessewu.library.view.ViewHolder;

public interface HeaderBuilder extends Builder {

    int getHeaderLayoutId();

    void bindHeaderView(ViewHolder viewHolder);

}
