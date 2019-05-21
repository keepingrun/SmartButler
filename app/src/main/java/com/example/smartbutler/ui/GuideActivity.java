package com.example.smartbutler.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.smartbutler.MainActivity;
import com.example.smartbutler.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019/5/5 0005.
 * 引导页
 */

public class GuideActivity extends AppCompatActivity{
    //ViewPager是个容器
    private ViewPager mViewPager_guide;
    //容器
    private List<View> mList=new ArrayList<>();
    private View view1,view2,view3;

    private ImageView point1,point2,point3;

    //跳过
    private ImageView iv_back;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        initView();
    }

    private void initView() {
        iv_back= (ImageView) findViewById(R.id.iv_back);
        mViewPager_guide= (ViewPager) findViewById(R.id.mViewPager_guide);
        point1= (ImageView) findViewById(R.id.point1);
        point2= (ImageView) findViewById(R.id.point2);
        point3= (ImageView) findViewById(R.id.point3);
        setPointImg(true,false,false);

        view1=View.inflate(this,R.layout.pager_item_one,null);
        view2=View.inflate(this,R.layout.pager_item_two,null);
        view3=View.inflate(this,R.layout.pager_item_three,null);

        mList.add(view1);
        mList.add(view2);
        mList.add(view3);

        //设置适配器
        mViewPager_guide.setAdapter(new GuideAdapter());

        mViewPager_guide.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch(position){
                    case 0:
                        setPointImg(true,false,false);
                        iv_back.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        setPointImg(false,true,false);
                        iv_back.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        setPointImg(false,false,true);
                        iv_back.setVisibility(View.GONE);
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setPointImg(boolean p1, boolean p2, boolean p3) {
        if(p1){
            point1.setBackgroundResource(R.drawable.point_on);
        }else{
            point1.setBackgroundResource(R.drawable.point_off);
        }
        if(p2){
            point2.setBackgroundResource(R.drawable.point_on);
        }else{
            point2.setBackgroundResource(R.drawable.point_off);
        }
        if(p3){
            point3.setBackgroundResource(R.drawable.point_on);
        }else{
            point3.setBackgroundResource(R.drawable.point_off);
        }
    }

    class GuideAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ((ViewPager)container).addView(mList.get(position));
            return mList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager)container).removeView(mList.get(position));
        }
    }

    public void onClickToMainActivity(View v){
        startActivity(new Intent(this,LoginActivity.class));
        //关闭引导页
        finish();
    }
}
