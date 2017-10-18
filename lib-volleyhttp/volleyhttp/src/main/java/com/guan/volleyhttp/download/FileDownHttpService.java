package com.guan.volleyhttp.download;

import android.util.Log;

import com.guan.volleyhttp.interfaces.IHttpListener;
import com.guan.volleyhttp.interfaces.IHttpService;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/16 0016.
 */
public class FileDownHttpService implements IHttpService {
    private static final String TAG = "tag";
    /**
     * 即将添加到请求头的信息
     */
    private Map<String, String> headerMap = Collections.synchronizedMap(new HashMap<String, String>());
    /**
     * 含有请求处理的接口
     */
    private IHttpListener httpListener;

    private HttpClient httpClient = new DefaultHttpClient();
    private HttpGet httpPost;
    private String url;

    /**
     * httpClient获取网络的回调
     */
    private HttpRespnceHandler httpRespnceHandler = new HttpRespnceHandler();

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public void excute() {
        httpPost = new HttpGet(url);
        constrcutHeader();
        try {
            httpClient.execute(httpPost, httpRespnceHandler);
        } catch (IOException e) {
            httpListener.onError("");
        }
    }

    private void constrcutHeader() {
        Iterator iterator = headerMap.keySet().iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            String value = headerMap.get(key);
            Log.i(TAG, " 请求头信息  " + key + "  value " + value);
            httpPost.addHeader(key, value);
        }
    }

    public Map<String, String> getHeaderMap() {
        return headerMap;
    }

    @Override
    public void setHttpListener(IHttpListener httpListener) {
        this.httpListener = httpListener;
    }

    @Override
    public void setParams(Map<String, String> map) {

    }

    @Override
    public void pause() {

    }

    @Override
    public boolean cancle() {
        return false;
    }

    @Override
    public boolean isCancle() {
        return false;
    }

    @Override
    public boolean isPause() {
        return false;
    }

    private class HttpRespnceHandler extends BasicResponseHandler {
        @Override
        public String handleResponse(HttpResponse response) throws ClientProtocolException {
            int code = response.getStatusLine().getStatusCode();
            if (code == 200) {
                httpListener.onSuccess(response.getEntity());
            } else {
                httpListener.onError("");
            }
            return null;
        }
    }
}
