package com.guan.lightsqlite_example.service;

import com.guan.lightsqlite.helper.BaseDaoFactory;
import com.guan.lightsqlite_example.dao.PhotoDao;
import com.guan.lightsqlite_example.dao.UserDao;
import com.guan.lightsqlite_example.helper.Constant;
import com.guan.lightsqlite_example.helper.FileUtil;
import com.guan.lightsqlite_example.helper.PrivateDataBaseEnums;
import com.guan.lightsqlite_example.model.PhotoBean;
import com.guan.lightsqlite_example.model.UserBean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/8 0008.
 */
public class PhotoInfoService {

    private PhotoDao photoDao;

    public PhotoInfoService() {
        String dbPath = PrivateDataBaseEnums.database.getValue();
        photoDao = BaseDaoFactory.getInstance().getDataHelper(dbPath, PhotoDao.class, PhotoBean.class);
    }

    public void savePhoto(PhotoBean photoBean) {
        photoDao.insert(photoBean);
    }

}
