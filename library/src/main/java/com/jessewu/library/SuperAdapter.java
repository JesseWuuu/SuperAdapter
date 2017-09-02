package com.jessewu.library;
/*
 * ===========================
 * SuperAdapter     2017/08/27
 *
 * Created by JesseWu
 * ===========================
 */

import android.util.Log;
import android.view.ViewGroup;

import com.jessewu.library.base.BaseSuperAdapter;
import com.jessewu.library.options.MultiViewBuilder;
import com.jessewu.library.view.ViewHolder;

import java.util.ArrayList;
import java.util.List;

public abstract class SuperAdapter<T> extends BaseSuperAdapter {

    private static final String TAG = "SuperAdapter";

    private List<T> mDatas = new ArrayList<>();

    public abstract void bindView(ViewHolder itemView, T data);


    public SuperAdapter(int layoutId ){
        this.mSingleItemViewLayoutId = layoutId;
    }


    public SuperAdapter(MultiViewBuilder multiViewBuilder){
        this.mMultiViewBuilder = multiViewBuilder;
    }

    public void setData(List<T> datas){
        this.mDatas = datas;
        notifyDataSetChanged();
    }

    public void addData(List<T> datas){
        this.mDatas.addAll(datas);
        notifyDataSetChanged();
    }

     public void addData(T data){
        this.mDatas.add(data);
        notifyDataSetChanged();
    }

    public void removeData(T data){
        this.mDatas.remove(data);
        notifyDataSetChanged();
    }

    public void removeData(int position){
        // TODO 如果有特殊 View 可能会出现问题
        this.mDatas.remove(position);
        notifyDataSetChanged();
    }

    public void clearData(){
        this.mDatas.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: "+mDatas.size());
        return mDatas.size() + getBuilderNum();
    }

    @Override
    public int getItemViewType(int position) {

        if(isMultiItemView()){
            return mMultiViewBuilder.getItemType(position,mDatas.get(position));
        }

        return super.getItemViewType(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        int layoutId = mSingleItemViewLayoutId;

        if (isMultiItemView()){
            layoutId = mMultiViewBuilder.getLayout(viewType);
        }

        return ViewHolder.bindView(parent,layoutId);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        bindView(holder,mDatas.get(position));
    }


}
