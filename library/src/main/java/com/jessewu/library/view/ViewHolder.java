package com.jessewu.library.view;
/*
 * ===========================
 * ViewHolder     2017/08/27
 *
 * Created by JesseWu
 * ===========================
 */


import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 通用ViewHolder
 */
public class ViewHolder extends RecyclerView.ViewHolder {

    // 布局中子view缓存
    private SparseArray<View> views;

    // 根布局
    private View mConvertView;


    private ViewHolder(View itemView) {
        super(itemView);
        mConvertView = itemView;
        views = new SparseArray<>();
    }

    /**
     * 绑定item view布局，生成view holder
     */
    public static ViewHolder bindView(ViewGroup parent,int layoutId){
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId,parent,false);
        return new ViewHolder(view);
    }

    /**
     *  通过id 获取item view中的view 控件
     * @param <T> 控件类型
     */
    public <T extends View>T getView(int viewId){
        View view = views.get(viewId);
        if (view == null){
            view = mConvertView.findViewById(viewId);
            views.put(viewId,view);
        }
        return (T)view;
    }



}
