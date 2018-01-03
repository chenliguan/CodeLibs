package com.example.rxjava.android;

import android.os.Handler;

import com.example.rxjava.Scheduler;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/**
 * Handler调度器
 * Created by Administrator on 2017/12/31 0031.
 */
public final class HandlerScheduler extends Scheduler {
    private final Executor executor;
    private final Handler handler;

    public HandlerScheduler(Executor executor, Handler handler) {
        super(executor);
        this.executor = executor;
        this.handler = handler;
    }

    public Worker createWorker() {
        return new HandlerScheduler.HandlerWorker(executor,handler);
    }

    private class HandlerWorker extends Worker {

        private final Handler handler;

        private HandlerWorker(Executor executor,Handler handler) {
            super(executor);
            this.handler = handler;
        }

        public void schedule(Runnable runnable) {
            handler.post(runnable);
        }
    }
}