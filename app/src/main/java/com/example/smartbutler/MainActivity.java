package com.example.smartbutler;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.smartbutler.fragment.*;
import com.example.smartbutler.ui.SettingActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private TabLayout mTableLayout;
    private List<String> mTitle;

    private ViewPager mViewPager;
    private List<Fragment> mFragment;

    //悬浮窗
    private FloatingActionButton fab_setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //去掉阴影
        getSupportActionBar().setElevation(0);

        initView();
        initData();

    }

    private void initView() {
        mTableLayout = (TabLayout) findViewById(R.id.mTabLayout);
        mViewPager = (ViewPager) findViewById(R.id.mViewPager);

        fab_setting= (FloatingActionButton) findViewById(R.id.fab_setting);
        fab_setting.hide();
        fab_setting.setOnClickListener(this);
    }

    private void initData() {
        mTitle = new ArrayList<>();
        mTitle.add(getResources().getString(R.string.title_name1));
        mTitle.add(getResources().getString(R.string.title_name2));
        mTitle.add(getResources().getString(R.string.title_name3));
        mTitle.add(getResources().getString(R.string.title_name4));

        mFragment = new ArrayList<>();
        mFragment.add(new ButlerFragment());
        mFragment.add(new WechatFragment());
        mFragment.add(new GirlFragment());
        mFragment.add(new UserFragment());
        //预加载
        mViewPager.setOffscreenPageLimit(mFragment.size());

        //viewpager滑动监听
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if(i==0){
                    fab_setting.hide();
                }else{
                    fab_setting.show();
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragment.get(position);
            }

            @Override
            public int getCount() {
                return mFragment.size();
            }

            //设置标题

            @Override
            public CharSequence getPageTitle(int position) {
                return mTitle.get(position);
            }
        });

        //TableLayout与Viewpager绑定
        mTableLayout.setupWithViewPager(mViewPager);

    }

    //点击事件
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.fab_setting:
                startActivity(new Intent(this, SettingActivity.class));
                break;
        }
    }
}