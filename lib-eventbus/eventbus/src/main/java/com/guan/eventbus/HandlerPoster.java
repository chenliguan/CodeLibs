package com.guan.eventbus;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.util.LinkedList;

/**
 * Author: 陈李冠
 * Version: 1.0.0
 * Date: 2020/6/7
 * Mender:
 * Modify:
 * Description: 主线程中post事件
 */
public class HandlerPoster extends Handler implements Poster {

    private final LinkedList<PendingPost> queue;
    private final EventBus eventBus;
    private boolean handlerActive;

    protected HandlerPoster(EventBus eventBus, Looper looper) {
        super(looper);
        this.eventBus = eventBus;
        this.queue = new LinkedList<>();
    }

    public void enqueue(Subscription subscription, Object event) {
        synchronized (this) {
            PendingPost pendingPost = new PendingPost(event, subscription);
            queue.offer(pendingPost);
            if (!handlerActive) {
                handlerActive = true;
                if (!sendMessage(obtainMessage())) {
                    Log.e(EventBus.TAG, "Could not send handler message");
                }
            }
        }
    }

    @Override
    public void handleMessage(Message msg) {
        boolean rescheduled = false;
        try {
            while (true) {
                PendingPost pendingPost = queue.poll();
                if (pendingPost == null) {
                    synchronized (this) {
                        // 再次检查，这次是同步的
                        pendingPost = queue.poll();
                        if (pendingPost == null) {
                            handlerActive = false;
                            return;
                        }
                    }
                }

                eventBus.invokeSubscriber(pendingPost.subscription, pendingPost.event);
                rescheduled = true;
            }
        } finally {
            handlerActive = rescheduled;
        }
    }
}