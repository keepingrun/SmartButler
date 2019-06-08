package com.example.smartbutler.ui;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.smartbutler.R;
import com.example.smartbutler.utils.UtilTools;

import java.util.ArrayList;
import java.util.List;

public class AboutActivity extends BaseActivity{
    private ListView mListView;
    private List<String> mList=new ArrayList<>();
    private ArrayAdapter<String> mAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initView();

    }

    private void initView() {
        mListView=findViewById(R.id.mListView);
        //去除阴影
        getSupportActionBar().setElevation(0);
        mList.add("应用名:"+getString(R.string.app_name));
        mList.add("版本号:"+ UtilTools.getVersion(this));
        mList.add("官网:www.imooc.com");
        mAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,mList);
        mListView.setAdapter(mAdapter);
    }

}
