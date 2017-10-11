package com.jessewu.superadapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void normal(View view){
        Intent i = new Intent();
        i.setClass(this,NormalActivity.class);
        startActivity(i);
    }

    public void multiItemView(View view){
        Intent i = new Intent();
        i.setClass(this,MultiItemViewActivity.class);
        startActivity(i);
    }

    public void singleHeader(View view){
        Intent i = new Intent();
        i.setClass(this,SingleHeaderViewActivity.class);
        startActivity(i);
    }

    public void click(View view){
        Intent i = new Intent();
        i.setClass(this,ClickActivity.class);
        startActivity(i);
    }

    public void more(View view){
        Intent i = new Intent();
        i.setClass(this,MoreDataActivity.class);
        startActivity(i);
    }

    public void empty(View view){
        Intent i = new Intent();
        i.setClass(this,EmptyViewActivity.class);
        startActivity(i);
    }

    public void demo(View view){
        Intent i = new Intent();
        i.setClass(this,DemoActivity.class);
        startActivity(i);
    }


}
