package org.guan.share.media;

import com.umeng.socialize.media.UMMin;

/**
 * 微信小程序
 * Created by chenliguan on 2017/8/24 0024.
 */
public class ShareMin extends BaseShareBean {

    private UMMin umMin;

    public ShareMin(String s) {
        umMin = new UMMin(s);
    }

    public UMMin getMin() {
        return umMin;
    }

    @Override
    public void setTitle(String var1) {
        umMin.setTitle(var1);
    }

    @Override
    public void setThumb(ShareImage var1) {
        umMin.setThumb(var1.getImage());
    }

    @Override
    public void setDescription(String var1) {
        umMin.setDescription(var1);
    }

    public void setUserName(String var1) {
        umMin.setUserName(var1);
    }

    public void setPath(String var1) {
        umMin.setPath(var1);
    }
}
