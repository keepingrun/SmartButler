package com.example.smartbutler.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartbutler.R;
import com.example.smartbutler.service.SmsService;
import com.example.smartbutler.utils.ShareUtils;
import com.example.smartbutler.utils.StaticClass;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.xys.libzxing.zxing.activity.CaptureActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2019/5/4 0004.
 */

public class SettingActivity extends BaseActivity implements View.OnClickListener {
    //语音播报
    private Switch sw_speak;
    //短信提醒
    private Switch sw_sms;
    //检测版本
    private LinearLayout ll_update;
    private TextView tv_version;
    private String versionName;
    private int versionCode;
    //下载地址
    private String url;
    //扫一扫
    private LinearLayout ll_scan;
    //扫面的结果
    private TextView tv_scan_result;
    //二维码分享
    private LinearLayout ll_qr_code;
    //百度地图
    private LinearLayout ll_my_location;
    //关于软件
    private LinearLayout ll_about;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
    }

    private void initView() {
        //语音播报
        sw_speak = findViewById(R.id.sw_speak);
        sw_speak.setOnClickListener(this);
        boolean isSpeak = ShareUtils.getBoolean(this, "isSpeak", false);
        sw_speak.setChecked(isSpeak);
        //短信提醒
        sw_sms = findViewById(R.id.sw_sms);
        sw_sms.setOnClickListener(this);
        boolean isSms = ShareUtils.getBoolean(this, "isSms", false);
        sw_speak.setChecked(isSms);
        //检测版本
        ll_update = findViewById(R.id.ll_update);
        ll_update.setOnClickListener(this);
        tv_version = findViewById(R.id.tv_version);
        try {
            getVersionNameCode();
            tv_version.setText("检测版本:"+versionName);
        } catch (PackageManager.NameNotFoundException e) {
            tv_version.setText("检测版本");
        }
        //扫一扫
        ll_scan=findViewById(R.id.ll_scan);
        ll_scan.setOnClickListener(this);
        tv_scan_result=findViewById(R.id.tv_scan_result);
        ll_qr_code=findViewById(R.id.ll_qr_code);
        ll_qr_code.setOnClickListener(this);
        //关于软件
        ll_about=findViewById(R.id.ll_about);
        ll_about.setOnClickListener(this);
        //百度地图
        ll_my_location=findViewById(R.id.ll_my_location);
        ll_my_location.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sw_speak:
                //切换相反,图形
                sw_speak.setSelected(!sw_speak.isSelected());
                //保存状态
                ShareUtils.putBoolean(this, "isSpeak", sw_speak.isChecked());
                break;
            case R.id.sw_sms:
                //切换相反,图形
                sw_sms.setSelected(!sw_sms.isSelected());
                //保存状态
                ShareUtils.putBoolean(this, "isSms", sw_sms.isChecked());
                //启动短信服务
                if (sw_sms.isChecked()) {
                    startService(new Intent(this, SmsService.class));
                } else {
                    stopService(new Intent(this, SmsService.class));
                }
                break;
            case R.id.ll_update:
                /**
                 * 1.请求服务器的配置文件，拿到 build.gradle:versionCode
                 * 2.比较
                 * 3.dialog提示
                 * 4.跳转到更新界面,并且把url传递过去
                 *
                 */
                RxVolley.get(StaticClass.CHECK_UPDATE_URL, new HttpCallback() {
                    @Override
                    public void onSuccess(String t) {
                        //L.i("json:"+t);
                        parsingJson(t);
                    }
                });
                break;
            case R.id.ll_scan:
                //打开扫描界面扫描条形码或二维码
                Intent openCameraIntent = new Intent(SettingActivity.this, CaptureActivity.class);
                startActivityForResult(openCameraIntent, 0);
                break;
            case R.id.ll_qr_code:
                startActivity(new Intent(this,QrCodeActivity.class));
                break;
            case R.id.ll_about:
                startActivity(new Intent(this,AboutActivity.class));
                break;
            case R.id.ll_my_location:
                startActivity(new Intent(this,LocationActivity.class));
                break;
        }
    }

    private void parsingJson(String t) {
        try {
            JSONObject jsonObject=new JSONObject(t);
            int code=jsonObject.getInt("versionCode");
            url=jsonObject.getString("url");
            //大于当前版本，更新
            if(code>versionCode){
                showUpdateDialog(jsonObject.getString("content"));
            }else{
                Toast.makeText(this,"当前是最新版本!",Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //弹出升级提示
    private void showUpdateDialog(String content) {
        new AlertDialog.Builder(this)
                .setTitle("有新版本啦")
                //content
                .setMessage("修复多项bug")
                .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent=new Intent(SettingActivity.this, UpdateActivity.class);
                        intent.putExtra("url",url);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    //什么都不用做,也会执行dismiss方法
                    }
                })
                .show();

    }

    //获取版本号/Code
    private void getVersionNameCode() throws PackageManager.NameNotFoundException {
        PackageManager pm = getPackageManager();
        PackageInfo info=pm.getPackageInfo(getPackageName(),0);
        versionName=info.versionName;
        versionCode=info.versionCode;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            tv_scan_result.setText(scanResult);
        }
    }
}