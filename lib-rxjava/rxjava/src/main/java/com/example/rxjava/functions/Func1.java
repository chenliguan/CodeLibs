package com.example.rxjava.functions;

/**
 * 抽象的转换函数
 * Created by Administrator on 2017/12/30 0030.
 */
public interface Func1<T, R> extends Function {
    R call(T var1);
}
