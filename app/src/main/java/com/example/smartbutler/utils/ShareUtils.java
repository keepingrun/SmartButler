package com.example.smartbutler.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2019/5/4 0004.
 * SharedPreferences的封装
 */

public class ShareUtils {
    public static final String NAME="config";
    //写
    public static void putString(Context mContext,String key,String value){
        SharedPreferences sp=mContext.getSharedPreferences(NAME,0);
        sp.edit().putString(key,value).apply();
    }
    //读
    public static String getString(Context mContext,String key,String defValue){
        SharedPreferences sp=mContext.getSharedPreferences(NAME,0);
        return sp.getString(key,defValue);
    }

    //写
    public static void putInt(Context mContext,String key,int value){
        SharedPreferences sp=mContext.getSharedPreferences(NAME,0);
        sp.edit().putInt(key,value).apply();
    }
    //读
    public static int getInt(Context mContext,String key,int defValue){
        SharedPreferences sp=mContext.getSharedPreferences(NAME,0);
        return sp.getInt(key,defValue);
    }

    //写
    public static void putBoolean(Context mContext,String key,boolean value){
        SharedPreferences sp=mContext.getSharedPreferences(NAME,0);
        sp.edit().putBoolean(key,value).apply();
    }
    //读
    public static boolean getBoolean(Context mContext,String key,boolean defValue){
        SharedPreferences sp=mContext.getSharedPreferences(NAME,0);
        return sp.getBoolean(key,defValue);
    }

    //删除 单个
    public static void deleShare(Context mContext,String key){
        SharedPreferences sp=mContext.getSharedPreferences(NAME,0);
        sp.edit().remove(key).apply();
    }
    //删除 全部
    public static void deleAll(Context mContext){
        SharedPreferences sp=mContext.getSharedPreferences(NAME,mContext.MODE_PRIVATE);//0或者MODE_PRIVATE
        sp.edit().clear().apply();
    }


//    private void test(Context mContext){
//        SharedPreferences sp=mContext.getSharedPreferences("config",0);//MODE_PRIVATE或0表示只有当应用程序才可以文件读写
//        sp.getString("key","为获取到");
//        SharedPreferences.Editor editor=sp.edit();
//        editor.putString("key","value");
    //     editor.apply();
//    }

}
