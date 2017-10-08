package com.jessewu.superadapter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jessewu.library.SuperAdapter;
import com.jessewu.library.builder.HeaderBuilder;
import com.jessewu.library.builder.MultiItemViewBuilder;
import com.jessewu.library.view.ViewHolder;
import com.jessewu.superadapter.data.DataModel;
import com.jessewu.superadapter.data.TestEntity;

public class ClickActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        HeaderBuilder builder = new HeaderBuilder() {
            @Override
            public int getHeaderLayoutId() {
                return R.layout.view_header_single;
            }

            @Override
            public void bindHeaderView(ViewHolder viewHolder) {
                viewHolder.<ImageView>getView(R.id.header_img).setImageResource(R.mipmap.ic_launcher);
                viewHolder.<TextView>getView(R.id.header_tv).setText("这是一个头部");
            }
        };

        MultiItemViewBuilder<TestEntity> multiItemViewBuilder = new MultiItemViewBuilder<TestEntity>() {
            @Override
            public int getLayoutId(int type) {
                if (type == 1){
                    return R.layout.view_list_item_1;
                }
                return R.layout.view_list_item_2;
            }

            @Override
            public int getItemType(int position, TestEntity data) {
                return data.getType();
            }
        };

        SuperAdapter<TestEntity> adapter = new SuperAdapter<TestEntity>(multiItemViewBuilder){

            @Override
            public void bindView(ViewHolder itemView, TestEntity data,int position) {
                if (data.getType() == 1){
                    itemView.<TextView>getView(R.id.text_1).setText(data.getTitle());
                }else{
                    itemView.<TextView>getView(R.id.text_2).setText(data.getTitle());
                }
            }
        };

        adapter.addHeader(builder);
        adapter.setData(DataModel.getData());

        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new SuperAdapter.OnItemClickListener<TestEntity>() {
            @Override
            public void onClick(int position, TestEntity data) {
                Toast.makeText(ClickActivity.this, "onClick:"+data.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });

        adapter.setOnItemLongClickListener(new SuperAdapter.OnItemLongClickListener<TestEntity>() {
            @Override
            public void onLongClick(int position, TestEntity data) {
                Toast.makeText(ClickActivity.this, "onLongClick:"+data.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });



    }
}
