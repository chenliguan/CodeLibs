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

    private static volatile EventBus defaultInstance;
    /**
     * 接收事件的方法存储在这个列表中
     */
    private final Map<Object, CopyOnWriteArrayList<SubscriberMethod>> subscriptionsByEventType;
    /**
     * 接收订阅者存储在这个列表中
     */
    private final Map<Object, List<Class<?>>> typesBySubscriber;
    
    private Handler handler;

    private EventBus() {
        this.subscriptionsByEventType = new HashMap<>();
        this.typesBySubscriber = new HashMap<>();
        this.handler = new Handler();
    }

    /** 使用一个进程范围的EventBus实例为应用程序提供方便的单例 */
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
        List<SubscriberMethod> subscriberMethods = findSubscriberMethods(subscriber);
        synchronized (this) {
            for (SubscriberMethod subscriberMethod : subscriberMethods) {
                subscribe(subscriber, subscriberMethod);
            }
        }
    }

    /**
     * 订阅：Must be called in synchronized block
     *
     * @param subscriber
     * @param subscriberMethod
     */
    private void subscribe(Object subscriber, SubscriberMethod subscriberMethod) {
        Class<?> eventType = subscriberMethod.eventType;
        CopyOnWriteArrayList<SubscriberMethod> subscriberMethods = subscriptionsByEventType.get(subscriber);
        if (subscriberMethods == null) {
            subscriberMethods = findSubscriberMethods(subscriber);
            subscriptionsByEventType.put(subscriber, subscriberMethods);
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
     * 通过反射获取订阅者Class对象的方法集合
     *
     * @param object
     * @return
     */
    private CopyOnWriteArrayList<SubscriberMethod> findSubscriberMethods(Object object) {
        CopyOnWriteArrayList<SubscriberMethod> subscriberMethods = new CopyOnWriteArrayList<>();
        Class<?> clazz = object.getClass();
        // 通过getDeclaredMethods()来获取类中所有方法而并不是通过getMethods()，由于前者只反射当前类的方法(不包括隐式继承的父类方法)，所以前者的效率较后者更高些
        Method[] methods = clazz.getDeclaredMethods();

        while (clazz != null) {
            // 找父类的时候 需要先判断一下是否是 系统级别的父类
            String name = clazz.getName();
            if (name.startsWith("java.") || name.startsWith("javax.") || name.startsWith("android.")) {
                break;
            }

            for (Method method : methods) {
                // 找到带有SubScribe 注解的方法
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
                subscriberMethods.add(subscriberMethod);
            }

            clazz = clazz.getSuperclass();
        }

        return subscriberMethods;
    }


    /**
     * 发布事件
     *
     * @param type
     */
    public void post(final Object type) {
        // 直接循环 Map 里面的方法 找到对应的回调
        Set<Object> set = subscriptionsByEventType.keySet();
        for (final Object obj : set) {
            List<SubscriberMethod> subscriberMethods = subscriptionsByEventType.get(obj);
            for (final SubscriberMethod subscriberMethod : subscriberMethods) {
                // a(if 条件前面的对象) 对象所对应的类信息是不是 b (if 条件后面的对象)对应所对应的类信息的父类或接口
                if (subscriberMethod.eventType.isAssignableFrom(type.getClass())) {
                    switch (subscriberMethod.threadMode) {
                        case MAIN:
                            // 主 --- 主
                            if (Looper.myLooper() == Looper.getMainLooper()) {
                                invokeSubscriber(subscriberMethod, obj, type);
                            } else {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        invokeSubscriber(subscriberMethod, obj, type);
                                    }
                                });
                            }

                            // 子 --- 主
                            break;
                        case BACKGROUND:
                            // ExecutorService 从主线程到子线程的切换
                            break;

                        default:
                            break;
                    }

//                    invoke(subscribeMethod, obj, type);
                }
            }
        }
    }

    /**
     * 调用method对应的方法
     *
     * @param subscriberMethod
     * @param obj
     * @param type
     */
    private void invokeSubscriber(SubscriberMethod subscriberMethod, Object obj, Object type) {
        Method method = subscriberMethod.method;
        try {
            method.invoke(obj, type);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 注销订阅者和它的事件方法
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
}
