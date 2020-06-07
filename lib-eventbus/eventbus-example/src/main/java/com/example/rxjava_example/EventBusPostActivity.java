package com.example.rxjava_example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.guan.eventbus.EventBus;
import com.guan.eventbus.MessageEvent;

/**
 * Author: 陈李冠
 * Version: 1.0.0
 * Date: 2020/5/31
 * Mender:
 * Modify:
 * Description: EventBus发布事件页面
 */
public class EventBusPostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventbus_post);
    }

    /**
     * 在子线程中发布事件
     */
    public void postInThread(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 在子线程中发布事件
                Log.d(EventBus.TAG, "子线程中发布 threadName :" + Thread.currentThread().getName());
                EventBus.getDefault().post(new MessageEvent("小小", "123"));
            }
        }).start();
    }

    /**
     * 在主线程中发布事件
     */
    public void postInMain(View view) {
        Log.d(EventBus.TAG, "主线程中发布 threadName :" + Thread.currentThread().getName());
        EventBus.getDefault().post(new MessageEvent("大大","456"));
    }
}
