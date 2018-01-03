package com.example.rxjava_example;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.rxjava.Observable;
import com.example.rxjava.Schedulers;
import com.example.rxjava.Subscriber;
import com.example.rxjava.android.AndroidSchedulers;
import com.example.rxjava.functions.Func1;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 基本的RxJava例子
        textRxJava1();
        // map操作符的作用是将T类型的Event转化成R类型
        textRxJava2();
        // 线程切换
        textRxJava3();
    }

    /**
     * 基本的RxJava例子
     */
    public void textRxJava1() {
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                for (int i = 0; i < 10; i++) {
                    subscriber.onNext(i);
                }
            }
        }).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onNext(Integer var1) {
                System.out.println(var1);
            }
        });
    }

    /**
     * map操作符的作用是将T类型的Event转化成R类型
     */
    private void textRxJava2() {
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                Log.e("tag", "create + call:" + 111);
                subscriber.onNext(111);
            }
        }).map(new Func1<Integer, String>() {
            @Override
            public String call(Integer from) {
                Log.e("tag", "create + Func1:" + from);
                return "maping " + from;
            }
        }).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Log.e("tag", "onCompleted");
            }

            @Override
            public void onError(Throwable t) {
                Log.e("tag", "onError:" + t.toString());
            }

            @Override
            public void onNext(String var1) {
                Log.e("tag", "onNext:" + var1);
            }
        });
    }

    /**
     * 线程切换
     *
     * subscribeOn()作用的是OnSubscribe
     * observeOn()作用的是Subscriber
     */
    private void textRxJava3() {
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                Log.e("tag", "OnSubscribe3call@ " + Thread.currentThread().getName());
                subscriber.onNext(1);
            }
        }).map(new Func1<Integer, String>() {
            @Override
            public String call(Integer from) {
                Log.e("tag", "OnSubscribe3Func1@ " + Thread.currentThread().getName());
                return "1";
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onNext(String var1) {
                        Log.e("tag", "Subscriber3onNext@ " + Thread.currentThread().getName());
                    }
                });
    }
}
