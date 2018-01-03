package com.example.rxjava;

import com.example.rxjava.functions.Func1;

/**
 * map操作OnSubscribe
 * Created by Administrator on 2017/12/30.
 */
public class OnSubscribeMap<T, R> implements Observable.OnSubscribe<R> {
    private final Observable<T> source;
    private final Func1<? super T, ? extends R> transformer;

    public OnSubscribeMap(Observable<T> source, Func1<? super T, ? extends R> transformer) {
        this.source = source;
        this.transformer = transformer;
    }

    @Override
    public void call(Subscriber<? super R> subscriber) {
        source.subscribe(new MapSubscriber<R, T>(subscriber, transformer));
    }


    private class MapSubscriber<T, R> extends Subscriber<R> {
        OnSubscribeMap n;
        final Subscriber<? super T> actual;
        final Func1<? super R, ? extends T> mapper;

        private MapSubscriber(Subscriber<? super T> actual, Func1<? super R, ? extends T> mapper) {
            this.actual = actual;
            this.mapper = mapper;
        }

        @Override
        public void onCompleted() {
            actual.onCompleted();
        }

        @Override
        public void onError(Throwable t) {
            actual.onError(t);
        }

        @Override
        public void onNext(R var1) {
            T t = mapper.call(var1);
            actual.onNext(t);
        }
    }
}
