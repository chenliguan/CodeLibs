package com.guan.lightsqlite_example.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.guan.lightsqlite_example.R;
import com.guan.lightsqlite_example.service.UserInfoService;
import com.guan.lightsqlite_example.model.UserBean;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "tag";

    private UserInfoService mUserInfoService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initService();
    }

    private void initService() {
        mUserInfoService = new UserInfoService();
    }

    public void save(View view) {
        for (int i = 0; i < 10; i++) {
            UserBean userBean = new UserBean(i, "android", "123456");
            mUserInfoService.saveUser(userBean);
        }
    }

    public void delete(View view) {
        UserBean userBean = new UserBean(2, "android", "123456");
        mUserInfoService.deleteOneUser(userBean);
    }

    public void update(View view) {
        UserBean userBean = new UserBean(1, "ios", "654321");
        UserBean where = new UserBean();
        where.setName("android");
        mUserInfoService.updateUser(userBean, where);
    }

    public void queryList(View view) {
        UserBean where = new UserBean();
        where.setName("android");
        where.setUser_Id(5);
        List<UserBean> list = mUserInfoService.getUsers(where);
        Log.e(TAG, "查询到：" + list.size() + "条数据");
    }
}
