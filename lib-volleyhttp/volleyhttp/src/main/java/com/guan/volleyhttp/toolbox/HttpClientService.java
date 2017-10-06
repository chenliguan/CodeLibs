package com.guan.volleyhttp.toolbox;

import com.guan.volleyhttp.interfaces.IHttpListener;
import com.guan.volleyhttp.interfaces.IHttpService;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by Administrator on 2017/1/13 0013.
 */
public class HttpClientService implements IHttpService {

    private HttpClient httpClient = new DefaultHttpClient();
    private HttpPost httpPost;
    private IHttpListener httpListener;// 持有

    private String url;
    private Map<String, String> map;
    // httpClient获取网络的回调
    private HttpRespnceHandler httpRespnceHandler = new HttpRespnceHandler();

    private class HttpRespnceHandler extends BasicResponseHandler {
        @Override
        public String handleResponse(HttpResponse response) throws ClientProtocolException {
            // 响应码
            int code = response.getStatusLine().getStatusCode();
            if (code == 200) {
                httpListener.onSuccess(response.getEntity());
            } else {
                httpListener.onError("响应码非200错误");
            }
            return null;
        }
    }

    @Override
    public void excute() {
        httpPost = new HttpPost(url);
        httpPost.setEntity(getEntity(map));
        try {
            httpClient.execute(httpPost, httpRespnceHandler);
        } catch (IOException e) {
            httpListener.onError(e.toString());
        }
    }

    private UrlEncodedFormEntity getEntity(Map<String, String> map){
        List<NameValuePair> list = new ArrayList<>();
        for (Map.Entry<String, String> elem : map.entrySet()) {
            list.add(new BasicNameValuePair(elem.getKey(), elem.getValue()));
        }
        if (list.size() > 0) {
            UrlEncodedFormEntity entity = null;
            try {
                entity = new UrlEncodedFormEntity(list, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return entity;
        }
        return null;
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public void setHttpListener(IHttpListener httpListener) {
        this.httpListener = httpListener;
    }

    @Override
    public void setParams(Map<String, String> map) {
        this.map = map;
    }
}
