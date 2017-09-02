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
import com.jessewu.library.options.HeaderBuilder;
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

    public void addHeader(HeaderBuilder headerBuilder){
        this.mHeaderBuilder = headerBuilder;
        this.mSpecialViewBuilder.add(headerBuilder);
    }


    @Override
    public int getItemCount() {
        return mDatas.size() + getSpecialBuilderNum();
    }

    @Override
    public int getItemViewType(int position) {


        if (position == 0 && hasHeaderView()){
            Log.d(TAG, "TYPE_HEAD_SINGLE: ");
            return TYPE_HEAD_SINGLE;
        }

        position = checkPosition(position);

        if(isMultiItemView()){
            return mMultiViewBuilder.getItemType(position,mDatas.get(position));
        }

        return super.getItemViewType(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_HEAD_SINGLE){
            return ViewHolder.bindView(parent,mHeaderBuilder.getHeaderLayoutId());
        }

        // 列表中的ItemView应放在特殊View之后判断
        int layoutId = mSingleItemViewLayoutId;
        if (isMultiItemView()){
            layoutId = mMultiViewBuilder.getLayout(viewType);
        }
        return ViewHolder.bindView(parent,layoutId);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {



        if( position == 0 && hasHeaderView()){
            mHeaderBuilder.bindHeaderView(holder);
            return;
        }

        bindView(holder,mDatas.get(checkPosition(position)));
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


}
