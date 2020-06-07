package com.guan.eventbus;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ExpandableListView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

/**
 * Author: 陈李冠
 * Version: 1.0.0
 * Date: 2020/5/31
 * Mender:
 * Modify:
 * Description: EventBus发布/订阅事件总线
 */
public class EventBus {

    public static String TAG = "Tag_EventBus";

    private final static ExecutorService DEFAULT_EXECUTOR_SERVICE = Executors.newCachedThreadPool();

    private static volatile EventBus defaultInstance;
    /**
     * 判断是否是主线程
     */
    private final MainThreadSupport mainThreadSupport;
    private final Poster mainThreadPoster;
    private final BackgroundPoster backgroundPoster;
    /**
     * 接收 订阅者+订阅者类的方法的对象 存储在这个列表中
     */
    private final Map<Object, CopyOnWriteArrayList<Subscription>> subscriptionsByEventType;
    /**
     * 接收订阅者存储在这个列表中
     */
    private final Map<Object, List<Class<?>>> typesBySubscriber;
    /**
     * Looper为MainLooper
     */
    private Handler handler;

    private EventBus() {
        this.mainThreadSupport = new MainThreadSupport.AndroidHandlerMainThreadSupport(Looper.getMainLooper());;
        this.mainThreadPoster = mainThreadSupport != null ? mainThreadSupport.createPoster(this) : null;
        this.backgroundPoster = new BackgroundPoster(this);
        this.subscriptionsByEventType = new HashMap<>();
        this.typesBySubscriber = new HashMap<>();
        this.handler = new Handler(Looper.getMainLooper());
    }

    /**
     * 使用一个进程范围的EventBus实例为应用程序提供方便的单例
     */
    public static EventBus getDefault() {
        if (defaultInstance == null) {
            synchronized (EventBus.class) {
                if (defaultInstance == null) {
                    defaultInstance = new EventBus();
                }
            }
        }
        return defaultInstance;
    }

    /**
     * 注册订阅者
     *
     * @param subscriber
     */
    public void register(Object subscriber) {
        List<Subscription> subscriptions = findSubscriberMethods(subscriber);
        synchronized (this) {
            for (Subscription subscription : subscriptions) {
                subscribe(subscriber, subscription);
            }
        }
    }

    /**
     * 订阅：Must be called in synchronized block
     *
     * @param subscriber
     * @param subscription
     */
    private void subscribe(Object subscriber, Subscription subscription) {
        Class<?> eventType = subscription.subscriberMethod.eventType;
        CopyOnWriteArrayList<Subscription> subscriptions = subscriptionsByEventType.get(subscriber);
        if (subscriptions == null) {
            subscriptions = findSubscriberMethods(subscriber);
            subscriptionsByEventType.put(subscriber, subscriptions);
        }

        List<Class<?>> subscribedEvents = typesBySubscriber.get(subscriber);
        if (subscribedEvents == null) {
            subscribedEvents = new ArrayList<>();
            typesBySubscriber.put(subscriber, subscribedEvents);
        }
        subscribedEvents.add(eventType);
    }

    /**
     * 遍历订阅者类的方法
     * 通过反射获取订阅者+订阅者类的方法对象的集合，找到被SubScribe注解订阅的方法，如:onEvent()
     *
     * @param subscriber
     * @return
     */
    private CopyOnWriteArrayList<Subscription> findSubscriberMethods(Object subscriber) {
        CopyOnWriteArrayList<Subscription> subscriptions = new CopyOnWriteArrayList<>();
        Class<?> clazz = subscriber.getClass();
        // 通过getDeclaredMethods()来获取类中所有方法而并不是通过getMethods()，由于前者只反射当前类的方法(不包括隐式继承的父类方法)，所以前者的效率较后者更高些
        Method[] methods = clazz.getDeclaredMethods();

        while (clazz != null) {
            // 找父类的时候 需要先判断一下是否是 系统级别的父类
            String name = clazz.getName();
            if (name.startsWith("java.") || name.startsWith("javax.") || name.startsWith("android.")) {
                break;
            }

            for (Method method : methods) {
                // 找到被SubScribe注解订阅的方法
                Subscribe subscribe = method.getAnnotation(Subscribe.class);
                if (subscribe == null) {
                    continue;
                }
                // 判断带有Subscribe注解方法中的参数类型
                Class<?>[] types = method.getParameterTypes();
                if (types.length != 1) {
                    Log.e(EventBus.TAG, "EventBus只接受一个参数");
                    continue;
                }

                ThreadMode threadMode = subscribe.threadMode();
                SubscriberMethod subscriberMethod = new SubscriberMethod(method, threadMode, types[0]);
                Subscription newSubscription = new Subscription(subscriber, subscriberMethod);
                subscriptions.add(newSubscription);
            }

            clazz = clazz.getSuperclass();
        }

        return subscriptions;
    }


