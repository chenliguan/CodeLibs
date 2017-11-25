package com.guan.imageloader.core;

import android.util.Log;

import com.guan.imageloader.loader.Loader;
import com.guan.imageloader.loader.LoaderManager;
import com.guan.imageloader.request.BitmapRequest;

import java.util.concurrent.BlockingQueue;

import static android.content.ContentValues.TAG;

/**
 * 转发器---请求转发线程
 * 不断从请求队列中获取请求
 *
 * Created by Administrator on 2017/2/6 0006.
 */
public class RequestDispatcher extends Thread {

    //请求队列
    private BlockingQueue<BitmapRequest> mBlockingQueue;

    public RequestDispatcher(BlockingQueue<BitmapRequest> blockingQueue) {
        this.mBlockingQueue = blockingQueue;
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                //阻塞式函数
                BitmapRequest request = mBlockingQueue.take();
                //处理请求对象
                String schema = parseSchema(request.getImageUrl());
                //获取加载器
                Loader loader = LoaderManager.getInstance().getLoader(schema);
                //加载图片
                loader.loadImage(request);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private String parseSchema(String imageUrl) {
        if (imageUrl.contains("://")) {
            return imageUrl.split("://")[0];
        } else {
            Log.i(TAG, "不支持此类型");
        }

        return null;
    }
}
