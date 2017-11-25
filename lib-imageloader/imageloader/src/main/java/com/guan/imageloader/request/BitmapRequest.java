package com.guan.imageloader.request;

import android.widget.ImageView;
import com.guan.imageloader.config.DisplayConfig;
import com.guan.imageloader.core.SimpleImageLoader;
import com.guan.imageloader.policy.LoadPolicy;
import com.guan.imageloader.utils.MD5Utils;

import java.lang.ref.SoftReference;

/**
 * Bitmap请求实体类
 *
 * Created by Administrator on 2017/2/6 0006.
 */
public class BitmapRequest implements Comparable<BitmapRequest> {

    //持有imageview的软引用
    private SoftReference<ImageView> imageViewSoft;
    //图片路径
    private String imageUrl;
    //MD5的图片路径
    private String imageUriMD5;
    //编号
    private int serialNo;
    //下载完成监听
    public SimpleImageLoader.ImageListener imageListener;
    //显示图片的配置
    private DisplayConfig displayConfig;
    //加载策略
    private LoadPolicy loadPolicy = SimpleImageLoader.getInstance().getConfig().getLoadPolicy();

    public BitmapRequest(ImageView imageView, String imageUrl, DisplayConfig displayConfig,
                         SimpleImageLoader.ImageListener imageListener) {
        this.imageViewSoft = new SoftReference<>(imageView);
        //设置可见的Image的Tag，要下载的图片路径
        imageView.setTag(imageUrl);
        this.imageUrl = imageUrl;
        this.imageUriMD5 = MD5Utils.toMD5(imageUrl);
        if (displayConfig != null) {
            this.displayConfig = displayConfig;
        }
        this.imageListener = imageListener;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BitmapRequest that = (BitmapRequest) o;

        return serialNo == that.serialNo && (loadPolicy != null ?
                loadPolicy.equals(that.loadPolicy) : that.loadPolicy == null);
    }

    @Override
    public int hashCode() {
        int result = loadPolicy != null ? loadPolicy.hashCode() : 0;
        result = 31 * result + serialNo;
        return result;
    }

    public ImageView getImageView() {
        return imageViewSoft.get();
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getImageUriMD5() {
        return imageUriMD5;
    }

    public DisplayConfig getDisplayConfig() {
        return displayConfig;
    }

    public LoadPolicy getLoadPolicy() {
        return loadPolicy;
    }

    /**
     * 请求队列使用的是优先级队列PriorityBlockingQueue，因此我们的BitmapRequest都实现了 Comparable 接口，
     * 我们在BitmapRequest的函数中将compareTo委托给LoadPolicy对象的compare。
     * @param o
     * @return
     */
    @Override
    public int compareTo(BitmapRequest o) {
        return loadPolicy.compareto(o, this);
    }

    public int getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(int serialNo) {
        this.serialNo = serialNo;
    }
}
