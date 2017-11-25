package com.guan.imageloader.loader;

import com.guan.imageloader.request.BitmapRequest;

/**
 * Created by Administrator on 2017/2/6 0006.
 */
public interface Loader {
    /**
     * 加载图片
     * @param request
     */
    void loadImage(BitmapRequest request);
}
