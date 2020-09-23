package com.guan.eventbus;

import android.os.Looper;

/**
 * Author: chen
 * Version: 1.0.0
 * Date: 2020/6/7
 * Mender:
 * Modify:
 * Description: 判断是否是主线程，通常在Android上使用Android的主线程
 */
public interface MainThreadSupport {

    boolean isMainThread();

    Poster createPoster(EventBus eventBus);

    class AndroidHandlerMainThreadSupport implements MainThreadSupport {

        private final Looper looper;

        public AndroidHandlerMainThreadSupport(Looper looper) {
            this.looper = looper;
        }

        @Override
        public boolean isMainThread() {
            return looper == Looper.myLooper();
        }

        @Override
        public Poster createPoster(EventBus eventBus) {
            return new HandlerPoster(eventBus, looper);
        }
    }
}