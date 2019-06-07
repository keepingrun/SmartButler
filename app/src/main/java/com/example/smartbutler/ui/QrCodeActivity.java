package com.example.smartbutler.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.example.smartbutler.R;
import com.xys.libzxing.zxing.encoding.EncodingUtils;

/**
 * 生成二维码
 */
public class QrCodeActivity extends BaseActivity{
    private ImageView iv_qr_code;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);
        initView();
    }

    private void initView() {
        iv_qr_code=findViewById(R.id.iv_qr_code);
        //屏幕宽
        int width=getResources().getDisplayMetrics().widthPixels;
        //content是扫码得到的内容
        Bitmap qrCodeBitmap = EncodingUtils.createQRCode("我是智能管家", width/2, width/2,
                        BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        iv_qr_code.setImageBitmap(qrCodeBitmap);
    }
}
