package com.guan.volleyhttp_example;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.guan.volleyhttp.download.DownloadItemInfo;
import com.guan.volleyhttp.download.interfaces.IDownloadResponse;
import com.guan.volleyhttp.interfaces.Response;
import com.guan.volleyhttp.toolbox.DownFileRequest;
import com.guan.volleyhttp.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static final String URL = "http://apis.juhe.cn/cook/query.php";
    public static final String APPKEY ="8fac966b379367b0e6f0527d634324ee";

    private TextView tvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvContent = (TextView) findViewById(R.id.content);
        Handler handler = new Handler();
        handler.sendEmptyMessage(1);
    }

    public void login(View view) {
        Map<String,String> map = new HashMap();//请求参数
        map.put("menu","龙眼");//需要查询的菜谱名
        map.put("key",APPKEY);//应用APPKEY(应用详细页查询)
        map.put("dtype","");
        map.put("pn","");
        map.put("rn","");
        map.put("albums","");

        for (int i = 0; i < 10; i++) {
            StringRequest.sendRequest(map, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onSuccess(String response) {
                            Log.e("tag", "onSuccess:" + response);
                            tvContent.setText(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onError(String error) {
                            Log.e("tag", "onError:" + error);
                            tvContent.setText(error);
                        }
                    });
        }
    }

    public void down(View view) {
        DownFileRequest.downRequest("http://gdown.baidu.com/data/wisegame/8be18d2c0dc8a9c9/WPSOffice_177.apk", new IDownloadResponse() {
            @Override
            public void onDownloadStatusChanged(DownloadItemInfo downloadItemInfo) {
                tvContent.setText("下载状态:" + downloadItemInfo.getStatus());
                Log.e("tag", "下载状态:" + downloadItemInfo.getStatus());
            }

            @Override
            public void onTotalLengthReceived(DownloadItemInfo downloadItemInfo) {
                tvContent.setText("下载接收");
                Log.e("tag", "下载接收");
            }

            @Override
            public void onCurrentSizeChanged(DownloadItemInfo downloadItemInfo, double downLenth, long speed) {
                tvContent.setText("下载长度:" + downLenth + "-----速度:" + speed/1000 +"kb/s");
                Log.e("tag", "下载长度:" + downLenth + "-----速度:" + speed/1000 +"kb/s");
            }

            @Override
            public void onDownloadSuccess(DownloadItemInfo downloadItemInfo) {
                tvContent.setText("下载成功" + "路径:" + downloadItemInfo.getFilePath() + "-----url:" + downloadItemInfo.getUrl());
                Log.e("tag", "下载成功" + "路径:" + downloadItemInfo.getFilePath() + "-----url:" + downloadItemInfo.getUrl());
            }

            @Override
            public void onDownloadPause(DownloadItemInfo downloadItemInfo) {
                tvContent.setText("下载暂停:" + downloadItemInfo.getStatus());
                Log.e("tag", "下载暂停:" + downloadItemInfo.getStatus());
            }

            @Override
            public void onDownloadError(DownloadItemInfo downloadItemInfo, int var2, String var3) {
                tvContent.setText("下载出错");
                Log.e("tag", "下载出错");
            }
        });
    }
}
