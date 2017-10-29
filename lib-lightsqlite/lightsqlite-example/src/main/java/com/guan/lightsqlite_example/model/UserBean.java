package com.guan.lightsqlite_example.model;

import com.guan.lightsqlite.annotion.DbFiled;
import com.guan.lightsqlite.annotion.DbTable;

/**
 * Created by Administrator on 2017/1/9 0009.
 */
@DbTable("tb_user")
public class UserBean {

    public int user_id=0;

    @DbFiled("user_name")
    public String name;
    @DbFiled("user_password")
    public String password;

    public UserBean( ) {
    }

    public UserBean(Integer user_id, String name, String password) {
        this.user_id= user_id;
        this.name = name;
        this.password = password;
    }

    public Integer getUser_Id() {
        return user_id;
    }

    public void setUser_Id(Integer user_Id) {
        this.user_id = user_Id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "name  "+name+"  password "+password;
    }
}
