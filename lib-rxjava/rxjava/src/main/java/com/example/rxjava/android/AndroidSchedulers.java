package com.example.rxjava.android;

import android.os.Handler;
import android.os.Looper;

import com.example.rxjava.Scheduler;

import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2017/12/31 0031.
 */
public class AndroidSchedulers {

    private AndroidSchedulers() {
    }

    public static Scheduler mainThread() {
        return MainThreadSchedulerHolder.MAIN_THREAD_SCHEDULER;
    }

    private static class MainThreadSchedulerHolder {
        private static final Scheduler MAIN_THREAD_SCHEDULER = new HandlerScheduler(
                Executors.newSingleThreadExecutor(),new Handler(Looper.getMainLooper()));
    }
}
