package com.jessewu.superadapter.data;
/*
 * ===========================
 * DataModel     2017/09/02
 *      
 * Created by JesseWu
 * ===========================
 */

import java.util.ArrayList;
import java.util.List;

public class DataModel {

    public static List<TestEntity> getData(){
        List<TestEntity> data = new ArrayList<>();
        for (int i = 1; i < 15; i++) {
            TestEntity test = new TestEntity();
            test.setTitle(i+"");
            test.setSubTitle("这是第"+i+"个小标题");
            test.setType(i % 2 );
            data.add(test);
        }
        return data;
    }

}
