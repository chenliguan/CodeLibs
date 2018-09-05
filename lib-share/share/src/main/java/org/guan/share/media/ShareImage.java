package org.guan.share.media;

import android.content.Context;
import android.graphics.Bitmap;

import com.umeng.socialize.media.UMImage;

import java.io.File;

/**
 * Created by chenliguan on 2017/8/24 0024.
 */
public class ShareImage extends BaseShareBean {

    private UMImage umImage;
    public UMImage.CompressStyle compressStyle;

    public ShareImage(Context context, File file) {
        umImage = new UMImage(context, file);
    }

    public ShareImage(Context context, String s) {
        umImage = new UMImage(context, s);
    }

    public ShareImage(Context context, int i) {
        umImage = new UMImage(context, i);
    }

    public ShareImage(Context context, byte[] bytes) {
        umImage = new UMImage(context, bytes);
    }

    public ShareImage(Context context, Bitmap bitmap) {
        umImage = new UMImage(context, bitmap);
    }

    public ShareImage(Context context, Bitmap bitmap, ShareImageMark imageMark) {
        umImage = new UMImage(context, bitmap, imageMark.getImageMark());
    }

    public ShareImage(Context context, int i, ShareImageMark imageMark) {
        umImage = new UMImage(context, i, imageMark.getImageMark());
    }

    public ShareImage(Context context, byte[] bytes, ShareImageMark imageMark) {
        umImage = new UMImage(context, bytes, imageMark.getImageMark());
    }

    public UMImage getImage() {
        return umImage;
    }

    @Override
    public void setTitle(String var1) {
        umImage.setTitle(var1);
    }

    @Override
    public void setThumb(ShareImage var1) {
        umImage.setThumb(var1.getImage());
    }

    @Override
    public void setDescription(String var1) {
        umImage.setDescription(var1);
    }

    public void setCompressStyle(CompressStyle var1) {
        umImage.compressStyle = ShareImage.CompressStyle.toMedia(var1);
    }

    public static enum CompressStyle {
        SCALE,
        QUALITY;

        private CompressStyle() {
        }

        public static UMImage.CompressStyle toMedia(CompressStyle style) {
            if (style.toString().equals("SCALE")) {
                return UMImage.CompressStyle.SCALE;
            } else if (style.toString().equals("QUALITY")) {
                return UMImage.CompressStyle.QUALITY;
            } else {
                return null;
            }
        }

        public static CompressStyle toStyle(UMImage.CompressStyle style) {
            if (style.toString().equals("SCALE")) {
                return CompressStyle.SCALE;
            } else if (style.toString().equals("QUALITY")) {
                return CompressStyle.QUALITY;
            } else {
                return null;
            }
        }
    }
}
