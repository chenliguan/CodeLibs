package com.guan.lightsqlite_example.service;

import com.guan.lightsqlite_example.dao.UserDao;
import com.guan.lightsqlite.helper.BaseDaoFactory;
import com.guan.lightsqlite_example.helper.Constant;
import com.guan.lightsqlite_example.helper.FileUtil;
import com.guan.lightsqlite_example.model.UserBean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/8 0008.
 */
public class UserInfoService {

    private UserDao userDao;

    public UserInfoService() {
        userDao = BaseDaoFactory.getInstance().getDataHelper(
                FileUtil.databasePath(Constant.FILE_NAME,Constant.USER_DB_NAME),
                UserDao.class, UserBean.class);
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void deleteOneUser(UserBean userBean) {
        userDao.delete(userBean);
    }

    public void saveUser(UserBean userBean) {
        userDao.insert(userBean);
    }

    public void updateUser(UserBean userBean, UserBean where) {
        userDao.update(userBean, where);
    }

    public List<UserBean> getUsers(UserBean where) {
        return userDao.query(where);
    }

}
