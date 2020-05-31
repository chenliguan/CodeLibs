package com.example.rxjava_example;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.guan.eventbus.EventBus;
import com.guan.eventbus.MessageEvent;
import com.guan.eventbus.Subscribe;
import com.guan.eventbus.ThreadMode;

/**
 * Author: 陈李冠
 * Version: 1.0.0
 * Date: 2020/5/31
 * Mender:
 * Modify:
 * Description: EventBus回调页面
 */
public class EventBusMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_eventbus);

        initEvent();
    }

    private void initEvent() {
        Intent intent = new Intent();
        intent.setClass(this, EventBusPostActivity.class);
        startActivity(intent);
    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent event) {
        Log.d(EventBus.TAG, "onEvent: " + event.toString());
        Log.d(EventBus.TAG, "onEvent threadName :" + Thread.currentThread().getName());
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onMessageEvent(MessageEvent event) {
        Log.d(EventBus.TAG, "getMessage: " + event.toString());
        Log.d(EventBus.TAG, "getMessage threadName :" + Thread.currentThread().getName());
    }
}
