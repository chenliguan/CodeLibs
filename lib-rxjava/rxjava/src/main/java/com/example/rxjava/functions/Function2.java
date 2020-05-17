package com.example.rxjava.functions;

import android.support.annotation.NonNull;

/**
 * 一个函数接口(回调)，它根据多个输入值计算一个值。
 * @param <T1> the first value type
 * @param <T2> the second value type
 * @param <R> the result type
 */
public interface Function2<T1, T2, R> {
    /**
     * 根据输入值计算一个值
     *
     * @param t1 the first value
     * @param t2 the second value
     * @return the result value
     * @throws Exception on error
     */
    @NonNull
    R apply(@NonNull T1 t1, @NonNull T2 t2) throws Exception;
}
