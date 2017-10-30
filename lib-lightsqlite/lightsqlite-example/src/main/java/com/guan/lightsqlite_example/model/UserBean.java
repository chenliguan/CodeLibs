package com.guan.lightsqlite_example.model;

import com.guan.lightsqlite.annotion.DbFiled;
import com.guan.lightsqlite.annotion.DbTable;

/**
 * Created by Administrator on 2017/1/9 0009.
 */
@DbTable("tb_user")
public class UserBean {

    public String user_id;

    @DbFiled("user_name")
    public String name;

    @DbFiled("user_password")
    public String password;

    @DbFiled("user_status")
    public Integer status;

    public String getUser_Id() {
        return user_id;
    }

    public void setUser_Id(String user_Id) {
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "user_id=" + user_id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", status=" + status +
                '}';
    }
}
