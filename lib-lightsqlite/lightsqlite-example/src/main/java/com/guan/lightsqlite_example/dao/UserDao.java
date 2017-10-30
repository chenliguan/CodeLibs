package com.guan.lightsqlite_example.dao;

import android.util.Log;

import com.guan.lightsqlite.dao.BaseDao;
import com.guan.lightsqlite_example.model.UserBean;

import java.util.List;

import static android.R.id.list;
import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 2017/1/9 0009.
 */
public class UserDao extends BaseDao<UserBean> {

    public UserDao() {

    }

    @Override
    protected String createTable() {
        return "create table if not exists tb_user(user_id varchar(20), user_name varchar(20), user_password varchar(10), user_status Integer)";
    }

    @Override
    public List query(String sql) {
        return null;
    }

    @Override
    public Long insert(UserBean entity) {
        UserBean query = new UserBean();
        query.setStatus(1);
        List<UserBean> userList = query(query);

        for (UserBean user : userList) {
            UserBean where = new UserBean();
            where.setUser_Id(user.getUser_Id());
            user.setStatus(0);
            update(user, where);
            Log.i(TAG, "用户" + user.getName() + "更改为未登录状态");
        }

        Log.i(TAG, "用户" + entity.getName() + "登录");
        entity.setStatus(1);
        return super.insert(entity);
    }

    /**
     * 得到当前登录的User
     *
     * @return UserBean
     */
    public UserBean getCurrentUser() {
        UserBean user = new UserBean();
        user.setStatus(1);
        List<UserBean> list = query(user);
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }
}
