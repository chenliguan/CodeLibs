package com.guan.lightsqlite_example.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.guan.lightsqlite_example.R;
import com.guan.lightsqlite_example.dao.UserDao;
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
            UserBean user = new UserBean();
            user.setUser_Id("V" + i);
            user.setName("android");
            user.setPassword("123456");
            mUserInfoService.saveUser(user);
        }
    }

    public void delete(View view) {
        UserBean user = new UserBean();
        user.setUser_Id("V" + i);
        user.setName("android");
        user.setPassword("123456");
        mUserInfoService.deleteOneUser(user);
    }

    public void update(View view) {
        UserBean user = new UserBean();
        user.setUser_Id("V" + 1);
        user.setName("ios");
        user.setPassword("654321");

        UserBean where = new UserBean();
        where.setName("android");
        mUserInfoService.updateUser(user, where);
    }

    public void queryList(View view) {
        UserBean where = new UserBean();
        where.setName("android");
        where.setUser_Id("V" + 5);
        List<UserBean> list = mUserInfoService.getUsers(where);

        Log.e(TAG, "查询到：" + list.size() + "条数据");
    }

    private int i = 0;

    /**
     * 多用户登录，将其他用户更改为未登录状态
     * @param view
     */
    public void login(View view) {
        ++i;
        UserBean user = new UserBean();
        user.setName("V00" + i);
        user.setPassword("123456");
        user.setUser_Id("V" + i);
        mUserInfoService.saveUser(user);
    }
}
