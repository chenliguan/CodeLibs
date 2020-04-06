package com.example.rxjava.observable;

import com.example.rxjava.observer.Observer;

/**
 * 一个函数接口，它有一个{@code subscribe()}方法来接收一个允许推送的实例
 * Created by Administrator on 2020/4/6.
 */
public interface ObservableOnSubscribe<T> {

    /**
     * 调用订阅的每个观察者，{@code subscribe()}方法来接收一个允许推送的实例
     *
     * @param observer 观察者
     * @throws Exception on error
     */
    void subscribe(Observer<? super T> observer);
}
