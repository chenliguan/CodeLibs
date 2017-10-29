package com.guan.lightsqlite_example.dao;

import com.guan.lightsqlite.dao.BaseDao;

import java.util.List;

/**
 * Created by Administrator on 2017/1/9 0009.
 */
public class UserDao extends BaseDao {

    public UserDao() {

    }

    @Override
    protected String createTable() {
        return "create table if not exists tb_user(user_id int, user_name varchar(20), user_password varchar(10))";
    }

    @Override
    public List query(String sql) {
        return null;
    }
}
