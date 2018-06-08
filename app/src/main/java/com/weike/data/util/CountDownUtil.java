package com.weike.data.util;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import java.lang.ref.WeakReference;

public class CountDownUtil {

    private int ALL_TIME = 60 * 1000;

    private MyCountDownTimer timer;

    private int TIME_INTERVAL = 1000;


    public CountDownUtil() {
        timer = new MyCountDownTimer(ALL_TIME, TIME_INTERVAL);
    }

    public CountDownUtil reset() {
        timer.cancel();
        timer.start();
        return this;
    }

    public CountDownUtil setOnTakeListener() {
        return this;
    }

    public void start() {
        timer.start();
    }

    public void cancel() {
        timer.cancel();
    }

    private class MyCountDownTimer extends CountDownTimer {


        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            listener.onTick(millisUntilFinished);
        }

        @Override
        public void onFinish() {
            listener.onFinish();
        }
    }

    private OnTimeTickListener listener;

    public void setListener(OnTimeTickListener listener){
        this.listener = listener;
    }

    public interface OnTimeTickListener{
       void onTick(long min);

       void onFinish();
    }
}
