package com.guan.imageloader.loader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.guan.imageloader.request.BitmapRequest;
import com.guan.imageloader.utils.BitmapDecoder;
import com.guan.imageloader.utils.ImageViewHelper;

import java.io.File;

/**
 * Created by Administrator on 2017/2/6 0006.
 */
public class LocalLoader extends AbstarctLoader {

    @Override
    protected Bitmap onLoad(BitmapRequest request, boolean ivCompressEnable) {
        //得到本地图片的路径
        final String path = Uri.parse(request.getImageUrl()).getPath();
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }

        BitmapDecoder decoder = new BitmapDecoder() {
            @Override
            public Bitmap decodeBitmapWithOption(BitmapFactory.Options options) {
                return BitmapFactory.decodeFile(path, options);
            }
        };

        return decoder.decodeBitmap(ImageViewHelper.getImageViewWidth(request.getImageView()), ImageViewHelper.getImageViewHeight(request.getImageView()), ivCompressEnable);
    }
}
