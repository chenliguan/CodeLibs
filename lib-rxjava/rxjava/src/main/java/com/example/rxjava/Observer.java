package com.example.rxjava;

/**
 * Created by Administrator on 2017/12/30.
 */
public interface Observer<T> {
    void onCompleted();

    void onError(Throwable t);

    void onNext(T var1);
}