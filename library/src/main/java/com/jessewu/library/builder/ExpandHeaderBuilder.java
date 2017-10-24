package com.jessewu.library.builder;

import com.jessewu.library.view.ViewHolder;

import java.util.List;

/*
 * ===========================
 * ExpandHeaderBuilder     2017/10/23
 *      
 * Created by Jesse Wu
 * ===========================
 */
public interface ExpandHeaderBuilder<P,C> extends Builder {


    int getHeaderLayoutType(P entity,int position,int headerPosition);

    int getHeaderLayoutId(int type);

    int getChildrenLayoutType(C entity,int position,int childrenPosition);

    int getChildrenLayoutId(int type);

    void bindChildrenView(ViewHolder holder, C entity,int position,int childrenPosition);

    List<C> getChildrenList(P entity);



}
