package com.guan.imageloader.core;

import android.util.Log;

import com.guan.imageloader.request.BitmapRequest;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import static android.content.ContentValues.TAG;

/**
 * 请求队列---并不是请求队列数据结构，而是对柱塞式队列的封装
 *
 * Created by Administrator on 2017/2/6 0006.
 */
public class RequestQueue {

    /**
     * 阻塞式队列
     * 多线程共享
     * 生产效率和消费效率相差太远了。
     * disPlayImage()
     * 使用优先级队列
     * 优先级高的队列先被消费
     * 每一个产品都有编号
     */
    private BlockingQueue<BitmapRequest> mBlockingQueue = new PriorityBlockingQueue<>();
    //转发器的数量
    private int threadCount;
    //i++  ++i  能 1  不能 2
    private AtomicInteger i = new AtomicInteger(0);
    //一组转发器
    private RequestDispatcher[] mDispachers;

    public RequestQueue(int threadCount) {
        this.threadCount = threadCount;
    }

    /**
     * 添加请求对象
     *
     * @param request
     */
    public void addRequest(BitmapRequest request) {
        //判断请求队列是否包含请求
        if (!mBlockingQueue.contains(request)) {
            //给请求进行编号
            request.setSerialNo(i.incrementAndGet());
            mBlockingQueue.add(request);
        } else {
            Log.i(TAG, "请求已经存在 编号：" + request.getSerialNo());
        }
    }

    /**
     * 开启请求
     */
    public void start() {
        //先停止
        stop();
        //开启转发器
        startDispatchers();
    }

    /**
     * 开启转发器
     */
    private void startDispatchers() {
        mDispachers = new RequestDispatcher[threadCount];
        for (int i = 0; i < threadCount; i++) {
            RequestDispatcher p = new RequestDispatcher(mBlockingQueue);
            mDispachers[i] = p;
            mDispachers[i].start();
        }
    }

    /**
     * 停止
     */
    public void stop() {

    }
}
