package org.fungo.share.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.umeng.socialize.UMShareAPI;

import org.fungo.share.bean.SHARE_PLATFROM;

/**
 * Created by chenliguan on 2017/8/23 0023.
 */
public class ShareAPI {

    private ShareAPI() {}

    public static ShareAPI get() {
        return ShareAPIHolder.instance;
    }

    private static class ShareAPIHolder {
        private static ShareAPI instance = new ShareAPI();
    }

    public static void init(Context context, String appkey) {
        UMShareAPI.init(context, appkey);
    }

    public boolean isInstall(Activity context, SHARE_PLATFROM platform) {
        return UMShareAPI.get(context).isInstall(context, SHARE_PLATFROM.toMedia(platform));
    }

    public boolean isAuthorize(Activity context, SHARE_PLATFROM platform) {
        return UMShareAPI.get(context).isAuthorize(context, SHARE_PLATFROM.toMedia(platform));
    }

    public boolean isSupport(Activity context, SHARE_PLATFROM platform) {
        return UMShareAPI.get(context).isSupport(context, SHARE_PLATFROM.toMedia(platform));
    }

    public String getversion(Activity context, SHARE_PLATFROM platform) {
        return UMShareAPI.get(context).getversion(context, SHARE_PLATFROM.toMedia(platform));
    }

    public void onActivityResult(Context context, int requestCode, int resultCode, Intent data) {
        UMShareAPI.get(context).onActivityResult(requestCode, resultCode, data);
    }

    public void release(Context context) {
        UMShareAPI.get(context).release();
    }

    public void onSaveInstanceState(Context context, Bundle bundle) {
        UMShareAPI.get(context).onSaveInstanceState(bundle);
    }
}
