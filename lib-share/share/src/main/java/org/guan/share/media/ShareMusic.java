package org.guan.share.media;

import com.umeng.socialize.media.UMusic;

/**
 * 音乐
 * Created by chenliguan on 2017/8/24 0024.
 */
public class ShareMusic extends BaseShareBean {

    private UMusic uMusic;

    public ShareMusic(String s) {
        uMusic = new UMusic(s);
    }

    public UMusic getMusic() {
        return uMusic;
    }

    @Override
    public void setTitle(String var1) {
        uMusic.setTitle(var1);
    }

    @Override
    public void setThumb(ShareImage var1) {
        uMusic.setThumb(var1.getImage());
    }

    @Override
    public void setDescription(String var1) {
        uMusic.setDescription(var1);
    }

    public void setmTargetUrl(String var1) {
        uMusic.setmTargetUrl(var1);
    }
}
