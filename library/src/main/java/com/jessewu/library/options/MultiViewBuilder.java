package com.jessewu.library.options;
/*
 * ===========================
 * MultiViewBuilder     2017/08/29
 *      
 * Created by JesseWu
 * ===========================
 */

public interface MultiViewBuilder<T> extends Builder {

    int getlayout(int type);

    int getItemType(int position,T data);



}
