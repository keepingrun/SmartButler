package com.example.smartbutler.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.smartbutler.R;
import com.example.smartbutler.entity.MyUser;
import com.example.smartbutler.utils.ShareUtils;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.UpdateListener;

public class ForgetPassWordActivity extends BaseActivity implements View.OnClickListener{
    //邮箱修改密码
    private EditText et_email;
    private Button btn_forget_password;
    //旧密码修改密码
    private EditText et_now;
    private EditText et_new;
    private EditText et_new_password;
    private Button btn_update_password;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        initView();
    }

    private void initView() {
        et_email=findViewById(R.id.et_email);
        btn_forget_password=findViewById(R.id.btn_forget_password);
        btn_forget_password.setOnClickListener(this);

        et_now=findViewById(R.id.et_now);
        et_new=findViewById(R.id.et_new);
        et_new_password=findViewById(R.id.et_new_password);
        btn_update_password=findViewById(R.id.btn_update_password);
        btn_update_password.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_forget_password:
                final String email=et_email.getText().toString().trim();
                if(!TextUtils.isEmpty(email)){
                    //final String email = "xxx@163.com";
                    MyUser.resetPasswordByEmail(email, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e==null){
                                Toast.makeText(ForgetPassWordActivity.this,"重置密码请求成功，请到" + email + "邮箱进行密码重置操作",Toast.LENGTH_SHORT).show();
                                finish();
                            }else{
                                Toast.makeText(ForgetPassWordActivity.this, "失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(this,"邮箱输入框不能为空",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_update_password:
                String oldPass=et_now.getText().toString().trim();
                final String newPass=et_new.getText().toString().trim();
                String newPassWord=et_new_password.getText().toString().trim();
                if(!TextUtils.isEmpty(oldPass)&!TextUtils.isEmpty(newPass)&!TextUtils.isEmpty(newPassWord)) {
                    if (newPass.equals(newPassWord)&oldPass.equals(ShareUtils.getString(this,"password",""))) {
                        BmobUser user =  BmobUser.getCurrentUser();
                        user.setPassword(newPass);
                        user.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if(e==null){
                                    Toast.makeText(ForgetPassWordActivity.this, "密码修改成功，可以用新密码进行登录啦", Toast.LENGTH_SHORT).show();
                                    //更新密码
                                    ShareUtils.putString(ForgetPassWordActivity.this,"password",newPass);
                                    finish();
                                }else{
                                    Toast.makeText(ForgetPassWordActivity.this, "失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
//                        MyUser.updateCurrentUserPassword(oldPass, newPass, new UpdateListener() {
//                            @Override
//                            public void done(BmobException e) {
//                                if (e == null) {
//                                    Toast.makeText(ForgetPassWordActivity.this, "密码修改成功，可以用新密码进行登录啦", Toast.LENGTH_SHORT).show();
//                                    finish();
//                                } else {
//                                    Toast.makeText(ForgetPassWordActivity.this, "失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
//                                }
//                            }
//
//                        });
                    } else {
                        Toast.makeText(ForgetPassWordActivity.this, "两次新密码不一致", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(ForgetPassWordActivity.this, "输入框不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
