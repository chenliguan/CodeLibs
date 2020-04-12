package com.example.rxjava.schedulers;

import java.util.concurrent.Executor;

/**
 * 线程调度器
 * A Scheduler is an object that schedules units of work：进行任务的调度的一个东西
 * Created by Administrator on 2017/12/30.
 */
public class Scheduler {

    // 执行线程池
    private final Executor executor;

    public Scheduler(Executor executor) {
        this.executor = executor;
    }

    public Worker createWorker() {
        return new Worker(executor);
    }

    /**
     * 直接调度线程池执行Runnable任务
     *
     * @param runnable
     */
    public void scheduleDirect(Runnable runnable) {
        final Worker worker = createWorker();

        worker.schedule(runnable);
    }

    /**
     * Scheduler的内部类，它是具体任务的执行者
     */
    public class Worker {
        final Executor executor;

        public Worker(Executor executor) {
            this.executor = executor;
        }

        /**
         * 调度线程池执行Runnable任务
         *
         * @param runnable
         */
        public void schedule(Runnable runnable) {
            executor.execute(runnable);
        }
    }
}
