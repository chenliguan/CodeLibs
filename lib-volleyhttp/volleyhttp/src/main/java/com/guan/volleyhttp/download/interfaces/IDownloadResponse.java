package com.guan.volleyhttp.download.interfaces;

import com.guan.volleyhttp.download.DownloadItemInfo;

/**
 * Created by david on 2017/1/12.
 */
public interface IDownloadResponse {
    void onDownloadStatusChanged(DownloadItemInfo downloadItemInfo);

    void onTotalLengthReceived(DownloadItemInfo downloadItemInfo);

    void onCurrentSizeChanged(DownloadItemInfo downloadItemInfo, double downLenth, long speed);

    void onDownloadSuccess(DownloadItemInfo downloadItemInfo);

    void onDownloadPause(DownloadItemInfo downloadItemInfo);

    void onDownloadError(DownloadItemInfo downloadItemInfo, int var2, String var3);
}
