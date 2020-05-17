package com.example.rxjava.functions;

/**
 * 实用程序方法转换双函数
 * Created by Administrator on 2020/5/17.
 */
public class Functions {

    public static <T1, T2, R> Function<Object[], R> toFunction(final Function2<T1, T2, R> f) {
        return new Array2Func<T1, T2, R>(f);
    }

    static final class Array2Func<T1, T2, R> implements Function<Object[], R> {
        final Function2<T1, T2, R> f;

        Array2Func(Function2<T1, T2, R> f) {
            this.f = f;
        }

        @Override
        public R apply(Object[] a) throws Exception {
            if (a.length != 2) {
                throw new IllegalArgumentException("Array of size 2 expected but got " + a.length);
            }
            return f.apply((T1)a[0], (T2)a[1]);
        }
    }
}
