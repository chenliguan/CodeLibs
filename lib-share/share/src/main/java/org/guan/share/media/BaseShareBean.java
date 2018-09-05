package org.guan.share.media;

/**
 * Created by chenliguan on 2017/10/20 0020.
 */
public abstract class BaseShareBean {

    /**
     * 设置标题
     * @param var1
     */
    public abstract void setTitle(String var1);

    /**
     * 设置分享Image
     * @param var1
     */
    public abstract void setThumb(ShareImage var1);

    /**
     * 设置描述
     * @param var1
     */
    public abstract void setDescription(String var1);
}
