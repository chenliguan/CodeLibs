package com.guan.imageloader.policy;

import com.guan.imageloader.request.BitmapRequest;

/**
 * Created by Administrator on 2017/2/6 0006.
 * 加载策略
 */
public interface LoadPolicy {
    /**
     * 两个BItmapRequest进行优先级比较
     * @param request1
     * @param request2
     * @return
     */
    int compareto(BitmapRequest request1, BitmapRequest request2);
}
