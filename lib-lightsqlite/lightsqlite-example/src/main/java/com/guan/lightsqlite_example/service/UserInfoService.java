package com.guan.lightsqlite_example.service;

import com.guan.lightsqlite.dao.IBaseDao;
import com.guan.lightsqlite_example.dao.UserDao;
import com.guan.lightsqlite.helper.BaseDaoFactory;
import com.guan.lightsqlite_example.model.UserBean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/8 0008.
 */
public class UserInfoService {

    private IBaseDao<UserBean> userDao;

    public UserInfoService() {
        userDao = BaseDaoFactory.getInstance().getDataHelper(UserDao.class, UserBean.class);
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
