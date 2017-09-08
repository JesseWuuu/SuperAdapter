package com.jessewu.library.builder;
/*
 * ===========================
 * MultiItemViewBuilder     2017/08/29
 *      
 * Created by JesseWu
 * ===========================
 */

public interface MultiItemViewBuilder<T>  {

    int getLayout(int type);

    int getItemType(int position,T data);



}
