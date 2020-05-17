package com.example.rxjava.observable;

import android.util.Log;
import com.example.rxjava.observer.Observer;
import com.example.rxjava.schedulers.Scheduler;

/**
 * 订阅源-subscribeOn操作符：切换Observable对象执行线程
 * Created by Administrator on 2020/4/12.
 */
public class ObservableSubscribeOn<T> extends Observable<T> {

    private final Observable<T> source;
    private final Scheduler scheduler;

    // 传入的source对象 = 前一个操作符返回的Observable对象（Observable<T> implements ObservableSource<T>）
    // 传入的scheduler对象 = SubscribeOn变换操作符中的scheduler对象
    public ObservableSubscribeOn(Observable<T> source, Scheduler scheduler) {
        this.source = source;
        this.scheduler = scheduler;
    }

    /**
     * 关注：当Observable.subscribe被调用时，subscribeActual(observer)被立刻回调
     * 作用：调用前一个操作符返回的Observable对象的subscribe(observer)
     *
     * @param observer
     */
    @Override
    protected void subscribeActual(Observer<? super T> observer) {
        Log.e(Observable.TAG, "回调ObservableSubscribeOn的subscribeActual()，作用:准备调用前一个操作符返回的Observable对象的subscribe(observer)");

        // 创建一个桥接 下游观察者 和 前一个操作符返回的Observable对象 的 线程切换任务Runnable对象
        scheduler.scheduleDirect(new SubscribeTask(observer));
    }

    /**
     * 线程切换任务
     */
    final class SubscribeTask implements Runnable {
        private final Observer<? super T> observer;

        SubscribeTask(Observer<? super T> observer) {
            this.observer = observer;
        }

        @Override
        public void run() {
            // 让前一个操作符返回的Observable对象的subscribe()在新线程中执行
            Log.d(Observable.TAG, "调用前一个操作符返回的Observable对象的subscribe(observer)，让它的subscribe()在新线程中执行，当前线程是：" + Thread.currentThread().getName());
            source.subscribe(observer);
        }
    }
}
