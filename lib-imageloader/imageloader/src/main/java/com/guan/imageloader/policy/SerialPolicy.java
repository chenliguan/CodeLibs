package com.guan.imageloader.policy;

import com.guan.imageloader.request.BitmapRequest;

/**
 * 顺序加载策略
 * Created by Administrator on 2017/2/6 0006.
 */
public class SerialPolicy implements LoadPolicy {

    @Override
    public int compareto(BitmapRequest request1, BitmapRequest request2) {
        return request1.getSerialNo() - request2.getSerialNo();
    }
}
