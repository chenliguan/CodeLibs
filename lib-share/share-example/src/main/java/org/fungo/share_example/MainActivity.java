package org.fungo.share_example;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import org.fungo.share.bean.SHARE_PLATFROM;
import org.fungo.share.helper.ShareAPI;
import org.fungo.share.helper.ShareController;
import org.fungo.share.inteface.ShareHandlerCallback;
import org.fungo.share.media.ShareImage;
import org.fungo.share.media.ShareWeb;

import java.io.File;

public class MainActivity extends Activity {

    private Dialog shareHDailog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.share);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showShareDailog(R.layout.daily_share_dialog);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ShareAPI.get().onActivityResult(this,requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareAPI.get().release(this);
    }

    private void showShareDailog(int layout) {
        LayoutInflater inflate = LayoutInflater.from(this);
        View dialogView = inflate.inflate(layout, null, false);

        View dialogShareCircle = dialogView.findViewById(R.id.live_share_circle);
        View dialogShareWeixin = dialogView.findViewById(R.id.live_share_weixin);
        View dialogShareQQ = dialogView.findViewById(R.id.live_share_qq);
        View dialogShareQzone = dialogView.findViewById(R.id.live_share_qzone);

        dialogShareCircle.setOnClickListener(shareClickListener);
        dialogShareWeixin.setOnClickListener(shareClickListener);
        dialogShareQQ.setOnClickListener(shareClickListener);
        dialogShareQzone.setOnClickListener(shareClickListener);

        if (shareHDailog == null) {
            shareHDailog = new Dialog(this, R.style.dialog_theme_transparent);
            // dialog默认都是有title的
            shareHDailog.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题，否则会影响高度计算，一定要在setContentView之前调用，终于明白有一个设置theme的构造函数的目的了
            shareHDailog.setContentView(dialogView);
            Window win = shareHDailog.getWindow();
            win.getDecorView().setPadding(0, 0, 0, 0);
            WindowManager.LayoutParams lp = win.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            win.setAttributes(lp);
            win.setGravity(Gravity.BOTTOM);
        }
        shareHDailog.show();
    }

    private View.OnClickListener shareClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.live_share_circle:
                    normalShare(SHARE_PLATFROM.WEIXIN_CIRCLE);
                    break;
                case R.id.live_share_weixin:
                    normalShare(SHARE_PLATFROM.WEIXIN);
                    break;
                case R.id.live_share_qq:
                    normalShare(SHARE_PLATFROM.QQ);
                    break;
                case R.id.live_share_qzone:
                    normalShare(SHARE_PLATFROM.QZONE);
                    break;
            }
        }
    };

    public void share() {
        ShareHandlerCallback callback = new ShareHandlerCallback() {//回调监听器
            /**
             * 分享开始的回调
             * @param var1 平台类型
             */
            @Override
            public void startCall(String var1) {

            }

            /**
             * 分享成功的回调
             * @param var1 平台类型
             */
            @Override
            public void successCall(String var1) {
                Toast.makeText(MainActivity.this, "分享成功" + var1, Toast.LENGTH_SHORT).show();
            }

            /**
             * 分享失败的回调
             * @param var1 平台类型
             * @param var2 错误原因
             */
            @Override
            public void errorCall(String var1, String var2) {
                Toast.makeText(MainActivity.this, "分享失败" + var1 + var2, Toast.LENGTH_SHORT).show();
            }

            /**
             * 分享取消的回调
             * @param var1 平台类型
             */
            @Override
            public void cancelCall(String var1) {
                Toast.makeText(MainActivity.this, "取消分享" + var1, Toast.LENGTH_SHORT).show();
            }
        };

        ShareController controller = new ShareController(this,SHARE_PLATFROM.QQ);
        controller.withText("hello");//分享内容
        controller.setCallback(callback);
        controller.share();
    }
