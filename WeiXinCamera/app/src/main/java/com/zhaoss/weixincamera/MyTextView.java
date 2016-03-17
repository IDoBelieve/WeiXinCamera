package com.zhaoss.weixincamera;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * Created by Zhaoss on 2016/3/15.
 */
public class MyTextView extends TextView {

    private OnTouchListener listener;
    /** 手指是否触摸在屏幕上 */
    private boolean isTouch;
    private MyHandler myHandler;

    public MyTextView(Context context) {
        super(context);
        init();
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        myHandler = new MyHandler(this);
    }

    static class MyHandler extends Handler{

        MyTextView tv;
        MyHandler(MyTextView tv){
            this.tv = tv;
        }
        @Override
        public void handleMessage(Message msg) {
            //判断是否为长按事件
            if(tv.isTouch){
                if(tv.listener != null) tv.listener.onLongListener();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                isTouch = true;
                myHandler.sendEmptyMessageDelayed(0, 500);
                if(listener != null) listener.onDownListener();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                isTouch = false;
                if(listener != null) listener.onUpListener();
                break;
        }
        return true;
    }

    public interface OnTouchListener{
        void onDownListener();
        void onLongListener();
        void onUpListener();
    }

    public void setOnTouchListener(OnTouchListener listener){
        this.listener = listener;
    }
}