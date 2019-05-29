package com.example.smartbutler.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Switch;

import com.example.smartbutler.R;
import com.example.smartbutler.utils.ShareUtils;

/**
 * Created by Administrator on 2019/5/4 0004.
 */

public class SettingActivity extends BaseActivity implements View.OnClickListener {
    private Switch sw_speak;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
    }

    private void initView() {
        sw_speak=findViewById(R.id.sw_speak);
        sw_speak.setOnClickListener(this);
        boolean isSpeak=ShareUtils.getBoolean(this,"isSpeak",false);
        sw_speak.setChecked(isSpeak);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.sw_speak:
                //切换相反,图形
                sw_speak.setSelected(!sw_speak.isSelected());
                //保存状态
                sw_speak.setChecked(sw_speak.isChecked());
                ShareUtils.putBoolean(this,"isSpeak",sw_speak.isChecked());
                break;
        }
    }
}
