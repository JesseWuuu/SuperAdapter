package com.jessewu.superadapter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.jessewu.library.SuperAdapter;
import com.jessewu.library.builder.MultiItemViewBuilder;
import com.jessewu.library.view.ViewHolder;
import com.jessewu.superadapter.data.DataModel;
import com.jessewu.superadapter.data.TestEntity;

public class MultiItemViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        MultiItemViewBuilder<TestEntity> builder = new MultiItemViewBuilder<TestEntity>() {
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

        SuperAdapter<TestEntity> adapter = new SuperAdapter<TestEntity>(builder){

            @Override
            public void bindView(ViewHolder itemView, TestEntity data,int position) {
                if(data.getType() == 0){
                    itemView.<TextView>getView(R.id.text_1).setText(data.getTitle());
                }else{
                    itemView.<TextView>getView(R.id.text_2).setText(data.getTitle());
                }
            }
        };
        recyclerView.setAdapter(adapter);

        adapter.setData(DataModel.getData());

    }
}
