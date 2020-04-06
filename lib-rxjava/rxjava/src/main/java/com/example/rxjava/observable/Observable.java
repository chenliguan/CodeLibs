package com.example.rxjava.observable;

import android.util.Log;

import com.example.rxjava.functions.Function;
import com.example.rxjava.observer.Observer;
import com.example.rxjava.schedulers.Scheduler;

/**
 * 订阅源（被观察者）
 * Created by Administrator on 2017/12/30.
 */
public abstract class Observable<T> {

    public static final String TAG = "RxJavaObservable";

    /**
     * 订阅（subscribe）连接观察者和被观察者的具体实现
     */
    protected abstract void subscribeActual(Observer<? super T> observer);

    /**
     * 创建操作符
     *
     * @param source
     * @param <T>
     * @return
     */
    public static <T> Observable<T> create(ObservableOnSubscribe<T> source) {
        // 创建ObservableCreate对象
        Log.e(Observable.TAG, "调用create创建ObservableCreate对象");
        return new ObservableCreate<T>(source);
    }

    /**
     * map 操作符
     *
     * @param func
     * @param <R>
     * @return
     */
    public <R> Observable<R> map(Function<? super T, ? extends R> func) {
        // 创建一个桥接前一个操作符和当前操作符的Observable子类-ObservableMap对象
        Log.e(Observable.TAG, "调用map创建ObservableMap对象");
        return new ObservableMap<>(this, func);
    }

    /**
     * 通过订阅（subscribe）连接观察者和被观察者
     *
     * @param observer
     */
    public void subscribe(Observer<? super T> observer) {
        // 当Observable.subscribe被调用时，回调subscribeActual(observer)
        subscribeActual(observer);
    }

    /**
     * subscribeOn()操作符，作用的是OnSubscribe
     *
     * @param scheduler
     * @return
     */
    public Observable<T> subscribeOn(final Scheduler scheduler) {
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(final Observer<? super T> subscriber) {
                subscriber.onSubscribe();
                // 将事件切换到新的线程
                scheduler.createWorker().schedule(new Runnable() {
                    @Override
                    public void run() {
                        // 让OnSubscribe的call方法在新线程中执行
                        Observable.this.subscribe(subscriber);
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
        return Observable.create(new ObservableOnSubscribe<T>() {

            @Override
            public void subscribe(final Observer<? super T> observer) {
                final Scheduler.Worker worker = scheduler.createWorker();
                Observable.this.subscribe(new Observer<T>() {
                    @Override
                    public void onSubscribe() {

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
                    public void onNext(final T var1) {
                        worker.schedule(new Runnable() {
                            @Override
                            public void run() {
                                observer.onNext(var1);
                            }
                        });
                    }
                });
            }
        });
    }
}
