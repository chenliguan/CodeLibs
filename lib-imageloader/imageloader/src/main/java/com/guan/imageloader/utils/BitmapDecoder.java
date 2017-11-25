package com.guan.imageloader.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Bitmap 解码
 * Created by Administrator on 2017/2/8 0008.
 */
public abstract class BitmapDecoder {

    public Bitmap decodeBitmap(int reqWidth, int reqHeight) {
        //初始化options
        BitmapFactory.Options options = new BitmapFactory.Options();
        //只需要读取图片宽高信息，无需将整张图片加载到内存，inJustDecodeBounds设置true
        options.inJustDecodeBounds = true;
        //第一次读图片宽高信息：根据options加载Bitmap，抽象（inJustDecodeBounds = true）
        decodeBitmapWithOption(options);
        //计算图片缩放比例
        calculateSampleSizeWithOption(options, reqWidth, reqHeight);

        //第二次读整个图片信息：（inJustDecodeBounds = false）
        return decodeBitmapWithOption(options);
    }

    /**
     * 计算图片缩放的比例
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     */
    private void calculateSampleSizeWithOption(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        //计算缩放的比例
        //图片的原始宽高
        int width = options.outWidth;
        int height = options.outHeight;

        int inSampleSize = 1;
        //reqWidth---ImageView的宽
        if (width > reqWidth || height > reqHeight) {
            //宽高的缩放比例
            int heightRatio = Math.round((float) height / (float) reqHeight);
            int widthRatio = Math.round((float) width / (float) reqWidth);

            //有的图是长图、有的是宽图
            inSampleSize = Math.max(heightRatio, widthRatio);
        }

        //全景图
        //当inSampleSize为2，图片的宽与高变成原来的1/2
        //options.inSampleSize = 2
        options.inSampleSize = inSampleSize;

        //每个像素2个字节（默认值ARGB_8888改为RGB_565,节约一半内存）
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        //Bitmap占用内存---false
        options.inJustDecodeBounds = false;
        //当系统内存不足时可以回收Bitmap
        options.inPurgeable = true;
        //设置是否深拷贝
        options.inInputShareable = true;
    }

    public abstract Bitmap decodeBitmapWithOption(BitmapFactory.Options options);

}
