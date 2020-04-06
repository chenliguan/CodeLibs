package com.example.rxjava.observer;

/**
 * 订阅者（观察者）
 * Created by Administrator on 2017/12/30.
 */
public interface Observer<T> {

    /**
     * 开始采用subscribe连接
     */
    void onSubscribe();

    void onNext(T var1);

    void onError(Throwable t);

    void onComplete();
}