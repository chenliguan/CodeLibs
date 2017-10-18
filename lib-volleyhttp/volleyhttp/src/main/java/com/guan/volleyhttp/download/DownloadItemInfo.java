package com.guan.volleyhttp.download;

import com.guan.volleyhttp.toolbox.HttpTask;

/**
 * Created by Administrator on 2017/1/16 0016.
 */
public class DownloadItemInfo extends BaseEntity<DownloadItemInfo> {

    public DownloadItemInfo() {
    }

    public DownloadItemInfo(String url, String filePath) {
        this.url = url;
        this.filePath = filePath;
    }

    private long currentLength;

    private long totalLength;

    private String url;

    private String filePath;

    private transient HttpTask httpTask;
    // 下载的状态
    private DownloadStatus status;



    public long getCurrentLength() {
        return currentLength;
    }

    public void setCurrentLength(long currentLength) {
        this.currentLength = currentLength;
    }

    public long getTotalLength() {
        return totalLength;
    }

    public void setTotalLength(long totalLength) {
        this.totalLength = totalLength;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public HttpTask getHttpTask() {
        return httpTask;
    }

    public void setHttpTask(HttpTask httpTask) {
        this.httpTask = httpTask;
    }

    public DownloadStatus getStatus() {
        return status;
    }

    public void setStatus(DownloadStatus status) {
        this.status = status;
    }
}
