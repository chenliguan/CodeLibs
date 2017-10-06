package com.guan.volleyhttp_example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.guan.volleyhttp.interfaces.Response;
import com.guan.volleyhttp.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static final String url = "http://apis.juhe.cn/cook/query.php";
    public static final String APPKEY ="8fac966b379367b0e6f0527d634324ee";

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.content);
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
            Volley.sendRequest(map, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onSuccess(String response) {
                            Log.e("tag", "onSuccess:" + response);
                            textView.setText(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onError(String error) {
                            Log.e("tag", "onError:" + error);
                            textView.setText(error);
                        }
                    });
        }
    }
}
