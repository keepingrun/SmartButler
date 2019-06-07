package com.example.smartbutler.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.LinearLayout;

/**
 * 事件分发
 */
public class DispatchLinearLayout extends LinearLayout {
    private DispatchKeyEventListener dispatchKeyEventListener;

    public DispatchKeyEventListener getDispatchKeyEventListener() {
        return dispatchKeyEventListener;
    }

    public void setDispatchKeyEventListener(DispatchKeyEventListener dispatchKeyEventListener) {
        this.dispatchKeyEventListener = dispatchKeyEventListener;
    }
    public DispatchLinearLayout(Context context) {
        super(context);
    }

    public DispatchLinearLayout(Context context,AttributeSet attrs) {
        super(context, attrs);
    }

    public DispatchLinearLayout(Context context,AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    //接口
    public  interface DispatchKeyEventListener{
        boolean dispatchKeyEvent(KeyEvent event);
    }
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if(dispatchKeyEventListener!=null){
            //如果不为空，说明调用了，去获取事件
            return dispatchKeyEventListener.dispatchKeyEvent(event);
        }
        return super.dispatchKeyEvent(event);
    }

}
