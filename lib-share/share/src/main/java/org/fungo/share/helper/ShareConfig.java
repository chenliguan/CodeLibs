package org.fungo.share.helper;

import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;

/**
 * Created by chenliguan on 2017/8/23 0023.
 */
public class ShareConfig {

    public static void isDebug(boolean debug) {
        Config.DEBUG = debug;
    }

    /**
     * String id, String key
     * @param content
     */
    public static void setQQZone(String... content) {
        PlatformConfig.setQQZone(content[0],content[1]);
    }

    public static void setTwitter(String... content) {
        PlatformConfig.setTwitter(content[0],content[1]);
    }

    public static void setAlipay(String... content) {
        PlatformConfig.setAlipay(content[0]);
    }

    public static void setDropbox(String... content) {
        PlatformConfig.setDropbox(content[0],content[1]);
    }

    public static void setDing(String... content) {
        PlatformConfig.setDing(content[0]);
    }

    public static void setSinaWeibo(String... content) {
        PlatformConfig.setSinaWeibo(content[0],content[1],content[2]);
    }

    public static void setVKontakte(String... content) {
        PlatformConfig.setVKontakte(content[0],content[1]);
    }

    public static void setWeixin(String... content) {
        PlatformConfig.setWeixin(content[0],content[1]);
    }

    public static void setLaiwang(String... content) {
        PlatformConfig.setLaiwang(content[0],content[1]);
    }

    public static void setYixin(String... content) {
        PlatformConfig.setYixin(content[0]);
    }

    public static void setPinterest(String... content) {
        PlatformConfig.setPinterest(content[0]);
    }

    public static void setKakao(String... content) {
        PlatformConfig.setKakao(content[0]);
    }

    public static void setYnote(String... content) {
        PlatformConfig.setYnote(content[0]);
    }

}
