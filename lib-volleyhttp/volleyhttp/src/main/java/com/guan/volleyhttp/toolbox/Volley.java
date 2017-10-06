package com.guan.volleyhttp.toolbox;

import com.guan.volleyhttp.interfaces.IHttpListener;
import com.guan.volleyhttp.interfaces.IHttpService;
import com.guan.volleyhttp.interfaces.Response;

import java.util.Map;
import java.util.concurrent.FutureTask;

/**
 * Created by Administrator on 2017/1/13 0013.
 */

public class Volley {

    /**
     * Creates a new GET request.
     */
    public static <T, M> void sendRequest(int method, String url, Response.Listener<M> listener, Response.ErrorListener errorListener) {
        RequestHodler<T> requestHodler = new RequestHodler<>();
        sendRequest(url, listener,errorListener,requestHodler);
    }

    /**
     * Creates a new POST request.
     */
    public static <T, M> void sendRequest(Map<String,String> map, String url, Response.Listener<M> listener, Response.ErrorListener errorListener) {
        RequestHodler<T> requestHodler = new RequestHodler<>();
        requestHodler.setMap(map);
        sendRequest(url, listener,errorListener,requestHodler);
    }

    /**
     * Creates a request.
     */
    public static <T, M> void sendRequest(String url, Response.Listener<M> listener, Response.ErrorListener errorListener,RequestHodler<T> requestHodler) {
        IHttpService httpService = new HttpClientService();
        IHttpListener httpListener = new JsonDealLitener<>(listener, errorListener);

        requestHodler.setUrl(url);
        requestHodler.setHttpService(httpService);
        requestHodler.setHttpListener(httpListener);

        HttpTask<T> httpTask = new HttpTask<>(requestHodler);
        try {
            ThreadPoolManager.getInstance().execte(new FutureTask<>(httpTask, null));
        } catch (InterruptedException e) {
            errorListener.onError(e.toString());
        }
    }

}
