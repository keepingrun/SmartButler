package com.example.smartbutler.entity;

import cn.bmob.v3.BmobUser;

//**
// * Created by Administrator on 2019/5/10 0010.
// */
//
public class MyUser extends BmobUser{
    //拓展属性
    private int age;
    private boolean sex;
    private String desc;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}
