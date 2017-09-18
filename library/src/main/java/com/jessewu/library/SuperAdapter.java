package com.jessewu.library;
/*
 * ===========================
 * SuperAdapter     2017/08/27
 *
 * Created by JesseWu
 * ===========================
 */

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.jessewu.library.base.BaseSuperAdapter;
import com.jessewu.library.builder.FooterBuilder;
import com.jessewu.library.builder.HeaderBuilder;
import com.jessewu.library.builder.MultiItemViewBuilder;
import com.jessewu.library.status.LoadDataStatus;
import com.jessewu.library.view.ViewHolder;

import java.util.ArrayList;
import java.util.List;

public abstract class SuperAdapter<T> extends BaseSuperAdapter implements View.OnClickListener,View.OnLongClickListener {

    private static final String TAG = "SuperAdapter";

    // 数据源
    private List<T> mDatas = new ArrayList<>();

    // 点击事件监听器
    private OnItemClickListener<T> mOnItemClickListener;

    // 长按事件监听器
    private OnItemLongClickListener<T> mOnItemLongClickListener;

    // 分页加载数据锁，列表滑动到底部时关闭锁防止重复加载数据，请求结束时打开锁
    private boolean mLoadingLock = true;

    // 分页加载数据时的当前页数
    private int mCurrentPage = 0;

    // 分页加载数据的状态
    private int mLoadingStatus = LOADING;

    // 分页加载数据时加载错误提示的信息
    private String mLoadingFailureMsg = "";


    /**
     * 对列表中的itemView进行布局绑定
     * 例：itemView.<TextView>getView(viewId).setText("1111");
     *
     * @param itemView 列表中的itemView
     * @param data 当前itemView对应的数据源
     * @param position 当前itemView在列表中的位置
     */
    public abstract void bindView(ViewHolder itemView, T data ,int position);

    /**
     *  普通单布局列表构造方法
     * @param layoutId 布局id
     */
    public SuperAdapter(int layoutId ){
        this.mSingleItemViewLayoutId = layoutId;
    }

    /**
     * 多类型布局列表构造方法
      * @param multiItemViewBuilder 多类型布局构造器
     */
    public SuperAdapter(MultiItemViewBuilder multiItemViewBuilder){
        this.mMultiItemViewBuilder = multiItemViewBuilder;
    }

    /**
     * 添加列表唯一头部控件
     * @param headerBuilder 头部控件构造器
     */
    public void addHeader(HeaderBuilder headerBuilder){
        this.mHeaderBuilder = headerBuilder;
        this.mSpecialViewBuilder.add(headerBuilder);
    }

    /**
     * 没有数据时显示的提示视图
     */
    public void setEmptyDataView(int layoutId){
        // TODO 空视图和加载更多数据冲突的问题
        mEmptyLayoutId = layoutId;
    }

    /**
     * 是否应该显示空视图
     */
    private boolean shouldShowEmptyView(){
        return hasEmptyView() && mDatas.size() == 0;
    }

