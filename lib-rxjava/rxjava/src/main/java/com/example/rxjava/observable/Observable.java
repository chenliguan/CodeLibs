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
        Log.d(Observable.TAG, "调用create创建ObservableCreate对象");
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
        Log.d(Observable.TAG, "调用map创建ObservableMap对象");
        return new ObservableMap<>(this, func);
    }

    /**
     * subscribeOn()操作符，作用的是OnSubscribe
     *
     * @param scheduler
     * @return
     */
    public Observable<T> subscribeOn(final Scheduler scheduler) {
        // 创建一个桥接前一个操作符和当前操作符的Observable子类-ObservableSubscribeOn对象
        Log.d(Observable.TAG, "调用subscribeOn创建ObservableSubscribeOn对象");
        return new ObservableSubscribeOn<>(this, scheduler);
    }

    /**
     * observeOn()作用的是Subscriber
     *
     * @param scheduler
     * @return
     */
    public Observable<T> observeOn(final Scheduler scheduler) {
        // 创建一个桥接前一个操作符和当前操作符的Observable子类-ObservableObserveOn
        Log.d(Observable.TAG, "调用observeOn创建ObservableObserveOn对象");
        return new ObservableObserveOn<>(this, scheduler);
    }

    /**
     * 通过订阅（subscribe）连接观察者和被观察者
     *
     * @param observer
     */
    public void subscribe(Observer<? super T> observer) {
        Log.d(Observable.TAG, "调用订阅subscribe(observer)连接观察者和被观察者，observer：" + observer);
        // 当Observable.subscribe被调用时，回调subscribeActual(observer)
        subscribeActual(observer);
    }

}
