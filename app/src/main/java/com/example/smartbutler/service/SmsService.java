package com.example.smartbutler.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.smartbutler.R;
import com.example.smartbutler.utils.L;
import com.example.smartbutler.utils.StaticClass;
import com.example.smartbutler.view.DispatchLinearLayout;

/**
 * 1.注册服务
 * windowManager获取屏幕宽高
 */
public class SmsService extends Service implements View.OnClickListener{
    //信息接收广播
    private SmsReceiver smsReceiver;
    //home键监听广播
    private HomeWatchReceiver homeWatchReceiver;
    //发件人号码
    private String smsPhone;
    //内容
    private String smsContent;
    //窗口管理
    private WindowManager wm;
    //布局参数
    private WindowManager.LayoutParams layoutParams;
    //窗口布局
    private DispatchLinearLayout mView;
    private TextView tv_phone;
    private TextView tv_content;
    private Button btn_send_sms;

    public static final String SYSTEM_DIALOGS_RESON_KEY="reason";
    public static final String SYSTEM_DIALOGS_HOME_KEY="homekey";
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }
    //初始化
    private void init() {
        L.i("init service");
        //动态注册广播
        smsReceiver=new SmsReceiver();
        IntentFilter intent=new IntentFilter();
        intent.addAction(StaticClass.SMS_ACTION);
        //设置权限
        intent.setPriority(Integer.MAX_VALUE);
        //注册
        registerReceiver(smsReceiver,intent);
        //home键注册
        homeWatchReceiver=new HomeWatchReceiver();
        //home键
        IntentFilter intentFilter=new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        registerReceiver(homeWatchReceiver,intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        L.i("stop service");
        unregisterReceiver(smsReceiver);
        unregisterReceiver(homeWatchReceiver);
    }


    class SmsReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            if(StaticClass.SMS_ACTION.equals(action)){
                L.i("收到短信");
                //获取短信内容，返回的是一个object数组
                Object[] objs= (Object[]) intent.getExtras().get("pdus");
                for(Object obj:objs){
                    //数组元素转换成短信对象
                    SmsMessage sms=SmsMessage.createFromPdu((byte[])obj);
                    //发件人
                    smsPhone=sms.getOriginatingAddress();
                    //内容
                    smsContent=sms.getMessageBody();
                    L.i("联系人"+smsPhone+":"+smsContent);
                    showWindow();
                }
            }
        }
    }
    //窗口提示接收的短信
    private void showWindow() {
        //获取系统服务
        wm= (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        //获取布局参数
        layoutParams=new WindowManager.LayoutParams();
        //定义宽高
        layoutParams.width=WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height=WindowManager.LayoutParams.MATCH_PARENT;
        //定义标记  FLAG_KEEP_SCREEN_ON当窗口可视，保持屏幕亮起
        layoutParams.flags=WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                |WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        //设置格式
        layoutParams.format= PixelFormat.TRANSPARENT;
        //类型
        layoutParams.type=WindowManager.LayoutParams.TYPE_PHONE;
        //加载布局
        mView= (DispatchLinearLayout) View.inflate(getApplicationContext(), R.layout.sms_item,null);
        tv_phone=mView.findViewById(R.id.tv_phone);
        tv_content=mView.findViewById(R.id.tv_content);
        btn_send_sms=mView.findViewById(R.id.btn_send_sms);
        btn_send_sms.setOnClickListener(this);

        tv_phone.setText("收件人:"+smsPhone);
        tv_content.setText("短信内容"+smsContent);
        //添加view到窗口
        wm.addView(mView,layoutParams);
        mView.setDispatchKeyEventListener(mDispatchKeyEventListener);
    }
    private DispatchLinearLayout.DispatchKeyEventListener mDispatchKeyEventListener=new DispatchLinearLayout.DispatchKeyEventListener() {
        @Override
        public boolean dispatchKeyEvent(KeyEvent event) {
            //判断是否按返回键
            if(event.getKeyCode()==KeyEvent.KEYCODE_BACK){
                if(mView.getParent()!=null){
                    wm.removeView(mView);
                }
                return true;
            }
            return false;
        }
    };
    //点击事件
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_send_sms:
                sendSms();
                //消失窗口
                if(mView.getParent()!=null){
                    wm.removeView(mView);
                }
                break;
        }
    }
    //回复短信
    private void sendSms() {
        Uri uri=Uri.parse("smsto:"+smsPhone);
        Intent intent=new Intent(Intent.ACTION_SENDTO,uri);
        //设置模式
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("sms_body","");
        startActivity(intent);
    }


    //监听home键的广播
    class HomeWatchReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            //关闭所有dialogs
            if(action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)){
                //home键
                String reason=intent.getStringExtra(SYSTEM_DIALOGS_RESON_KEY);
                if(SYSTEM_DIALOGS_HOME_KEY.equals(reason)){
                    L.i("我点击了home键");
                    if(mView.getParent()!=null){
                        wm.removeView(mView);
                    }
                }
            }
        }
    }
}
