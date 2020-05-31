package com.guan.eventbus;

/**
 * Author: 陈李冠
 * Version: 1.0.0
 * Date: 2020/5/31
 * Mender:
 * Modify:
 * Description: 方法执行的线程
 */
public enum ThreadMode {

    /**
     * 订阅者将在Android的主线程(UI线程)中被调用。如果发布线程是主线程、订阅方方法将被直接调用，阻塞发布线程。
     * 否则事件正在排队等待交付(非阻塞)。使用此模式的订阅者必须快速返回，以避免阻塞主线程。
     */
    MAIN,

    /**
     * 订阅者将在后台线程中被调用。如果发布线程不是主线程，订阅方方法将在发布线程中直接调用。
     * 如果提交线程是主线程，则EventBus使用单个线程后台线程，它将按顺序交付其所有事件。
     * 使用此模式的订户应尝试这样做快速返回，避免阻塞后台线程。
     */
    BACKGROUND
}
