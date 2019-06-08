package com.example.smartbutler.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Created by Administrator on 2019/4/29 0029.
 *
 * 工具统一类
 */

public class UtilTools {
    public static void setFont(Context mContext,TextView textView){
        Typeface fontType=Typeface.createFromAsset(mContext.getAssets(),"fonts/FONT.TTF");
        textView.setTypeface(fontType);
    }
    //保存图片到shareUtils
    public static void putImageToShare(Context mContext,ImageView imageView){
        //保存
        BitmapDrawable drawable= (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap=drawable.getBitmap();
        //第一步：将bitmap压缩成字节数组输出流
        ByteArrayOutputStream byStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,80,byStream);//转成了字节流了
        //第二步：利用base64将我们的字节数组输出流转化成string
        byte[] byteArray=byStream.toByteArray();
        String imgString=new String(Base64.encode(byteArray,Base64.DEFAULT));
        ShareUtils.putString(mContext,"image_title",imgString);
    }
    //读取图片
    public static void getImageToShare(Context mContext,ImageView imageView) {
        //拿到string
        String imgString=ShareUtils.getString(mContext,"image_title","");
        if(!imgString.equals("")){
            //利用base64将我们的string转化成字节数组
            byte[] byteArray= Base64.decode(imgString,Base64.DEFAULT);
            ByteArrayInputStream byStream=new ByteArrayInputStream(byteArray);
            //生成bitmap
            Bitmap bitmap=BitmapFactory.decodeStream(byStream);
            imageView.setImageBitmap(bitmap);
        }
    }

    //获取版本号
    public static String getVersion(Context mContext){
        try {
            PackageInfo info=mContext.getPackageManager().getPackageInfo(mContext.getPackageName(),0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "未知";
        }
    }

}
