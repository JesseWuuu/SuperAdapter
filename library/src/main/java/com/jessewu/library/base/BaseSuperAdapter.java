package com.jessewu.library.base;
/*
 * ===========================
 * BaseSuperAdapter     2017/08/29
 *      
 * Created by JesseWu
 * ===========================
 */

import android.support.v7.widget.RecyclerView;

import com.jessewu.library.options.Builder;
import com.jessewu.library.options.HeaderBuilder;
import com.jessewu.library.options.MultiViewBuilder;
import com.jessewu.library.view.ViewHolder;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseSuperAdapter extends RecyclerView.Adapter<ViewHolder> {

    protected static final int TYPE_HEAD_SINGLE = 1000;

    protected int mSingleItemViewLayoutId;


    protected List<Builder> mSpecialViewBuilder = new ArrayList<>();

    protected MultiViewBuilder mMultiViewBuilder;

    protected HeaderBuilder mHeaderBuilder;


    protected int getSpecialBuilderNum(){
        return mSpecialViewBuilder.size();
    }

    protected boolean isMultiItemView(){
        return mMultiViewBuilder != null;
    }

    protected boolean hasHeaderView(){
        return mHeaderBuilder != null;
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
