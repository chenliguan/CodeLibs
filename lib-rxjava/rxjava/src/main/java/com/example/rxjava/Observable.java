package com.example.rxjava;

import com.example.rxjava.functions.Func1;

/**
 * 订阅源（被观察者）
 * Created by Administrator on 2017/12/30.
 */
public class Observable<T> {

    public interface OnSubscribe<T> {
        void call(Subscriber<? super T> subscriber);
    }

    private final OnSubscribe<T> onSubscribe;

    private Observable(OnSubscribe<T> onSubscribe) {
        this.onSubscribe = onSubscribe;
    }

    public static <T> Observable<T> create(OnSubscribe<T> onSubscribe) {
        return new Observable<T>(onSubscribe);
    }

    public void subscribe(Subscriber<? super T> subscriber) {
        subscriber.onStart();
        onSubscribe.call(subscriber);
    }

    public <R> Observable<R> map(Func1<? super T, ? extends R> func) {
        return create(new OnSubscribeMap<T, R>(this, func));
    }

    /**
     * subscribeOn()作用的是OnSubscribe
     * @param scheduler
     * @return
     */
    public Observable<T> subscribeOn(final Scheduler scheduler) {
        return Observable.create(new OnSubscribe<T>() {
            @Override
            public void call(final Subscriber<? super T> subscriber) {
                subscriber.onStart();
                // 将事件切换到新的线程
                scheduler.createWorker().schedule(new Runnable() {
                    @Override
                    public void run() {
                        // 让OnSubscribe的call方法在新线程中执行
                        Observable.this.onSubscribe.call(subscriber);
                    }
                });
            }
        });
    }

    /**
     * observeOn()作用的是Subscriber
     * @param scheduler
     * @return
     */
    public Observable<T> observeOn(final Scheduler scheduler) {
        return Observable.create(new OnSubscribe<T>() {
            @Override
            public void call(final Subscriber<? super T> subscriber) {
                subscriber.onStart();
                final Scheduler.Worker worker = scheduler.createWorker();
                Observable.this.onSubscribe.call(new Subscriber<T>() {
                    @Override
                    public void onCompleted() {
                        worker.schedule(new Runnable() {
                            @Override
                            public void run() {
                                subscriber.onCompleted();
                            }
                        });
                    }

                    @Override
                    public void onError(final Throwable t) {
                        worker.schedule(new Runnable() {
                            @Override
                            public void run() {
                                subscriber.onError(t);
                            }
                        });
                    }

                    @Override
                    public void onNext(final T var1) {
                        worker.schedule(new Runnable() {
                            @Override
                            public void run() {
                                subscriber.onNext(var1);
                            }
                        });
                    }
                });
            }
        });
    }
}
