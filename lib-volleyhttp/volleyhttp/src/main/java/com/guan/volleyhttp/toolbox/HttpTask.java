package com.guan.volleyhttp.toolbox;

import com.guan.volleyhttp.interfaces.IHttpListener;
import com.guan.volleyhttp.interfaces.IHttpService;

/**
 * Created by Administrator on 2017/1/13 0013.
 */
public class HttpTask<T> implements Runnable {

    private IHttpService IHttpService;

    public HttpTask(RequestHodler<T> requestHodler) {
        IHttpService = requestHodler.getHttpService();
        IHttpService.setHttpListener(requestHodler.getHttpListener());
        IHttpService.setUrl(requestHodler.getUrl());
        if (requestHodler.getMap() != null) {
            IHttpService.setParams(requestHodler.getMap());
        }
    }

    @Override
    public void run() {
        IHttpService.excute();
    }
}
