package com.jessewu.library.widget;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jessewu.library.R;
import com.jessewu.library.builder.FooterBuilder;
import com.jessewu.library.view.ViewHolder;

/*
 * ===========================
 * SimpleFooterBuilder     2017/10/11
 *      
 * Created by Jesse Wu
 * ===========================
 */
public class SimpleFooterBuilder implements FooterBuilder {

    private String normalMsg;
    private String onLoadingMsg;
    private String onLoadingFailureMsg;
    private String onNoMoreDataMsg;


    public SimpleFooterBuilder(String normalMsg,String onLoadingMsg,String onLoadingFailureMsg
            ,String onNoMoreDataMsg){
        this.normalMsg = normalMsg;
        this.onLoadingMsg = onLoadingMsg;
        this.onLoadingFailureMsg = onLoadingFailureMsg;
        this.onNoMoreDataMsg = onNoMoreDataMsg;
    }


    @Override
    public int getFooterLayoutId() {
        return R.layout.view_simple_footer;
    }

    @Override
    public void onNormal(ViewHolder holder) {
        holder.<ProgressBar>getView(R.id.footer_progress).setVisibility(View.GONE);
        holder.<TextView>getView(R.id.footer_msg).setText(this.normalMsg);
    }

    @Override
    public void onLoading(ViewHolder holder) {
        holder.<ProgressBar>getView(R.id.footer_progress).setVisibility(View.VISIBLE);
        holder.<TextView>getView(R.id.footer_msg).setText(this.onLoadingMsg);
    }

    @Override
    public void onLoadingFailure(ViewHolder holder, String msg) {
        holder.<ProgressBar>getView(R.id.footer_progress).setVisibility(View.GONE);
        holder.<TextView>getView(R.id.footer_msg).setText(msg == null || msg.equals("") ? this.onLoadingFailureMsg : msg);
    }

    @Override
    public void onNoMoreData(ViewHolder holder) {
        holder.<ProgressBar>getView(R.id.footer_progress).setVisibility(View.GONE);
        holder.<TextView>getView(R.id.footer_msg).setText(this.onNoMoreDataMsg);
    }
}
