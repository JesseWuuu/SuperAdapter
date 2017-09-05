package com.jessewu.library.builder;
/*
 * ===========================
 * MultiViewBuilder     2017/08/29
 *      
 * Created by JesseWu
 * ===========================
 */

public interface MultiViewBuilder<T>  {

    int getLayout(int type);

    int getItemType(int position,T data);



}
