package com.jessewu.library.base;
/*
 * ===========================
 * BaseSuperAdapter     2017/08/29
 *      
 * Created by JesseWu
 * ===========================
 */

import android.support.v7.widget.RecyclerView;

import com.jessewu.library.builder.Builder;
import com.jessewu.library.builder.FooterBuilder;
import com.jessewu.library.builder.HeaderBuilder;
import com.jessewu.library.builder.MultiViewBuilder;
import com.jessewu.library.view.ViewHolder;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseSuperAdapter extends RecyclerView.Adapter<ViewHolder> {


    protected static final int TYPE_HEAD_SINGLE = 1000;
    protected static final int TYPE_FOOT = 1001;
    protected static final int TYPE_IGNORE = 1002;

    protected static final int LOADING = 2000;
    protected static final int LOADING_FAILURE = 2001;
    protected static final int LOADING_NO_MORE = 2002;

    protected int mSingleItemViewLayoutId;


    protected List<Builder> mSpecialViewBuilder = new ArrayList<>();

    protected MultiViewBuilder mMultiViewBuilder;

    protected HeaderBuilder mHeaderBuilder;

    protected FooterBuilder mFooterBuilder;


    /**
     * 获取里列表中特殊控件的数量
     */
    protected int getSpecialBuilderNum(){
        return mSpecialViewBuilder.size();
    }

    protected boolean isMultiItemView(){
        return mMultiViewBuilder != null;
    }

    protected boolean hasHeaderView(){
        return mHeaderBuilder != null;
    }

    protected boolean hasFooterView(){
        return mFooterBuilder != null;
    }

    /**
     * 重新效验position
     *
     * 当添加了特殊View后会对列表中itemView的position造成错乱，需要重新效验position
     */
    protected int checkPosition(int position){

        int newPosition = position;

        if (hasHeaderView() && position != 0){
           newPosition = position - 1;
        }
        return newPosition;
    }


}
