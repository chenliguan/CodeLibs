package org.guan.share.media;

import com.umeng.socialize.media.UMWeb;

/**
 * Created by chenliguan on 2017/8/24 0024.
 */
public class ShareWeb extends BaseShareBean {

    private UMWeb umWeb;

    public ShareWeb(String var1) {
        umWeb = new UMWeb(var1);
    }

    public ShareWeb(String var1, String var2, String var3, ShareImage var4) {
        umWeb = new UMWeb(var1, var2, var3, var4.getImage());
    }

    public UMWeb getWeb() {
        return umWeb;
    }

    @Override
    public void setThumb(ShareImage var1) {
        umWeb.setThumb(var1.getImage());
    }

    @Override
    public void setTitle(String var1) {
        umWeb.setTitle(var1);
    }

    @Override
    public void setDescription(String var1) {
        umWeb.setDescription(var1);
    }

    public void setmExtra(String var1, Object var2) {
        umWeb.setmExtra(var1,var2);
    }
}
