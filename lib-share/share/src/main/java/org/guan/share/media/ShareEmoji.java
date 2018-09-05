package org.guan.share.media;

import android.content.Context;
import android.graphics.Bitmap;

import com.umeng.socialize.media.UMEmoji;

import java.io.File;

/**
 * GIF
 * Created by chenliguan on 2017/8/24 0024.
 */
public class ShareEmoji extends BaseShareBean {

    private UMEmoji umEmoji;

    public ShareEmoji(Context context, File file) {
        umEmoji = new UMEmoji(context, file);
    }

    public ShareEmoji(Context context, String s) {
        umEmoji = new UMEmoji(context, s);
    }

    public ShareEmoji(Context context, int i) {
        umEmoji = new UMEmoji(context, i);
    }

    public ShareEmoji(Context context, byte[] bytes) {
        umEmoji = new UMEmoji(context, bytes);
    }

    public ShareEmoji(Context context, Bitmap bitmap) {
        umEmoji = new UMEmoji(context, bitmap);
    }

    public ShareEmoji(Context context, Bitmap bitmap, ShareImageMark imageMark) {
        umEmoji = new UMEmoji(context, bitmap, imageMark.getImageMark());
    }

    public ShareEmoji(Context context, int i, ShareImageMark imageMark) {
        umEmoji = new UMEmoji(context, i, imageMark.getImageMark());
    }

    public ShareEmoji(Context context, byte[] bytes, ShareImageMark imageMark) {
        umEmoji = new UMEmoji(context, bytes, imageMark.getImageMark());
    }

    public UMEmoji getEmoji() {
        return umEmoji;
    }

    @Override
    public void setTitle(String var1) {
        umEmoji.setTitle(var1);
    }

    @Override
    public void setThumb(ShareImage var1) {
        umEmoji.setThumb(var1.getImage());
    }

    @Override
    public void setDescription(String var1) {
        umEmoji.setDescription(var1);
    }
}
