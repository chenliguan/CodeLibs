package org.guan.share.inteface;

/**
 * 分享回调接口
 * Created by chenliguan on 2017/8/24 0024.
 */
public interface ShareHandlerCallback {

    /**
     * 分享开始的回调
     * @param var1 平台类型
     */
    void startCall(String var1);

    /**
     * 分享成功的回调
     * @param var1 平台类型
     */
    void successCall(String var1);

    /**
     * 分享失败的回调
     * @param var1 平台类型
     * @param var2 错误原因
     */
    void errorCall(String var1,String var2);

    /**
     * 分享取消的回调
     * @param var1 平台类型
     */
    void cancelCall(String var1);
}



