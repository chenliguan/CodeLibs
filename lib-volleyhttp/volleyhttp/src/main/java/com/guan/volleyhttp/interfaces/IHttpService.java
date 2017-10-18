package com.guan.volleyhttp.interfaces;

import java.util.Map;

/**
 * 重要3个接口之一：具体请求数据
 * Created by Administrator on 2017/1/13 0013.
 */
public interface IHttpService {
    /**
     * 设置url
     * @param url
     */
    void setUrl(String url);

    /**
     * 执行获取网络
     */
    void excute();

    /**
     * 设置处理接口
     * @param httpListener
     */
    void setHttpListener(IHttpListener httpListener);

    /**
     * 设置请求参数
     */
    void setParams(Map<String, String> map);

    void pause();

    boolean cancle();

    boolean isCancle();

    boolean isPause();
}
