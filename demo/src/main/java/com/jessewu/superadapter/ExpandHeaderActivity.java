package com.jessewu.superadapter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.jessewu.library.SuperAdapter;
import com.jessewu.library.builder.ExpandHeaderBuilder;
import com.jessewu.library.view.ViewHolder;
import com.jessewu.superadapter.data.DataModel;
import com.jessewu.superadapter.data.ExpandDataEntity;
import com.jessewu.superadapter.data.TestEntity;

import java.util.List;

public class ExpandHeaderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        ExpandHeaderBuilder<ExpandDataEntity,TestEntity> builder = new ExpandHeaderBuilder<ExpandDataEntity, TestEntity>() {
            @Override
            public int getHeaderLayoutType(ExpandDataEntity entity, int position, int headerPosition) {
                return 0;
            }

            @Override
            public int getHeaderLayoutId(int type) {
                return R.layout.view_expand_header;
            }

            @Override
            public int getChildrenLayoutType(TestEntity entity, int position, int childrenPosition) {
                return 0;
            }

            @Override
            public int getChildrenLayoutId(int type) {
                return R.layout.view_list_item_1;
            }

            @Override
            public void bindChildrenView(ViewHolder holder, TestEntity entity, int position, int childrenPosition) {
                holder.<TextView>getView(R.id.text_1).setText(entity.getTitle());
            }

            @Override
            public List<TestEntity> getChildrenList(ExpandDataEntity entity) {
                return entity.getChildren();
            }
        };


        SuperAdapter<ExpandDataEntity> adapter = new SuperAdapter<ExpandDataEntity>(builder) {
            @Override
            public void bindView(ViewHolder itemView, ExpandDataEntity data, int position) {
                itemView.<TextView>getView(R.id.expand_text).setText("SubHeader"+data.getTitle());
            }
        };

        recyclerView.setAdapter(adapter);

        adapter.setData(DataModel.getExpandData());

    }
}
