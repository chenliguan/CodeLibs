package org.guan.share.media;

import com.umeng.socialize.media.UMVideo;

/**
 * Created by chenliguan on 2017/8/24 0024.
 */
public class ShareVideo extends BaseShareBean {

    private UMVideo umVideo;

    public ShareVideo(String s) {
        umVideo = new UMVideo(s);
    }

    public UMVideo getVideo() {
        return umVideo;
    }

    @Override
    public void setTitle(String var1) {
        umVideo.setTitle(var1);
    }

    @Override
    public void setThumb(ShareImage var1) {
        umVideo.setThumb(var1.getImage());
    }

    @Override
    public void setDescription(String var1) {
        umVideo.setDescription(var1);
    }
}
