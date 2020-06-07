package com.guan.eventbus;

import java.util.LinkedList;

/**
 * Author: 陈李冠
 * Version: 1.0.0
 * Date: 2020/6/7
 * Mender:
 * Modify:
 * Description: 子线程中post事件
 */
public class BackgroundPoster implements Runnable, Poster {

    private final LinkedList<PendingPost> queue;
    private final EventBus eventBus;

    private volatile boolean executorRunning;

    BackgroundPoster(EventBus eventBus) {
        this.eventBus = eventBus;
        queue = new LinkedList<>();
    }

    public void enqueue(Subscription subscription, Object event) {
        synchronized (this) {
            PendingPost pendingPost = new PendingPost(event, subscription);
            queue.offer(pendingPost);
            if (!executorRunning) {
                executorRunning = true;
                eventBus.getExecutorService().execute(this);
            }
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                PendingPost pendingPost = queue.poll();
                if (pendingPost == null) {
                    synchronized (this) {
                        // 再次检查，这次是同步的
                        pendingPost = queue.poll();
                        if (pendingPost == null) {
                            executorRunning = false;
                            return;
                        }
                    }
                }

                eventBus.invokeSubscriber(pendingPost.subscription, pendingPost.event);
            }
        } finally {
            executorRunning = false;
        }
    }
}
