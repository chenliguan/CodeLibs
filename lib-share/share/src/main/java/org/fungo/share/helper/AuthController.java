package org.fungo.share.helper;

import android.app.Activity;
import android.os.Bundle;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.fungo.share.bean.SHARE_PLATFROM;
import org.fungo.share.inteface.AuthHandlerCallback;

import java.lang.ref.WeakReference;
import java.util.Map;

/**
 * Created by chenliguan on 2017/8/24 0024.
 */
public class AuthController {

    private Activity activity;
    private SHARE_PLATFROM platform;
    private Bundle bundle;
    private AuthHandlerCallback callback;

    public AuthController(Activity activity, SHARE_PLATFROM platform) {
        setParams(activity, platform);
    }

    public AuthController(Activity activity, SHARE_PLATFROM platform, AuthHandlerCallback callback) {
        setParams(activity, platform);
        this.callback = callback;
    }

    public AuthController(Activity activity, SHARE_PLATFROM platform, Bundle bundle, AuthHandlerCallback callback) {
        setParams(activity, platform);
        this.bundle = bundle;
        this.callback = callback;
    }

    private void setParams(Activity activity, SHARE_PLATFROM platform) {
        if (activity != null) {
            this.activity = (Activity) (new WeakReference(activity)).get();
        }
        this.platform = platform;
    }

    private UMAuthListener listener = new UMAuthListener() {

        @Override
        public void onStart(SHARE_MEDIA shareMedia) {
            if (callback != null) {
                callback.startCall(SHARE_PLATFROM.toPlatfrom(shareMedia));
            }
        }

        @Override
        public void onComplete(SHARE_MEDIA shareMedia, int i, Map<String, String> map) {
            if (callback != null) {
                callback.successCall(SHARE_PLATFROM.toPlatfrom(shareMedia), i, map);
            }
        }

        @Override
        public void onError(SHARE_MEDIA shareMedia, int i, Throwable throwable) {
            if (callback != null) {
                callback.errorCall(SHARE_PLATFROM.toPlatfrom(shareMedia), i, throwable);
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA shareMedia, int i) {
            if (callback != null) {
                callback.cancelCall(SHARE_PLATFROM.toPlatfrom(shareMedia), i);
            }
        }
    };

    public void doOauthVerify() {
        UMShareAPI.get(activity).doOauthVerify(activity, SHARE_PLATFROM.toMedia(platform), listener);
    }

    public void deleteOauth() {
        UMShareAPI.get(activity).doOauthVerify(activity, SHARE_PLATFROM.toMedia(platform), listener);
    }

    public void getPlatformInfo() {
        UMShareAPI.get(activity).doOauthVerify(activity, SHARE_PLATFROM.toMedia(platform), listener);
    }

    public void fetchAuthResultWithBundle() {
        UMShareAPI.get(activity).fetchAuthResultWithBundle(activity, bundle, listener);
    }
}
