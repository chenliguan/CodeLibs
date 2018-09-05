package org.guan.share.helper;

import android.app.Activity;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.ShareContent;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.guan.share.bean.SHARE_PLATFROM;
import org.guan.share.inteface.ShareHandlerCallback;
import org.guan.share.media.ShareEmoji;
import org.guan.share.media.ShareImage;
import org.guan.share.media.ShareMin;
import org.guan.share.media.ShareMusic;
import org.guan.share.media.ShareVideo;
import org.guan.share.media.ShareWeb;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * Created by chenliguan on 2017/8/24 0024.
 */
public class ShareController {

    private Activity activity;
    private ShareAction shareAction;
    private ShareContent shareContent = new ShareContent();
    private ShareHandlerCallback callback;

    public ShareController(Activity activity, SHARE_PLATFROM platform) {
        if(activity != null) {
            this.activity = (Activity)(new WeakReference(activity)).get();
        }
        shareAction = new ShareAction(this.activity);
        shareAction.setPlatform(SHARE_PLATFROM.toMedia(platform));
    }

    private UMShareListener listener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA shareMedia) {
            if (callback != null) {
                callback.startCall(shareMedia.toString());
            }
        }

        @Override
        public void onResult(SHARE_MEDIA shareMedia) {
            if (callback != null) {
                callback.successCall(shareMedia.toString());
            }
        }

        @Override
        public void onError(SHARE_MEDIA shareMedia, Throwable throwable) {
            if (callback != null) {
                callback.errorCall(shareMedia.toString(),throwable.toString());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA shareMedia) {
            if (callback != null) {
                callback.cancelCall(shareMedia.toString());
            }
        }
    };

    public void share() {
        UMShareAPI.get(activity).doShare(activity, shareAction, listener);
    }

    public ShareContent getShareContent() {
        return shareContent;
    }

    public boolean getUrlValid() {
        return shareContent == null || shareContent.mMedia == null || !(shareContent.mMedia instanceof ShareWeb) || shareContent.mMedia.toUrl().startsWith("http");
    }

    public SHARE_MEDIA getPlatform() {
        return shareAction.getPlatform();
    }

    public void setShareContent(ShareContent shareContent) {
        shareAction.setShareContent(shareContent);
    }

    public void withText(String text) {
        shareContent.mText = text;
        shareAction.setShareContent(shareContent);
    }

    public void withSubject(String subject) {
        shareContent.subject = subject;
        shareAction.setShareContent(shareContent);
    }

    public void withFile(File file) {
        shareContent.file = file;
        shareAction.setShareContent(shareContent);
    }

    public void withApp(File file) {
        shareContent.app = file;
        shareAction.setShareContent(shareContent);
    }

    public void withMedia(ShareImage image) {
        shareContent.mMedia = image.getImage();
        shareAction.setShareContent(shareContent);
    }

    public void withMedia(ShareMin umMin) {
        shareContent.mMedia = umMin.getMin();
        shareAction.setShareContent(shareContent);
    }

    public void withMedia(ShareEmoji image) {
        shareContent.mMedia = image.getEmoji();
        shareAction.setShareContent(shareContent);
    }

    public void withMedia(ShareWeb web) {
        shareContent.mMedia = web.getWeb();
        shareAction.setShareContent(shareContent);
    }

    public void withFollow(String follow) {
        shareContent.mFollow = follow;
        shareAction.setShareContent(shareContent);
    }

    public void withExtra(ShareImage mExtra) {
        shareContent.mExtra = mExtra.getImage();
        shareAction.setShareContent(shareContent);
    }

    public void withMedia(ShareMusic music) {
        shareContent.mMedia = music.getMusic();
        shareAction.setShareContent(shareContent);
    }

    public void withMedia(ShareVideo video) {
        shareContent.mMedia = video.getVideo();
        shareAction.setShareContent(shareContent);
    }

    public void setCallback(ShareHandlerCallback callback) {
        this.callback = callback;
    }
}
