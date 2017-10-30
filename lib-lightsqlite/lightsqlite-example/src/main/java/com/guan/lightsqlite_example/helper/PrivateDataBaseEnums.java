package com.guan.lightsqlite_example.helper;

import android.os.Environment;

import com.guan.lightsqlite.helper.BaseDaoFactory;
import com.guan.lightsqlite_example.dao.UserDao;
import com.guan.lightsqlite_example.model.UserBean;

import java.io.File;

/**
 * Created by david on 20/1/2017.
 */

public enum PrivateDataBaseEnums {

    /**
     * 存放本地数据库的路径
     */
    database("local/data/database/");

    /**
     * 文件存储的文件路径
     */
    private String value;

    PrivateDataBaseEnums(String value) {
        this.value = value;
    }

    /**
     * 使用PrivateDataBaseEnums.database.getValue()调用
     * 修改枚举变量，即可实现路径修改
     *
     * @return
     */
    public String getValue() {
        UserDao userDao = BaseDaoFactory.getInstance().getDataHelper(UserDao.class, UserBean.class);
        if (userDao != null) {
            UserBean currentUser = userDao.getCurrentUser();
            if (currentUser != null) {
                File file = new File(Environment.getExternalStorageDirectory(), "update");
                if (!file.exists()) {
                    file.mkdirs();
                }
                return file.getAbsolutePath() + "/" + currentUser.getUser_Id() + "/logic.db";
            }

        }
        return value;
    }
}
