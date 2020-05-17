package com.example.rxjava.observable;

import android.util.Log;

import com.example.rxjava.observer.Observer;
import com.example.rxjava.schedulers.Scheduler;

/**
 * 订阅源-observeOn操作符：切换Observer对象执行线程
 * Created by Administrator on 2020/4/12.
 */
public class ObservableObserveOn<T> extends Observable<T> {

    private final Observable<T> source;
    private final Scheduler scheduler;

    // 传入的source对象 = 前一个操作符返回的Observable对象（Observable<T> implements ObservableSource<T>）
    // 传入的scheduler对象 = SubscribeOn变换操作符中的scheduler对象
    public ObservableObserveOn(Observable<T> source, Scheduler scheduler) {
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
        Log.e(Observable.TAG, "回调ObservableObserveOn的subscribeActual()，作用:准备调用前一个操作符返回的Observable对象的subscribe(observer)");

        // 创建一个桥接 下游观察者 和 前一个操作符返回的Observable对象 的 Observer子类-ObserveOnObserver对象
        Scheduler.Worker worker = scheduler.createWorker();
        source.subscribe(new ObserveOnObserver<T>(observer, worker));
    }

    private class ObserveOnObserver<T> implements Observer<T> {
        final Observer<? super T> observer;
        final Scheduler.Worker worker;

        ObserveOnObserver(Observer<? super T> observer, Scheduler.Worker worker) {
            this.observer = observer;
            this.worker = worker;
        }

        @Override
        public void onSubscribe() {

        }

        @Override
        public void onNext(final T var1) {
            // 进行线程切换，再把结果返回给下游观察者
            worker.schedule(new Runnable() {
                @Override
                public void run() {
                    // 让下游观察者的Observer对象在新线程中执行
                    Log.e(Observable.TAG, "回调ObserveOnObserver的onNext()，作用:进行线程切换，准备调用下游观察者的onNext()，当前线程是：" + Thread.currentThread().getName());
                    // 调用下游观察者的onNext()
                    observer.onNext(var1);
                }
            });
        }

        @Override
        public void onError(final Throwable t) {
            worker.schedule(new Runnable() {
                @Override
                public void run() {
                    observer.onError(t);
                }
            });
        }

        @Override
        public void onComplete() {
            worker.schedule(new Runnable() {
                @Override
                public void run() {
                    observer.onComplete();
                }
            });
        }
    }
}
