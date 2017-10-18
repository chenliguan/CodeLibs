package com.guan.volleyhttp.toolbox;

import com.guan.volleyhttp.interfaces.IHttpListener;
import com.guan.volleyhttp.interfaces.IHttpService;

/**
 * Created by Administrator on 2017/1/13 0013.
 */
public class HttpTask<T> implements Runnable {

    private IHttpService httpService;

    public HttpTask(RequestHodler<T> requestHodler) {
        httpService = requestHodler.getHttpService();
        httpService.setHttpListener(requestHodler.getHttpListener());
        httpService.setUrl(requestHodler.getUrl());
        //增加方法
        IHttpListener httpListener = requestHodler.getHttpListener();
        if (requestHodler.getMap() != null) {
            httpService.setParams(requestHodler.getMap());
            httpListener.addHttpHeader(requestHodler.getMap());
        }
    }

    @Override
    public void run() {
        httpService.excute();
    }
}
