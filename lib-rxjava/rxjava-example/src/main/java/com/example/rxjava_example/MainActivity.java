package com.example.rxjava_example;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.rxjava.functions.Function;
import com.example.rxjava.observable.Observable;
import com.example.rxjava.observable.ObservableOnSubscribe;
import com.example.rxjava.observer.Observer;
import com.example.rxjava.schedulers.Schedulers;
import com.example.rxjava.android.AndroidSchedulers;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * create操作符
     */
    public void subscribe(View view) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(Observer<? super Integer> observer) {
                Log.d(Observable.TAG, "回调create操作符的subscribe()");
                for (int i = 0; i < 3; i++) {
                    observer.onNext(i);
                }
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe() {
                Log.d(Observable.TAG, "开始采用subscribe连接");
            }

            @Override
            public void onNext(Integer value) {
                Log.d(Observable.TAG, "onNext接收到了事件：" + value);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(Observable.TAG, "对Error事件作出响应");
            }

            @Override
            public void onComplete() {
                Log.d(Observable.TAG, "对Complete事件作出响应");
            }
        });

        /**
         * 调用create创建ObservableCreate对象
         * 调用订阅subscribe(observer)连接观察者和被观察者，observer：com.example.rxjava_example.MainActivity$1@b6c1a61
         * 回调ObservableCreate的subscribeActual()，作用:准备调用source对象（即ObservableOnSubscribe对象）的subscribe(observer)
         * 调用source对象（即ObservableOnSubscribe对象）的subscribe(observer)
         * 开始采用subscribe连接
         * 回调create操作符的subscribe()
         * onNext接收到了事件：0
         * onNext接收到了事件：1
         * onNext接收到了事件：2
         */
    }

    /**
     * map操作符的作用是将T类型的Event转化成R类型
     */
    public void map(View view) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(Observer<? super Integer> subscriber) {
                Log.d(Observable.TAG, "回调create操作符的subscribe()，初始值是：" + 10);
                subscriber.onNext(10);
            }

        }).map(new Function<Integer, String>() {
            @Override
            public String apply(Integer integer) throws Exception {
                String str =  "事件" + integer + "的参数从整型：" + integer + "，变换成字符串类型：" + (integer + "s");
                Log.d(Observable.TAG, "回调map操作符的apply()，将" + str);
                return str;
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe() {
                Log.d(Observable.TAG, "开始采用subscribe连接");
            }

            @Override
            public void onNext(String value) {
                Log.d(Observable.TAG, "onNext接收到了事件：" + value);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(Observable.TAG, "对Error事件作出响应");
            }

            @Override
            public void onComplete() {
                Log.d(Observable.TAG, "对Complete事件作出响应");
            }
        });

        /**
         * 调用create创建ObservableCreate对象
         * 调用map创建ObservableMap对象
         * 调用订阅subscribe(observer)连接观察者和被观察者，observer：com.example.rxjava_example.MainActivity$3@516f986
         * 回调ObservableMap的subscribeActual()，作用:准备调用前一个操作符返回的Observerable对象的subscribe(observer)
         * 调用订阅subscribe(observer)连接观察者和被观察者，observer：com.example.rxjava.observable.ObservableMap$MapObserver@e734247
         * 回调ObservableCreate的subscribeActual()，作用:准备调用source对象（即ObservableOnSubscribe对象）的subscribe(observer)
         * 调用source对象（即ObservableOnSubscribe对象）的subscribe(observer)
         * 回调create操作符的subscribe()，初始值是：10
         * 回调MapObserver的onNext()，作用:进行转换和处理后，准备调用map操作符的apply()
         * 回调map操作符的apply()，将事件10的参数从整型：10，变换成字符串类型：10s
         * onNext接收到了事件：事件10的参数从整型：10，变换成字符串类型：10s
         */
    }

    /**
     * 线程切换
     * <p>
     * subscribeOn()作用的是OnSubscribe
     * observeOn()作用的是Subscriber
     */
    public void subscribeOn(View view) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(Observer<? super Integer> subscriber) {
                Log.d(Observable.TAG, "create创建后回调了subscribe() + 初始线程是："+ Thread.currentThread().getName());

                subscriber.onNext(1);
            }
        }).map(new Function<Integer, String>() {
            @Override
            public String apply(Integer from) {
                Log.d(Observable.TAG, "map后回调了apply() + 线程是："+ Thread.currentThread().getName());
                return "1";
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe() {
                        Log.d(Observable.TAG, "开始采用subscribe连接");
                    }

                    @Override
                    public void onNext(String value) {
                        Log.d(Observable.TAG, "onNext接收到了事件：" + value + " + 线程是："+ Thread.currentThread().getName());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(Observable.TAG, "对Error事件作出响应");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(Observable.TAG, "对Complete事件作出响应");
                    }
                });
    }
}
