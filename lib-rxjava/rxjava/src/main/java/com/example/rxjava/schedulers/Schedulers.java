package com.example.rxjava.schedulers;

import java.util.concurrent.Executors;

/**
 * 线程调度器管理
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

    /**
     * io操作线程，执行网络请求、读写文件等io密集型操作
     *
     * @return
     */
    public static Scheduler io() {
        return getInstance().ioScheduler;
    }

    /**
     * 常规新线程，执行耗时等操作
     *
     * @return
     */
    public static Scheduler newThread() {
        return getInstance().newThreadScheduler;
    }
}