//
//    public void set() {
////        //调用withMedia可以设置一个ShareImage，ShareImage的构建有如下几种形式
////        ShareImage image = new ShareImage(this, "imageurl");//网络图片
////        ShareImage image = new ShareImage(this, file);//本地文件
////        ShareImage image = new ShareImage(this, R.drawable.xxx);//资源文件
////        ShareImage image = new ShareImage(this, bitmap);//bitmap文件
////        ShareImage image = new ShareImage(this, byte[]);//字节流
////
////        //推荐使用网络图片和资源文件的方式，平台兼容性更高。对于部分平台，分享的图片需要设置缩略图，缩略图的设置规则为：
////        ShareImage thumb =  new ShareImage(this, R.drawable.thumb);
////        image.setThumb(thumb);
////
////        //用户设置的图片大小最好不要超过250k，缩略图不要超过18k，如果超过1M会报出error,用户可以设置压缩的方式：
////        image.setCompressStyle(ShareImage.CompressStyle.SCALE);//大小压缩，默认为大小压缩，适合普通很大的图
////        image.setCompressStyle(ShareImage.CompressStyle.QUALITY);//质量压缩，适合长图的分享
////
////        ShareController controller = new ShareController(this,SHARE_PLATFROM.QQ);
////        controller.withText("hello");//分享内容
////        controller.setCallback(callback);//回调监听器
////        controller.withMedia(image);//图片
////        controller.share();
//
//        ShareMin min = new ShareMin(Defaultcontent.url);
//        min.setTitle("This is music title");//标题
//        min.setThumb(thumb);//缩略图
//        min.setDescription("my description");//描述
//        min.setPath("pages/page10007/xxxxxx");
//        min.setUserName("xx_xxx");
//        controller.withMedia(min);
//
//        ShareEmoji emoji = new ShareEmoji(this, getSaveFile("/storage/emulated/0/loveshow/camera/crop/1503889044413.gif"));
//        Bitmap bitmap = BitmapFactory.decodeFile("/storage/emulated/0/loveshow/camera/1503889589012.jpg");
//        ShareImage thumb = new ShareImage(this, bitmapSetSize(bitmap,120,120));
//        emoji.setThumb(thumb);
//        shareController.withMedia(emoji);
//
//        UMMin umMin = new UMMin(Defaultcontent.url);
//        umMin.setThumb(imagelocal);
//        umMin.setTitle(Defaultcontent.title);
//        umMin.setDescription(Defaultcontent.text);
//        umMin.setPath("pages/page10007/xxxxxx");
//        umMin.setUserName("xx_xxx");
//
//        controller.setCallback(callback);//回调监听器
//        controller.share();
//    }


    public void normalShare(SHARE_PLATFROM platform) {
        ShareController controller = new ShareController(this,platform);

        ShareImage image = new ShareImage(this, "http://avatar.csdn.net/8/6/E/1_chenliguan.jpg");
        image.setCompressStyle(ShareImage.CompressStyle.QUALITY);
        controller.withMedia(image);

        ShareWeb web = new ShareWeb("https://www.hao123.com/");
        web.setTitle("哈哈哈哈");//标题
        web.setThumb(image);//缩略图
        web.setDescription("哈哈哈哈好好玩");//描述
        controller.withMedia(web);

//        ShareEmoji shareEmoji = new ShareEmoji(this, getSaveFile("/storage/emulated/0/loveshow/camera/crop/1503889044413.gif"));
//        Bitmap shareBitmap = BitmapFactory.decodeFile("/storage/emulated/0/loveshow/camera/1503889589012.jpg");
//        ShareImage shareImage = new ShareImage(this, bitmapSetSize(shareBitmap,120,120));
//        shareEmoji.setThumb(shareImage);
//        shareController.withMedia(shareEmoji);

        controller.setCallback(new ShareHandlerCallback() {
                    @Override
                    public void startCall(String va1) {
                        Toast.makeText(MainActivity.this, "开始分享" + va1, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void successCall(String var1) {
                        Toast.makeText(MainActivity.this, "分享成功" + var1, Toast.LENGTH_SHORT).show();
                        shareHDailog.dismiss();
                    }

                    @Override
                    public void errorCall(String var1, String var2) {
                        Toast.makeText(MainActivity.this, "分享失败" + var1 + var2, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void cancelCall(String var1) {
                        Toast.makeText(MainActivity.this, "取消分享" + var1, Toast.LENGTH_SHORT).show();
                    }
                });
        controller.share();

//        AuthController authController = new AuthController(this, platform,
//                new AuthHandlerCallback() {
//                    @Override
//                    public void startCall(SHARE_PLATFROM var1) {
//
//                    }
//
//                    @Override
//                    public void successCall(SHARE_PLATFROM var1, int var2, Map var3) {
//                        Toast.makeText(MainActivity.this, "授权成功，获取用户信息...", Toast.LENGTH_SHORT).show();
//                        Log.e("tag", "var1:" + var1);
//                        Log.e("tag", "var3:" + var3.toString());
//                    }
//
//                    @Override
//                    public void errorCall(SHARE_PLATFROM var1, int var2, Throwable var3) {
//
//                    }
//
//                    @Override
//                    public void cancelCall(SHARE_PLATFROM var1, int var2) {
//
//                    }
//                });
//        authController.doOauthVerify();
    }

    /**
     * 设置图片大小
     *
     * @param bitmap
     * @param width
     * @param height
     * @return
     */
    public static Bitmap bitmapSetSize(Bitmap bitmap, int width, int height) {
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();

        float scaleWidth = (float) width / bitmapWidth;
        float scaleHeight = (float) height / bitmapHeight;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth, bitmapHeight, matrix, true);
        return newBitmap;
    }

    /**
     * 从指定路径获取文件
     *
     * @return 如果文件不存在则创建, 如果如果无法创建文件或文件名为空则返回null
     */
    public static File getSaveFile(String path) {
        File file = new File(path);
        return file;
    }
}
