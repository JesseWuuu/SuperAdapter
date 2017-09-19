package com.jessewu.superadapter;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.jessewu.library.SuperAdapter;
import com.jessewu.library.view.ViewHolder;
import com.jessewu.superadapter.data.DataModel;
import com.jessewu.superadapter.data.TestEntity;

public class EmptyViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        final SuperAdapter<TestEntity> adapter = new SuperAdapter<TestEntity>(R.layout.view_list_item_1){

            @Override
            public void bindView(ViewHolder itemView, TestEntity data, int position) {
                itemView.<TextView>getView(R.id.text_1).setText(data.getTitle());
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.setEmptyDataView(R.layout.activity_empty_view);

        Toast.makeText(this, "display data after 2s", Toast.LENGTH_SHORT).show();
       new Handler().postDelayed(new Runnable() {
           @Override
           public void run() {
                adapter.setData(DataModel.getData());
           }
       },2000);

    }

}
