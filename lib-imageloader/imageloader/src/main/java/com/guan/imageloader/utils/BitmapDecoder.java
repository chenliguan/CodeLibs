package com.guan.imageloader.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * 图片压缩
 *
 * Created by Administrator on 2017/2/8 0008.
 */
public abstract class BitmapDecoder {

    /**
     * 图片采样率缩放的具体实现
     *
     * @param reqWidth 计算ImageView控件的宽
     * @param reqHeight 计算ImageView控件的高
     * @param ivCompressEnable 是否对图片压缩处理
     * @return
     */
    public Bitmap decodeBitmap(int reqWidth, int reqHeight, boolean ivCompressEnable) {
        //第一次解析：将inJustDecodeBounds设置为true，来获取图片大小
        BitmapFactory.Options options = new BitmapFactory.Options();

        if (ivCompressEnable) {
            //只需要读取图片宽高信息，无需将整张图片加载到内存。
            //禁止为bitmap分配内存，返回值也不再是一个Bitmap对象，而是null
            options.inJustDecodeBounds = true;
            //第一次读，在加载图片之前就获取到：图片的长宽值和MIME类型(根据options加载Bitmap),从而根据情况对图片进行压缩；
            //==BitmapFactory.decodeResource(res, resId, options)。
            decodeBitmapWithOption(options);

            //调用上面定义的方法计算inSampleSize值
            options.inSampleSize = calculateSampleSizeWithOption(options, reqWidth, reqHeight);
            //每个像素2个字节（默认值ARGB_8888改为RGB_565,节约一半内存）
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            //为bitmap分配内存---false
            options.inJustDecodeBounds = false;
            //当系统内存不足时可以回收Bitmap
            options.inPurgeable = true;
            //设置是否深拷贝
            options.inInputShareable = true;
        }


        //使用获取到的inSampleSize值再次解析图片
        //第二次解析：经过第一次读取现在图片的大小，就可以决定是把整张图片加载到内存中还是加载一个压缩版的图片到内存中
        return decodeBitmapWithOption(options);
    }

    /**
     * 计算图片缩放的比例
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     */
    private int calculateSampleSizeWithOption(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        //计算缩放的比例，图片的原始宽高
        int width = options.outWidth;
        int height = options.outHeight;

        int inSampleSize = 1;
        //reqWidth---ImageView的宽
        if (width > reqWidth || height > reqHeight) {
            //宽高的缩放比例（图片原始宽高/ImageView的宽高）
            int heightRatio = Math.round((float) height / (float) reqHeight);
            int widthRatio = Math.round((float) width / (float) reqWidth);

            //取最大值：有的图是长图、有的是宽图
            inSampleSize = Math.max(heightRatio, widthRatio);
        }

        //当inSampleSize为2，图片的宽与高变成原来的1/2，options.inSampleSize = 2
        return inSampleSize;
    }

    public abstract Bitmap decodeBitmapWithOption(BitmapFactory.Options options);

}
