package com.guan.volleyhttp.toolbox;

import android.os.Handler;
import android.os.Looper;

import com.guan.volleyhttp.interfaces.IHttpListener;
import com.guan.volleyhttp.interfaces.Response;

import org.apache.http.HttpEntity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Administrator on 2017/1/13 0013.
 * M  对应响应类
 */
public class JsonDealLitener<M> implements IHttpListener {

    /**
     * 回调调用层的接口
     */
    private Response.Listener<M> listener;
    private Response.ErrorListener errorListener;
    /**
     * 通过handle切换至主线程获取主线程的Handler
     */
    private Handler handler = new Handler(Looper.getMainLooper());

    JsonDealLitener(Response.Listener<M> listener, Response.ErrorListener errorListener) {
        this.listener = listener;
        this.errorListener = errorListener;
    }

    @Override
    public void onSuccess(HttpEntity httpEntity) {
        InputStream inputStream = null;
        try {
            inputStream = httpEntity.getContent();
            // 得到网络返回的数据---子线程
            final String content = getContent(inputStream);
//            final M m = JSON.parseObject(content, responese);

            // 切换至主线程获取主线程
            handler.post(new Runnable() {
                @Override
                public void run() {
                    listener.onSuccess((M) content);
                }
            });

        } catch (IOException e) {
            errorListener.onError(e.toString());
        }

    }

    @Override
    public void onError(String error) {
        errorListener.onError(error);
    }

    private String getContent(InputStream inputStream) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line = null;

            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
            } catch (IOException e) {
                errorListener.onError(e.toString());
                System.out.println("Error=" + e.toString());
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    System.out.println("Error=" + e.toString());
                }
            }
            return sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
            errorListener.onError(e.toString());
        }
        return null;
    }
}
