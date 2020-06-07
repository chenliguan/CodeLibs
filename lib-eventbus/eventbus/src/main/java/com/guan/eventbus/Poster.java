package com.guan.eventbus;

/**
 * Author: 陈李冠
 * Version: 1.0.0
 * Date: 2020/6/7
 * Mender:
 * Modify:
 * Description: post事件
 */
interface Poster {

    /**
     * 为特定订阅加入要发布的事件队列
     *
     * @param subscription 订阅将会收到事件的订阅
     * @param event        事件将会发布给订阅者.
     */
    void enqueue(Subscription subscription, Object event);
}