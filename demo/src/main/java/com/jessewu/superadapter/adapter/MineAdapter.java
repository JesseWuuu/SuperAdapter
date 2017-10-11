package com.jessewu.superadapter.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import com.jessewu.library.SuperAdapter;
import com.jessewu.library.builder.HeaderBuilder;
import com.jessewu.library.builder.MultiItemViewBuilder;
import com.jessewu.library.view.ViewHolder;
import com.jessewu.library.widget.SimpleFooterBuilder;
import com.jessewu.superadapter.R;
import com.jessewu.superadapter.data.TestEntity;

/*
 * ===========================
 * MineAdapter     2017/10/11
 *      
 * Created by Jesse Wu
 * ===========================
 */
public class MineAdapter extends SuperAdapter<TestEntity> {

    private static MultiItemViewBuilder<TestEntity> multiItemViewBuilder = new MultiItemViewBuilder<TestEntity>() {

        @Override
        public int getLayoutId(int type) {
            if (type == 0){
                return R.layout.view_list_item_1;
            }else {
                return R.layout.view_list_item_2;
            }
        }

        @Override
        public int getItemType(int position, TestEntity data) {
            return data.getType();
        }
    };

    public MineAdapter() {
        super(multiItemViewBuilder);
        addHeader();
        addFooter();
    }

    @Override
    public void bindView(ViewHolder itemView, TestEntity data, int position) {
        if(data.getType() == 0){
            itemView.<TextView>getView(R.id.text_1).setText(data.getTitle());
        }else{
            itemView.<TextView>getView(R.id.text_2).setText(data.getTitle());
        }
    }

    private void addHeader(){
        this.addHeader(new HeaderBuilder() {
            @Override
            public int getHeaderLayoutId() {
                return R.layout.view_header_single;
            }

            @Override
            public void bindHeaderView(ViewHolder viewHolder) {
                viewHolder.<ImageView>getView(R.id.header_img).setImageResource(R.mipmap.ic_launcher);
                viewHolder.<TextView>getView(R.id.header_tv).setText("这是一个头部");
            }
        });
    }

    public void addFooter(){
        this.addFooter(new SimpleFooterBuilder("这是个底部","正在加载数据中","加载数据失败","已经到底啦"));
    }

}
