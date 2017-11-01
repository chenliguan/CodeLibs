package com.guan.lightsqlite_example.helper;

import android.os.Environment;

import com.guan.lightsqlite_example.dao.UserDao;
import com.guan.lightsqlite_example.model.UserBean;
import com.guan.lightsqlite_example.service.UserInfoService;

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
        UserInfoService userService = new UserInfoService();
        UserDao userDao = userService.getUserDao();
        if (userDao != null) {
            UserBean currentUser = userDao.getCurrentUser();
            if (currentUser != null) {
                File file = FileUtil.getFile(Environment.getExternalStorageDirectory() + "/" + Constant.FILE_NAME + "/" + currentUser.getUser_Id());
                value = file.getAbsolutePath() + Constant.FILE_DB_NAMEE;
            }
        }
        return value;
    }
}
