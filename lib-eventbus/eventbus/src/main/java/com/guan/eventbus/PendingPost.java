package com.guan.eventbus;

/**
 * Author: chen
 * Version: 1.0.0
 * Date: 2020/5/31
 * Mender:
 * Modify:
 * Description: 包装了订阅者+订阅者类的方法的对象 + 事件对象
 */
public class PendingPost {

    public Object event;
    public Subscription subscription;

    public PendingPost(Object event, Subscription subscription) {
        this.event = event;
        this.subscription = subscription;
    }
}
