package com.jessewu.library;
/*
 * ===========================
 * SuperAdapter     2017/08/27
 *
 * Created by JesseWu
 * ===========================
 */

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.jessewu.library.view.ViewHolder;

import java.util.List;

public abstract class SuperAdapter<T> extends RecyclerView.Adapter<ViewHolder> {

    private List<T> mDatas;
    private int mLayoutId;


    public abstract void bindView(ViewHolder itemView,T data);

    public SuperAdapter(int layoutId,List<T> data){

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