    /**
     *  方法中判断item类型的顺序不可调整
     */
    @Override
    public int getItemViewType(int position) {

        //  列表滑动到底部时会多次调用 getItemViewType（int position） 方法
        Log.d(TAG, "getItemViewType: position"+position);

        // 判断唯一头部
        if (position == 0 && hasHeaderView()){
            return TYPE_HEAD_SINGLE;
        }

        // 判断空视图
        if (shouldShowEmptyView()){
            return TYPE_EMPTY;
        }

        // 判断底部
        if(position == mDatas.size() && hasFooterView()){
            return TYPE_FOOT;
        }

        // 重新效验position
        position = checkPosition(position);

        // 多类型item
        if(isMultiItemView()){
            return mMultiItemViewBuilder.getItemType(position,mDatas.get(position));
        }

        return super.getItemViewType(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_HEAD_SINGLE){
            return ViewHolder.bindView(parent,mHeaderBuilder.getHeaderLayoutId());
        }

        if (viewType == TYPE_EMPTY){
            return ViewHolder.bindView(parent,mEmptyLayoutId);
        }

        if (viewType == TYPE_FOOT){
            ViewHolder holder = ViewHolder.bindView(parent,mFooterBuilder.getFooterLayoutId());
            // 设置成不被循环利用，每次都要重新加载
            holder.setIsRecyclable(false);
            return holder;
        }

        // 列表中的ItemView应放在特殊View之后判断
        int layoutId = mSingleItemViewLayoutId;
        if (isMultiItemView()){
            layoutId = mMultiItemViewBuilder.getLayout(viewType);
        }
        ViewHolder viewHolder = ViewHolder.bindView(parent,layoutId);
        viewHolder.itemView.setOnClickListener(this);
        viewHolder.itemView.setOnLongClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if( position == 0 && hasHeaderView()){
            // 绑定 header 视图
            mHeaderBuilder.bindHeaderView(holder);
            return;
        }

        if (shouldShowEmptyView()){
            return;
        }

        position = checkPosition(position);

        Log.d(TAG, "onBindViewHolder: position:"+position+",dataSize:"+mDatas.size());

        if (checkPosition(position) == mDatas.size() && hasFooterView()){
            // 加载数据中
            if (mLoadingStatus == LOADING){
                Log.d(TAG, "onBindViewHolder: LOADING");
                if (!mLoadingLock){
                    Log.d(TAG, "onBindViewHolder: loading lock is close");
                    mFooterBuilder.onLoading(holder);
                    return;
                }else {
                    mLoadingLock = false;
                    mFooterBuilder.onLoading(holder);
                    mFooterBuilder.onLoadingData(mCurrentPage+1,mLoadDataStatus);
                }


            }

            // 加载数据失败
            if (mLoadingStatus == LOADING_FAILURE){
                mFooterBuilder.onLoadingFailure(holder,mLoadingFailureMsg);

                // 将加载状态调整回正在加载中的状态，下次即可重新加载
                mLoadingStatus = LOADING;

                // 点击footer重新加载
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        notifyDataSetChanged();
                    }
                });
            }
            if (mLoadingStatus == LOADING_NO_MORE){
                mFooterBuilder.onNoMoreData(holder);
            }
            return;
        }
        holder.itemView.setTag(position);
        bindView(holder,mDatas.get(position),position);
    }

    @Override
    public int getItemCount() {
        int count = mDatas.size();

        // 如果数据源没有数据且设置过空视图，count加一
        if (shouldShowEmptyView()){
            count++;
        }
        return count + getSpecialBuilderNum();
    }

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener){
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener<T> onItemLongClickListener){
        this.mOnItemLongClickListener = onItemLongClickListener;
    }

    @Override
    public void onClick(View v) {
        int position = (int)v.getTag();
        if (mOnItemClickListener != null){
            mOnItemClickListener.onClick(position,mDatas.get(position));
        }
    }

    @Override
    public boolean onLongClick(View v) {
        int position = (int)v.getTag();
        if (mOnItemLongClickListener != null){
            mOnItemLongClickListener.onLongClick(position,mDatas.get(position));
        }
        return true;
    }

    public interface OnItemClickListener<T>{
        void onClick(int position,T data);
    }

    public interface OnItemLongClickListener<T>{
        void onLongClick(int position,T data);
    }

    public LoadDataStatus<T> setPaginationData(int startPage,FooterBuilder footerBuilder){
        this.mFooterBuilder = footerBuilder;
        this.mSpecialViewBuilder.add(footerBuilder);
        this.mCurrentPage = startPage;
        return mLoadDataStatus;
    }

    private LoadDataStatus<T> mLoadDataStatus = new LoadDataStatus<T>() {
        @Override
        public void onSuccess(List<T> data) {
            addData(data);
            mCurrentPage++;
            mLoadingLock = true;
        }

        @Override
        public void onFailure(String msg) {
            onLoadingStatusChanged(LOADING_FAILURE);
            mLoadingFailureMsg = msg;
        }

        @Override
        public void onNoMoreData() {
            onLoadingStatusChanged(LOADING_NO_MORE);
        }
    };

    private void onLoadingStatusChanged(int status){
        mLoadingStatus = status;
        mLoadingLock = true;
        notifyDataSetChanged();
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