    /**
     * 发布事件
     * 匹配"被SubScribe注解订阅的方法的参数"和"发布者发布的对象"一致，即可分发Event
     *
     * @param event
     */
    public void post(final Object event) {
        // 直接循环 Map 里面的方法 找到"被SubScribe注解订阅的方法的参数"和"发布者发布的对象"一致的方法
        Set<Object> subscribers = subscriptionsByEventType.keySet();

        // 遍历所有订阅者对象，包括接口
        for (final Object subscriber : subscribers) {
            List<Subscription> subscriptions = subscriptionsByEventType.get(subscriber);
            if (subscriptions == null) {
                return;
            }

            // 遍历此订阅者+订阅者类的方法对象的所有方法
            for (final Subscription subscription : subscriptions) {
                // eventType对象所对应的类信息是不是event.getClass()对象所对应的类信息的父类或接口
                if (subscription.subscriberMethod.eventType.isAssignableFrom(event.getClass())) {

                    postToSubscription(subscription, event, isMainThread());
                }
            }
        }
    }

    private void postToSubscription(Subscription subscription, Object event, boolean isMainThread) {
        switch (subscription.subscriberMethod.threadMode) {
            case POSTING:
                invokeSubscriber(subscription, event);
                break;
            case MAIN:
                if (isMainThread) {
                    invokeSubscriber(subscription, event);
                } else {
                    mainThreadPoster.enqueue(subscription, event);
                }
                break;
            case BACKGROUND:
                if (isMainThread) {
                    backgroundPoster.enqueue(subscription, event);
                } else {
                    invokeSubscriber(subscription, event);
                }
                break;
            default:
                throw new IllegalStateException("Unknown thread mode: " + subscription.subscriberMethod.threadMode);
        }
    }

    /**
     * 执行订阅者订阅的方法，参数是event
     *
     * @param subscription
     * @param event
     */
    public void invokeSubscriber(Subscription subscription, Object event) {
        try {
            subscription.subscriberMethod.method.invoke(subscription.subscriber, event);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Unexpected exception", e);
        }
    }

    /**
     * 反注册：移除订阅者和它的事件方法
     *
     * @param subscriber
     */
    public void unregister(Object subscriber) {
        List<Class<?>> subscribedTypes = typesBySubscriber.get(subscriber);
        if (subscribedTypes != null) {
            subscriptionsByEventType.remove(subscriber);
            typesBySubscriber.remove(subscriber);
            subscribedTypes.clear();
        } else {
            Log.e(EventBus.TAG, "Subscriber to unregister was not registered before: " + subscriber.getClass());
        }
    }

    /**
     * 检查当前线程是否在主线程中运行。如果没有主线程支持(例如非android)，总是返回“true”。
     * 在这种情况下，主线程订阅者总是在发布线程中调用，而后台订阅者总是从后台海报中调用
     */
    public boolean isMainThread() {
        return mainThreadSupport == null || mainThreadSupport.isMainThread();
    }

    /**
     * 获取线程池
     *
     * @return
     */
    public ExecutorService getExecutorService() {
        return DEFAULT_EXECUTOR_SERVICE;
    }
}
