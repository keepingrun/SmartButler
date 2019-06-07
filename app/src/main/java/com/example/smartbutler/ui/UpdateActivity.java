package com.example.smartbutler.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.example.smartbutler.R;
import com.example.smartbutler.utils.L;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.ProgressListener;
import com.kymjs.rxvolley.http.VolleyError;
import com.kymjs.rxvolley.toolbox.FileUtils;

import java.io.File;

public class UpdateActivity extends BaseActivity{
    //正在下载
    public static final int HANDLER_LODING=10001;
    //下载完成
    public static final int HANDLER_OK=10002;
    //下载失败
    public static final int HANDLER_OFF=10003;
    private TextView tv_size;
    private String url;
    //下载完成的.apk保存到sd的路径和文件名字
    private String path;
    private NumberProgressBar number_progress_bar;
    private  Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                //实时更新进度
                case HANDLER_LODING:
                    Bundle bundle=msg.getData();
                    Long transferredBytes=bundle.getLong("transferredBytes");
                    Long totalSize=bundle.getLong("totalSize");
                    tv_size.setText(transferredBytes+" / "+totalSize);
                    //进度条
                    number_progress_bar.setProgress((int)(((float)transferredBytes/(float)totalSize)*100));
                    break;
                case HANDLER_OK:
                    tv_size.setText("下载成功！");
                    //启动这个应用安装
                    startInstallApk();
                    break;
                case HANDLER_OFF:
                    tv_size.setText("下载失败！");
                    break;
            }
        }
    };
    //启动安装
    private void startInstallApk() {
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setDataAndType(Uri.fromFile(new File(path)),"application/vnd.android.package-archive");
        startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        initView();
    }

    private void initView() {
        tv_size=findViewById(R.id.tv_size);
        //下载 接受从SettingActivity传递过来的url
        url=getIntent().getStringExtra("url");
        path= FileUtils.getSDCardPath()+"/"+System.currentTimeMillis()+".apk";
        if(!TextUtils.isEmpty(url)){
            //下载
            RxVolley.download(path, url, new ProgressListener() {
                @Override
                public void onProgress(long transferredBytes, long totalSize) {
                    //L.i("transferredBytes:"+transferredBytes+"    totalSize:"+totalSize);
                    Message message=new Message();
                    message.what=HANDLER_LODING;
                    Bundle bundle=new Bundle();
                    bundle.putLong("transferredBytes",transferredBytes);
                    bundle.putLong("totalSize",totalSize);
                    message.setData(bundle);
                    handler.sendMessage(message);
                }
            }, new HttpCallback() {
                @Override
                public void onSuccess(String t) {
                    //L.e("成功");
                    handler.sendEmptyMessage(HANDLER_OK);
                }

                @Override
                public void onFailure(VolleyError error) {
                    //L.e("失败");
                    handler.sendEmptyMessage(HANDLER_OFF);
                }
            });
        }
        //进度条
        number_progress_bar=findViewById(R.id.number_progress_bar);
        number_progress_bar.setMax(100);
    }
}
