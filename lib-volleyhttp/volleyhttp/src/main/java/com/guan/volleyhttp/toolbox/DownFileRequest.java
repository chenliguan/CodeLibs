package com.guan.volleyhttp.toolbox;

import android.os.Environment;
import android.util.Log;

import com.guan.volleyhttp.download.DownLoadLitener;
import com.guan.volleyhttp.download.DownloadItemInfo;
import com.guan.volleyhttp.download.FileDownHttpService;
import com.guan.volleyhttp.download.interfaces.IDownloadResponse;
import com.guan.volleyhttp.interfaces.IHttpListener;
import com.guan.volleyhttp.interfaces.IHttpService;

import java.io.File;
import java.util.Map;
import java.util.concurrent.FutureTask;

/**
 * Created by Administrator on 2017/1/16 0016.
 */
public class DownFileRequest {
    private static final String TAG = "dongnao";
    private byte[] lock = new byte[0];

    public static void downRequest(String url, IDownloadResponse downloadResponse) {
        down(url, null, downloadResponse);
    }

    public static void downRequest(String url, Map<String, String> map, IDownloadResponse downloadResponse) {
        down(url, map, downloadResponse);
    }

    /**
     * 下载
     *
     * @param url
     */
    private static void down(String url, Map<String, String> map, IDownloadResponse downloadResponse) {
        String[] preFixs = url.split("/");
        String afterFix = preFixs[preFixs.length - 1];

        File file = new File(Environment.getExternalStorageDirectory(), afterFix);
        //实例化DownloadItem
        DownloadItemInfo downloadItemInfo = new DownloadItemInfo(url, file.getAbsolutePath());

        RequestHodler requestHodler = new RequestHodler();
        //设置请求下载的策略
        IHttpService httpService = new FileDownHttpService();
        //处理结果的策略
        IHttpListener httpListener = new DownLoadLitener(downloadItemInfo, downloadResponse, httpService);

        requestHodler.setUrl(url);
        requestHodler.setHttpListener(httpListener);
        requestHodler.setHttpService(httpService);

        HttpTask httpTask = new HttpTask(requestHodler);
        try {
            ThreadPoolManager.getInstance().execte(new FutureTask<Object>(httpTask, null));
        } catch (InterruptedException e) {

        }
    }

}
