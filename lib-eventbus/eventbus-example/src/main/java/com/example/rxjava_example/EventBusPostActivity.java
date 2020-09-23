package com.example.rxjava_example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.guan.eventbus.EventBus;
import com.guan.eventbus.Subscribe;
import com.guan.eventbus.ThreadMode;

/**
 * Author: chen
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

    /**
     * 发布粘性事件
     *
     * @param view
     */
    public void postSticky(View view) {
        Log.d(EventBus.TAG, "发布 postSticky threadName :" + Thread.currentThread().getName());
        // 粘性事件，在发送事件之后再订阅该事件也能收到。并且，粘性事件会保存在内存中，每次进入都会去内存中查找获取最新的粘性事件，除非你手动解除注册。
        EventBus.getDefault().postSticky(new MessageEvent("发布 postSticky：Hello everyone!", "789"));
    }

    /**
     * 手动获取和删除粘性事件
     *
     * @param view
     */
    public void removeStickyEvent(View view) {
        MessageEvent stickyEvent = EventBus.getDefault().getStickyEvent(MessageEvent.class);
        if(stickyEvent != null) {
            EventBus.getDefault().removeStickyEvent(stickyEvent);
        }
    }

    /**
     * 注册
     *
     * @param view
     */
    public void register(View view) {
        EventBus.getDefault().register(this);
    }

    /**
     * 反注册
     *
     * @param view
     */
    public void unRegister(View view) {
        EventBus.getDefault().unregister(this);
    }

    /**
     * 当MessageEvent被发布时将调用此方法
     *
     * 默认情况下，EventBus捕获从订阅者方法抛出的异常，并发送不强制要求处理的SubscriberExceptionEvent。
     * Caused by: EventBusException: Subscriber class ...EventBusActivity and its super classes have no public methods with the @Subscribe annotation
     *
     * @param event
     */
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEventBus(MessageEvent event) {
        Log.e(EventBus.TAG,"onEventBus + 接收到EventBus事件：" + event.name);
    }

    /**
     * EventBus 是为已经存在的activity传递消息，而且订阅者必须要注册且不能被注销了，
     * 如果你在onStop里面注销了，栈中虽然有这个activity，但是EventBus并没有被注册，所以也接收不到消息，
     */
//    @Override
//    public void onStop() {
//        EventBus.getDefault().unregister(this);
//        super.onStop();
//    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
