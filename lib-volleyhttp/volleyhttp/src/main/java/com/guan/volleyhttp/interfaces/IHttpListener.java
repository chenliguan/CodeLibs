package com.guan.volleyhttp.interfaces;

import org.apache.http.HttpEntity;

/**
 * 重要3个接口之一：网络处理回调
 * Created by Administrator on 2017/1/13 0013.
 */
public interface IHttpListener {
    /**
     * 网络访问
     * 处理结果  回调
     * @param httpEntity
     */
    void onSuccess(HttpEntity httpEntity);

    void onError(String error);
}
