package com.guan.volleyhttp.download.interfaces;

import com.guan.volleyhttp.interfaces.IHttpListener;
import com.guan.volleyhttp.interfaces.IHttpService;

/**
 * Created by Administrator on 2017/1/16 0016.
 */

public interface IDownLitener extends IHttpListener {

    void setHttpServive(IHttpService httpServive);


    void setCancleCalle();


    void setPuaseCallble();
}
