package com.guan.lightsqlite.helper;

import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.guan.lightsqlite.dao.BaseDao;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/9 0009.
 */
public class BaseDaoFactory {

    private static BaseDaoFactory instance = new BaseDaoFactory();
    private Map<String, BaseDao> map = Collections.synchronizedMap(new HashMap<String, BaseDao>());

    private BaseDaoFactory() {}

    public <T extends BaseDao<M>, M> T getDataHelper(String sqliteDatabasePath, Class<T> clazz, Class<M> entityClass) {
        SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(sqliteDatabasePath, null);

        BaseDao baseDao = null;
        if (map.get(clazz.getSimpleName()) != null) {
            return (T) map.get(clazz.getSimpleName());
        }
        try {
            baseDao = clazz.newInstance();
            baseDao.init(entityClass, sqLiteDatabase);
            map.put(clazz.getSimpleName(), baseDao);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return (T) baseDao;
    }

    public static BaseDaoFactory getInstance() {
        return instance;
    }

}
