package com.example.smartbutler.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.smartbutler.R;
import com.example.smartbutler.utils.ShareUtils;
import com.example.smartbutler.utils.StaticClass;
import com.example.smartbutler.utils.UtilTools;

/**
 * Created by Administrator on 2019/5/4 0004.
 */

public class SplashActivity extends AppCompatActivity {
    /**
     *
     * @param savedInstanceState
     * 1.延时2000ms
     * 2.判断程序是否第一次运行
     * 3.自定义字体
     * 4.Activity全屏主题
     */
    
    private TextView tv__splash;

    //延时或者更新ui，这里用于延时
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case StaticClass.HANDLER_SPLASH:
                    //判断程序是否是第一次运行
                    if(isFirst()){
                        startActivity(new Intent(SplashActivity.this,GuideActivity.class));
                    }else{
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));//MainActivity
                    }
                    finish();//关闭当前activity
                    break;
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
    }

    //初始化化view
    private void initView() {
        //延时2秒发送
        handler.sendEmptyMessageDelayed(StaticClass.HANDLER_SPLASH,2000);
        tv__splash= (TextView) findViewById(R.id.tv_splash);

        //设置字体
        UtilTools.setFont(this,tv__splash);
    }

    private boolean isFirst(){
        boolean isFirst= ShareUtils.getBoolean(this,StaticClass.SHARE_IS_FIRST,true);
        if(isFirst){
            ShareUtils.putBoolean(this,StaticClass.SHARE_IS_FIRST,false);
            return true;
        }else{
            return false;
        }

    }

    //禁止返回键
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
