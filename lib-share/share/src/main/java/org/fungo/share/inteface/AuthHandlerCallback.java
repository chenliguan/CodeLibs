package org.fungo.share.inteface;

import org.fungo.share.bean.SHARE_PLATFROM;

import java.util.Map;

/**
 * 授权回调接口
 * Created by chenliguan on 2017/8/24 0024.
 */
public interface AuthHandlerCallback {

    /**
     * 授权开始的回调
     * @param var1 平台类型
     */
    void startCall(SHARE_PLATFROM var1);

    /**
     * 授权成功的回调
     * @param var1 平台类型
     * @param var2 行为序号，开发者用不上
     * @param var3 错误原因
     */
    void successCall(SHARE_PLATFROM var1, int var2, Map<String, String> var3);

    /**
     * 授权失败的回调
     * @param var1 平台类型
     * @param var2 错误原因
     */
    void errorCall(SHARE_PLATFROM var1, int var2, Throwable var3);

    /**
     * 取消授权的回调
     * @param var1 平台类型
     * @param var2 行为序号，开发者用不上
     */
    void cancelCall(SHARE_PLATFROM var1, int var2);
}



