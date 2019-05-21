package com.example.smartbutler.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartbutler.MainActivity;
import com.example.smartbutler.R;
import com.example.smartbutler.entity.MyUser;
import com.example.smartbutler.utils.ShareUtils;
import com.example.smartbutler.view.CustomDialog;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Administrator on 2019/5/7 0007.
 * 登陆页面
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    //注册按钮
    private Button btn_registered;
    //登陆按钮
    private Button btnLogin;
    private EditText et_name;
    private EditText et_password;
    private CheckBox keep_password;
    private TextView tv_forget;

    private CustomDialog dialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        //注册
        btn_registered= (Button) findViewById(R.id.btn_registered);
        btn_registered.setOnClickListener(this);
        //登录
        btnLogin=findViewById(R.id.btnLogin);
        et_name=findViewById(R.id.et_name);
        et_password=findViewById(R.id.et_password);
        btnLogin.setOnClickListener(this);
        //记住密码,忘记密码
        keep_password=findViewById(R.id.keep_password);
        tv_forget=findViewById(R.id.tv_forget);
        tv_forget.setOnClickListener(this);
        //设置选中的状态
        boolean isCheck=ShareUtils.getBoolean(this,"keeppass",false);
        //根据存储设置CheckBox
        keep_password.setChecked(isCheck);
        if(isCheck){
         et_name.setText(ShareUtils.getString(this,"name",""));
         et_password.setText(ShareUtils.getString(this,"password",""));
        }

        dialog=new CustomDialog(this,100,100,R.layout.dialog_loading,R.style.Theme_dialog, Gravity.CENTER,R.style.pop_anim_style);
        dialog.setCanceledOnTouchOutside(false);
    }
    @Override
    public void  onClick(View v){
        switch(v.getId()){
            case R.id.btn_registered:
                startActivity(new Intent(LoginActivity.this,RegisteredActivity.class));
                break;
            case R.id.btnLogin:
                //获取输入框的值
                String name=et_name.getText().toString().trim();
                String password=et_password.getText().toString().trim();
                //判断是否为空
                if(!TextUtils.isEmpty(name)&!TextUtils.isEmpty(password)){
                    //登陆
                    dialog.show();
                    final MyUser user=new MyUser();
                    user.setUsername(name);
                    user.setPassword(password);
                    user.login(new SaveListener<MyUser>() {
                        @Override
                        public void done(MyUser myUser, BmobException e) {
                            dialog.dismiss();
                            if(e==null){
                                //判断邮箱是否验证
                                if(user.getEmailVerified()) {
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    //finish();
                                }else{
                                    Toast.makeText(LoginActivity.this, "请前往邮箱验证", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(LoginActivity.this,"登陆失败"+e.toString(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(this, "输入框不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_forget:
                startActivity(new Intent(this,ForgetPassWordActivity.class));
                break;
        }
    }
    //假设输入用户名和密码，但是不点击登陆，而是退出了
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //保存状态
        ShareUtils.putBoolean(this,"keeppass",keep_password.isChecked());
        //是否记住密码
        if(keep_password.isChecked()){
            ShareUtils.putString(this,"name",et_name.getText().toString().trim());
            ShareUtils.putString(this,"password",et_password.getText().toString().trim());
        }else{
            ShareUtils.deleShare(this,"name");
            ShareUtils.deleShare(this,"password");
        }
    }
}
