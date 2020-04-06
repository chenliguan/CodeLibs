package com.example.rxjava.schedulers;

import java.util.concurrent.Executors;

/**
 * Scheduler调度管理器
 * Created by Administrator on 2017/12/30.
 */
public class Schedulers {

    private volatile static Schedulers current;

    // newSingleThreadExecutor创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，
    private final Scheduler ioScheduler;
    private final Scheduler newThreadScheduler;

    private Schedulers() {
        ioScheduler = new Scheduler(Executors.newSingleThreadExecutor());
        newThreadScheduler = new Scheduler(Executors.newSingleThreadExecutor());
    }

    private static Schedulers getInstance() {
        if (current == null) {
            synchronized (Schedulers.class) {
                if (current == null) {
                    current = new Schedulers();
                }
            }
        }
        return current;
    }

    public static Scheduler io() {
        return getInstance().ioScheduler;
    }
}
