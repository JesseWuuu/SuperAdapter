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

public class ViewHolder extends RecyclerView.ViewHolder {

    private SparseArray<View> views;
    private View mConvertView;


    private ViewHolder(View itemView) {
        super(itemView);
        mConvertView = itemView;
        views = new SparseArray<>();
    }

    public static ViewHolder bindView(ViewGroup parent,int layoutId){
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId,parent,false);
        return new ViewHolder(view);
    }

    public <T extends View>T getView(int viewId){
        View view = views.get(viewId);
        if (view == null){
            view = mConvertView.findViewById(viewId);
            views.put(viewId,view);
        }
        return (T)view;
    }



}
