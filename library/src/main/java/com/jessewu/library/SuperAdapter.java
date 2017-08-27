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

import com.jessewu.library.options.CommOption;
import com.jessewu.library.options.MultiViewOption;
import com.jessewu.library.view.ViewHolder;

import java.util.List;

public abstract class SuperAdapter<T> extends RecyclerView.Adapter<ViewHolder> {

    private List<T> mDatas;
    private int mLayoutId;


    public abstract void bindView(ViewHolder itemView, T data);

    public SuperAdapter(CommOption commOption){

    }

    public SuperAdapter(MultiViewOption multiViewOption){

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
    }


}
