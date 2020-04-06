package com.example.rxjava.schedulers;

import java.util.concurrent.Executor;

/**
 * 调度器
 * Created by Administrator on 2017/12/30.
 */
public class Scheduler {

    // 执行线程
    private final Executor executor;

    public Scheduler(Executor executor) {
        this.executor = executor;
    }

    public Worker createWorker() {
        return new Worker(executor);
    }

    /**
     * 调度工作者
     */
    public class Worker {
        final Executor executor;

        public Worker(Executor executor) {
            this.executor = executor;
        }

        public void schedule(Runnable runnable) {
            executor.execute(runnable);
        }
    }
}
