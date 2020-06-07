package com.guan.eventbus;

import java.lang.reflect.Method;

/**
 * Author: 陈李冠
 * Version: 1.0.0
 * Date: 2020/5/31
 * Mender:
 * Modify:
 * Description: 订阅者类的方法实体
 */
public class SubscriberMethod {

    /**
     * 回调方法
     */
    public Method method;
    /**
     * 线程模式
     */
    public ThreadMode threadMode;
    /**
     * 方法中的参数
     */
    public Class<?> eventType;
    /**
     * 是否是粘滞事件
     */
    public final boolean sticky;

    public SubscriberMethod(Method method, ThreadMode threadMode, Class<?> eventType, boolean sticky) {
        this.method = method;
        this.threadMode = threadMode;
        this.eventType = eventType;
        this.sticky = sticky;
    }
}
