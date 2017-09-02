package com.jessewu.library.base;
/*
 * ===========================
 * BaseSuperAdapter     2017/08/29
 *      
 * Created by JesseWu
 * ===========================
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import com.jessewu.library.options.Builder;
import com.jessewu.library.options.MultiViewBuilder;
import com.jessewu.library.view.ViewHolder;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseSuperAdapter extends RecyclerView.Adapter<ViewHolder> {

    protected int mSingleItemViewLayoutId;

    protected List<Builder> mViewBuilder = new ArrayList<>();

    protected MultiViewBuilder mMultiViewBuilder;

    protected LayoutInflater inflater;

    protected int getBuilderNum(){
        return mViewBuilder.size();
    }

    protected boolean isMultiItemView(){
        return mMultiViewBuilder != null;
    }



}
