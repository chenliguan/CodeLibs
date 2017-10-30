package com.guan.lightsqlite_example.helper;

import android.database.sqlite.SQLiteDatabase;

import com.guan.lightsqlite.dao.BaseDao;
import com.guan.lightsqlite.helper.BaseDaoFactory;

/**
 * @author Guan
 * @file com.guan.lightsqlite_example.helper
 * @date 2017/10/29
 * @Version 1.0
 */
public class DaoFactory extends BaseDaoFactory {
    private DaoFactory() {
        super();
    }

    public synchronized <T extends BaseDao<M>, M> T getUserHelper(Class<T> clazz, Class<M> entityClass) {
        userDatabase = SQLiteDatabase.openOrCreateDatabase(PrivateDataBaseEnums.database.getValue(), null);
        BaseDao baseDao = null;
        try {
            baseDao = clazz.newInstance();
            baseDao.init(entityClass, userDatabase);
            map.put(clazz.getSimpleName(), baseDao);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return (T) baseDao;
    }

}
