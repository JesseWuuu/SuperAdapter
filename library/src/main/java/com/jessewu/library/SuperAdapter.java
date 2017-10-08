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
     *  点击事件监听器
     */
    public interface OnItemClickListener<T>{
        void onClick(int position,T data);
    }

    /**
     *  长按事件监听器
     */
    public interface OnItemLongClickListener<T>{
        void onLongClick(int position,T data);
    }

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
        this.mSpecialViewBuilder.add("header");
    }

    /**
     * 没有数据时显示的提示视图
     *
     * 注意: 当设置了分页加载数据的footer后空视图将无效
     */
    public void setEmptyDataView(int layoutId){
        mEmptyLayoutId = layoutId;
    }

    /**
     * 注册点击事件监听器
     */
    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener){
        this.mOnItemClickListener = onItemClickListener;
    }

    /**
     * 注册长按事件监听器
     */
    public void setOnItemLongClickListener(OnItemLongClickListener<T> onItemLongClickListener){
        this.mOnItemLongClickListener = onItemLongClickListener;
    }

    /**
     * 设置列表以分页的方式动态加载数据源
     *
     * @param startPage 加载数据的起始页，如 startPage = 0 列表之后加载数据的页数依次为1、2、3...
     * @param footerBuilder 列表底部布局footer构造器
     * @return 分页加载数据 - 状态控制器
     */
    public LoadDataStatus<T> setPaginationData(int startPage,FooterBuilder footerBuilder){
        this.mFooterBuilder = footerBuilder;
        this.mSpecialViewBuilder.add("footer");
        this.mCurrentPage = startPage;
        return mLoadDataStatus;
    }

    /**
     *  设置数据源
     */
    public void setData(List<T> datas){
        this.mDatas = datas;
        notifyDataSetChanged();
    }

    /**
     * 添加数据源
     */
    public void addData(List<T> datas){
        this.mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    /**
     * 添加数据源
     */
    public void addData(T data){
        this.mDatas.add(data);
        notifyDataSetChanged();
    }

    /**
     * 移除数据源
     */
    public void removeData(T data){
        this.mDatas.remove(data);
        notifyDataSetChanged();
    }

    /**
     * 移除数据源
     */
    public void removeData(int position){
        this.mDatas.remove(checkPosition(position));
        notifyDataSetChanged();
    }

    /**
     * 清空数据源
     */
    public void clearData(){
        this.mDatas.clear();
        notifyDataSetChanged();
    }

    /**
     *  方法中判断item类型的顺序不可调整
     *
     *  列表滑动到底部时会多次调用 getItemViewType（int position） 方法
     */
    @Override
    public int getItemViewType(int position) {

        // 判断唯一头部
        if (position == 0 && hasHeaderView()){
            return TYPE_HEAD_SINGLE;
        }

        // 判断空视图
        if (shouldShowEmptyView()){
            return TYPE_EMPTY;
        }

        // 重新效验position
        position = checkPosition(position);

        // 判断底部
        if(position == mDatas.size() && hasFooterView()){
            return TYPE_FOOT;
        }
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

        Log.d(TAG, "onBindViewHolder----------------");

        if( position == 0 && hasHeaderView()){
            // 绑定 header 视图
            mHeaderBuilder.bindHeaderView(holder);
            Log.d(TAG, " header");
            return;
        }

        if (shouldShowEmptyView()){
            Log.d(TAG, "emptyView");
            return;
        }

        position = checkPosition(position);

        if (position == mDatas.size() && hasFooterView()){
            Log.d(TAG, "footer");
            // 加载数据中
            if (mLoadingStatus == LOADING){
                if (!mLoadingLock){
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



    /**
     * 分页获取数据状态控制器接口实现类
     */
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

    /**
     * 当分页获取数据状态发生改变
     */
    private void onLoadingStatusChanged(int status){
        mLoadingStatus = status;
        mLoadingLock = true;
        notifyDataSetChanged();
    }

    /**
     * 是否应该显示空视图
     */
    private boolean shouldShowEmptyView(){
        return hasEmptyView() && mDatas.size() == 0 && !hasFooterView();
    }


}
