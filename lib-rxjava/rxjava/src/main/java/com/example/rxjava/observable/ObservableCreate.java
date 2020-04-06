package com.example.rxjava.observable;

import android.util.Log;

import com.example.rxjava.observer.Observer;

/**
 * 订阅源-创建操作符
 * Created by Administrator on 2017/12/30.
 */
public class ObservableCreate<T> extends Observable<T> {

    private ObservableOnSubscribe<T> source;

    // 传入的source对象 = 手动创建的ObservableOnSubscribe对象
    public ObservableCreate(ObservableOnSubscribe<T> source) {
        this.source = source;
    }

    /**
     * 关注：当Observable.subscribe被调用时，subscribeActual(observer)被立刻回调
     * 作用：调用source对象（即ObservableOnSubscribe对象）的subscribe(observer)
     *
     * @param observer
     */
    @Override
    protected void subscribeActual(Observer<? super T> observer) {
        Log.e(Observable.TAG, "回调ObservableCreate的subscribeActual()，作用:调用source对象（即ObservableOnSubscribe对象）的subscribe(observer)");

        observer.onSubscribe();
        source.subscribe(observer);
    }
}
