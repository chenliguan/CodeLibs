package com.guan.imageloader.cache;

import android.graphics.Bitmap;

import com.guan.imageloader.request.BitmapRequest;

/**
 * Created by Administrator on 2017/2/6 0006.
 */
public interface BitmapCache {
    /**
     * 缓存bitmap
     * @param bitmap
     */
    void put(BitmapRequest request, Bitmap bitmap);

    /**
     * 通过请求取Bitmap
     * @param request
     * @return
     */
    Bitmap get(BitmapRequest request);

    /**
     * 移除缓存
     * @param request
     */
    void remove(BitmapRequest request);
}
