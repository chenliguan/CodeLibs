package com.example.rxjava.functions;

import android.support.annotation.NonNull;

/**
 * 一个函数接口，它接受一个值并返回另一个值，可能带有一个不同的类型，允许抛出一个选中的异常
 */
public interface Function<T, R> {

    /**
     * 对输入值应用一些计算并返回一些其他值。
     *
     * @param t 输入值
     * @返回 输出值
     * @抛出 异常错误
     */
    R apply(@NonNull T t) throws Exception;
}