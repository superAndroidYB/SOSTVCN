package com.sostvcn.widget;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Gallery;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/4/22.
 */
public class HomeSlideGallery extends Gallery {

    private static final int timerAnimation = 1;
    private final Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case timerAnimation:
                    int position = getSelectedItemPosition();
                    if (position >= (getCount() - 1)) {
                        onKeyDown(KeyEvent.KEYCODE_DPAD_LEFT, null);
                    } else {
                        onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, null);
                    }
                    break;

                default:
                    break;
            }
        };
    };

    private Timer timer = null;
    private TimerTask task = null;

    public HomeSlideGallery(Context paramContext) {
        super(paramContext);
    }

    public HomeSlideGallery(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);

    }

    public HomeSlideGallery(Context paramContext, AttributeSet paramAttributeSet,
                     int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);

    }

    private boolean isScrollingLeft(MotionEvent paramMotionEvent1,
                                    MotionEvent paramMotionEvent2) {
        float f2 = paramMotionEvent2.getX();
        float f1 = paramMotionEvent1.getX();
        if (f2 > f1)
            return true;
        return false;
    }

    public boolean onFling(MotionEvent paramMotionEvent1,
                           MotionEvent paramMotionEvent2, float paramFloat1, float paramFloat2) {
        int keyCode;
        if (isScrollingLeft(paramMotionEvent1, paramMotionEvent2)) {
            keyCode = KeyEvent.KEYCODE_DPAD_LEFT;
        } else {
            keyCode = KeyEvent.KEYCODE_DPAD_RIGHT;
        }
        onKeyDown(keyCode, null);
        return true;
    }

    public void destroy() {
        if(task != null){
            task.cancel();
            task = null;
        }
        if(timer != null){
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }

    public void startTimer(){
        if(timer == null){
            timer = new Timer();
        }
        if(task == null){
            task = new TimerTask() {
                public void run() {
                    mHandler.sendEmptyMessage(timerAnimation);
                }
            };
        }
        if(timer != null && task != null){
            timer.schedule(task, 3000, 3000);
        }
    }
}
