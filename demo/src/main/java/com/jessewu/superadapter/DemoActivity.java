package com.jessewu.superadapter;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.jessewu.library.SuperAdapter;
import com.jessewu.library.paging.LoadDataListener;
import com.jessewu.library.paging.LoadDataStatus;
import com.jessewu.superadapter.adapter.MineAdapter;
import com.jessewu.superadapter.data.DataModel;
import com.jessewu.superadapter.data.TestEntity;

public class DemoActivity extends AppCompatActivity {

    MineAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        adapter = new MineAdapter();
        recyclerView.setAdapter(adapter);

        // 设置空数据视图
        adapter.setEmptyDataView(R.layout.activity_empty_view);

        // 点击事件
        adapter.setOnItemClickListener(new SuperAdapter.OnItemClickListener<TestEntity>() {
            @Override
            public void onClick(int position, TestEntity data) {
                Toast.makeText(DemoActivity.this, "onClick:"+data.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });

        // 长按事件
        adapter.setOnItemLongClickListener(new SuperAdapter.OnItemLongClickListener<TestEntity>() {
            @Override
            public void onLongClick(int position, TestEntity data) {
                Toast.makeText(DemoActivity.this, "onLongClick:"+data.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btn).setVisibility(View.VISIBLE);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setData();
            }
        });
    }

    // 分页加载数据
    private void setData(){
        adapter.clearData();
        adapter.setPaginationData(-1, new LoadDataListener() {
            @Override
            public void onLoadingData(final int loadPage, final LoadDataStatus loadDataStatus) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadDataStatus.onSuccess(DataModel.getMoreData(loadPage));
                    }
                },2000);
            }
        });
    }

}
