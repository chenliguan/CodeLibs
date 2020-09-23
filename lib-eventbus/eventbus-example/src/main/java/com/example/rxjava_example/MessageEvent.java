package com.example.rxjava_example;

/**
 * Author: chen
 * Version: 1.0.0
 * Date: 2020/5/31
 * Mender:
 * Modify:
 * Description: 定义事件
 */
public class MessageEvent {

    public String name;
    public String num;

    public MessageEvent(String name, String num) {
        this.name = name;
        this.num = num;
    }
}
