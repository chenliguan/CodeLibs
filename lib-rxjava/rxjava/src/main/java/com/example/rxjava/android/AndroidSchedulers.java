package com.example.rxjava.android;

import android.os.Handler;
import android.os.Looper;

import com.example.rxjava.schedulers.Scheduler;

import java.util.concurrent.Executors;

/**
 * Android线程调度器管理
 * Created by Administrator on 2017/12/31 0031.
 */
public class AndroidSchedulers {

    private AndroidSchedulers() {
    }

    /**
     * Android主线程：操作UI
     *
     * @return
     */
    public static Scheduler mainThread() {
        return MainThreadSchedulerHolder.MAIN_THREAD_SCHEDULER;
    }

    private static class MainThreadSchedulerHolder {
        private static final Scheduler MAIN_THREAD_SCHEDULER = new HandlerScheduler(
                Executors.newSingleThreadExecutor(), new Handler(Looper.getMainLooper()));
    }
}
