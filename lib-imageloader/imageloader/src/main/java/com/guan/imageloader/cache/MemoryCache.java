package com.guan.imageloader.cache;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.guan.imageloader.request.BitmapRequest;

/**
 * Created by Administrator on 2017/2/6 0006.
 */
public class MemoryCache implements BitmapCache {

    private LruCache<String, Bitmap> mLruCache;

    public MemoryCache() {
        //1.设置LruCache缓存的大小，手机可用内存的1/8
        int maxSize = (int) (Runtime.getRuntime().freeMemory() / 1024 / 8);
        //2.重写sizeOf方法，计算出要缓存的每张图片的大小。
        mLruCache = new LruCache<String, Bitmap>(maxSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };
    }

    @Override
    public void put(BitmapRequest request, Bitmap bitmap) {
        //3.put
        mLruCache.put(request.getImageUriMD5(), bitmap);
    }

    @Override
    public Bitmap get(BitmapRequest request) {
        //4.get
        return mLruCache.get(request.getImageUriMD5());
    }

    @Override
    public void remove(BitmapRequest request) {
        //5.remove
        mLruCache.remove(request.getImageUriMD5());
    }
}
