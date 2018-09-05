package org.guan.share.media;

import android.graphics.Bitmap;

import com.umeng.social.tool.UMImageMark;

/**
 * 水印
 * Created by chenliguan on 2017/8/24 0024.
 */
public class ShareImageMark {

    private UMImageMark umImageMark;

    private ShareImageMark() {
        umImageMark = new UMImageMark();
    }

    public UMImageMark getImageMark() {
        return umImageMark;
    }

    public void setGravity(int gravity) {
        umImageMark.setGravity(gravity);
    }

    public void setMarkBitmap(Bitmap markBitmap) {
        umImageMark.setMarkBitmap(markBitmap);
    }

    /**
     * 设置透明度
     * @param alpha
     */
    public void setAlpha(float alpha) {
        umImageMark.setAlpha(alpha);
    }

    /**
     * 设置边距
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public void setMargins(int left, int top, int right, int bottom) {
        umImageMark.setMargins(left, top, right, bottom);
    }

    public void setScale(float scale) {
        umImageMark.setScale(scale);
    }

    public void setRotate(int degree) {
        umImageMark.setRotate(degree);
    }

    public void setTransparent() {
        umImageMark.setTransparent();
    }

    public Bitmap compound(Bitmap src) {
        return umImageMark.compound(src);
    }

    public void bringToFront() {
        umImageMark.bringToFront();
    }

}
