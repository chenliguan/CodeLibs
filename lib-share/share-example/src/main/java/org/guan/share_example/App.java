package org.guan.share_example;

import android.app.Application;

import com.umeng.socialize.BuildConfig;

import org.guan.share.helper.ShareConfig;

/**
 * Created by chenliguan on 2017/8/24 0024.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        initUmengShare();
    }

    public void initUmengShare() {
        ShareConfig.isDebug(BuildConfig.DEBUG);

        ShareConfig.setWeixin("APP_ID", "SECRET");
        ShareConfig.setSinaWeibo("APP_ID", "SECRET","http://sns.whalecloud.com");
        ShareConfig.setQQZone("APP_ID", "SECRET");
    }
}