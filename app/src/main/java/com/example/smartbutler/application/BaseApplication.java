package com.example.smartbutler.application;

import android.app.Application;

import com.example.smartbutler.utils.StaticClass;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.tencent.bugly.crashreport.CrashReport;

import cn.bmob.v3.Bmob;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化bugly
        CrashReport.initCrashReport(getApplicationContext(), StaticClass.BUGLY_APP_ID, true);
        Bmob.initialize(this, StaticClass.BMOB_APP_ID);
        // 将“12345678”替换成您申请的APPID，申请地址：http://www.xfyun.cn
        // 请勿在“=”与appid之间添加任何空字符或者转义符
        SpeechUtility.createUtility(getApplicationContext(), SpeechConstant.APPID +"="+StaticClass.VOICE_KEY);
    }
}
