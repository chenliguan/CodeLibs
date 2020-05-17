package com.example.rxjava.observable;

import android.util.Log;

import com.example.rxjava.functions.Function;
import com.example.rxjava.observer.Observer;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 订阅源-zip操作符（原理在于就是依靠队列+数组）
 * Created by Administrator on 2020/4/5.
 */
public final class ObservableZip<T, R> extends Observable<R> {

    private final Observable<? extends T>[] sources;
    private final Function<? super Object[], ? extends R> function;

    // 传入的sources数组 = 前一个操作符返回的Observable对象数组（Observable<T> implements ObservableSource<T>）
    // 传入的zipper对象 = Zip变换操作符中的Function函数对象
    public ObservableZip(Observable<? extends T>[] sources, Function<? super Object[], ? extends R> function) {
        this.sources = sources;
        this.function = function;
    }

    /**
     * 关注：当Observable.subscribe被调用时，subscribeActual(observer)被立刻回调
     * 作用：调用前一个操作符返回的Observable对象的subscribe(observer)
     *
     * @param observer
     */
    @Override
    public void subscribeActual(Observer<? super R> observer) {
        Log.e(Observable.TAG, "回调ObservableZip的subscribeActual()，作用:准备调用前一个操作符返回的Observable对象的subscribe(observer)");
        Observable<? extends T>[] sources = this.sources;
        int count = sources.length;

        // 创建一个协调器ZipCoordinator
        // observer：下游层观察者，function：Zip-Function函数，count：Observable对象数组的数量
        ZipCoordinator<T, R> zc = new ZipCoordinator<T, R>(observer, function, count);
        zc.subscribe(sources);
    }

    static final class ZipCoordinator<T, R> extends AtomicInteger {

        final Observer<? super R> observer;
        final Function<? super Object[], ? extends R> function;
        final ZipObserver<T, R>[] zipObservers;
        final T[] row; // 被观察者数量的数组

        ZipCoordinator(Observer<? super R> observer, Function<? super Object[], ? extends R> function, int count) {
            // 初始化了一个和上游Observable一样数量大小的ZipObserver数组和T类型的数组
            this.observer = observer;
            this.function = function;
            this.zipObservers = new ZipObserver[count];
            this.row = (T[]) new Object[count];
        }

        public void subscribe(Observable<? extends T>[] sources) {
            // 分别初始化了ZipObserver数组的元素
            // 桥接 上游Observable 和 ZipCoordinator对象-drain() 的 Observer子类-ZipObserver对象
            int len = zipObservers.length;
            for (int i = 0; i < len; i++) {
                zipObservers[i] = new ZipObserver<T, R>(this);
            }

            this.lazySet(0);
            observer.onSubscribe();

            // 让上游Observable分别订阅了对应的ZipObserver
            for (int i = 0; i < len; i++) {
                sources[i].subscribe(zipObservers[i]);
            }
        }

        private void drain() {
            if (getAndIncrement() != 0) {
                return;
            }

            int missing = 1;
            final T[] os = row;

            for (; ; ) {
                for (; ; ) {
                    int i = 0;
                    int emptyCount = 0;
                    for (ZipObserver<T, R> zipObserver : zipObservers) {
                        if (os[i] == null) {
                            // 3.1、如果数组i的元素为空，就去指定队列中poll
                            T v = zipObserver.queue.poll();
                            boolean empty = v == null;

                            if (!empty) {
                                // 4.1、如果不为null，则填充到数组的指定位置
                                os[i] = v;
                            } else {
                                // 4.2、如果poll出来null，说明该队列中还没有事件被发射过来，emptyCount++
                                emptyCount++;
                            }
                        } else {
                            // 3.2、如果数组i的元素不为空，判断是否结束，没结束无操作
                            if (zipObserver.done) {
                                clear();
                                observer.onError(new Throwable());
                                return;
                            }
                        }
                        // i++，遍历数组
                        i++;
                    }

                    // 5.1、emptyCount不为0则意味着数组没有被填满，某上游被观察者未发射事件，对应队列中还没有值，
                    // 所以只能结束此次操作，等待下一次上游发射事件了
                    if (emptyCount != 0) {
                        break;
                    }

                    // 5.2、如果emptyCount为0，那么说明数组中的值被填满了，这意味着符合触发下游Observer#onNext(T)的要求了
                    R r;
                    try {
                        // 6、调用zip的apply()进行转换和处理
                        Log.e(Observable.TAG, "调用zip的apply()进行转换和处理");
                        r = function.apply(os.clone());
                    } catch (Throwable ex) {
                        clear();
                        observer.onError(ex);
                        return;
                    }

                    // 7、再把结果返回给下游观察者触发下游 Observer#onNext(T)
                    observer.onNext(r);

                    // 8、将数组内部元素置null，为下次数据填充做准备。
                    Arrays.fill(os, null);
                }

                missing = addAndGet(-missing);
                if (missing == 0) {
                    return;
                }
            }
        }

        void clear() {
            for (ZipObserver<?, ?> zipObserver : zipObservers) {
                zipObserver.queue.clear();
            }
        }
    }

    static final class ZipObserver<T, R> implements Observer<T> {

        final ZipCoordinator<T, R> parent;
        final LinkedList<T> queue;
        volatile boolean done;

        ZipObserver(ZipCoordinator<T, R> parent) {
            this.parent = parent;
            this.queue = new LinkedList<T>();
        }

        @Override
        public void onSubscribe() {

        }

        @Override
        public void onNext(T t) {
            // 1、当一个上游事件被发射过来的时候，首先进入队列
            queue.offer(t);
            // 2、再去查看数组的每个元素是否为空
            parent.drain();
        }

        @Override
        public void onError(Throwable t) {
            done = true;
            parent.drain();
        }

        @Override
        public void onComplete() {
            done = true;
            parent.drain();
        }
    }
}

