package com.example.rxjava.observable;

import android.util.Log;

import com.example.rxjava.functions.Function;
import com.example.rxjava.observer.Observer;

/**
 * 订阅源-map操作符
 * Created by Administrator on 2020/4/5.
 */
public class ObservableMap<T, R> extends Observable<R> {

    private final Observable<T> source;
    private final Function<? super T, ? extends R> function;

    // 传入的source对象 = 前一个操作符返回的Observable对象（Observable<T> implements ObservableSource<T>）
    // 传入的function对象 = Map变换操作符中的Function函数对象
    public ObservableMap(Observable<T> source, Function<? super T, ? extends R> function) {
        this.source = source;
        this.function = function;
    }

    /**
     * 关注：当Observable.subscribe被调用时，subscribeActual(observer)被立刻回调
     * 作用：调用前一个操作符返回的Observable对象的subscribe(observer)
     *
     * @param observer
     */
    @Override
    protected void subscribeActual(Observer<? super R> observer) {
        Log.e(Observable.TAG, "回调ObservableMap的subscribeActual()，作用:准备调用前一个操作符返回的Observable对象的subscribe(observer)");

        // 创建一个桥接 下层观察者 和 Map-Function函数对象 的 Observer子类-MapObserver对象
        source.subscribe(new MapObserver<>(observer, function));
    }

    private class MapObserver<T, R> implements Observer<R> {
        final Observer<? super T> observer;
        final Function<? super R, ? extends T> function;

        private MapObserver(Observer<? super T> observer, Function<? super R, ? extends T> function) {
            this.observer = observer;
            this.function = function;
        }

        @Override
        public void onSubscribe() {

        }

        @Override
        public void onNext(R var1){
            // 进行转换和处理，再把结果返回给下层观察者
            T t = null;
            try {
                Log.e(Observable.TAG, "回调MapObserver的onNext()，作用:进行转换和处理后，准备调用map操作符的apply()");
                // 调用map的apply()
                t = function.apply(var1);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            // 调用下层观察者的onNext()
            observer.onNext(t);
        }

        @Override
        public void onError(Throwable t) {
            observer.onError(t);
        }

        @Override
        public void onComplete() {
            observer.onComplete();
        }
    }
}
