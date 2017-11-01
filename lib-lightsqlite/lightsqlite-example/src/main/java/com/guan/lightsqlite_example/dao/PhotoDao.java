package com.guan.lightsqlite_example.dao;

import com.guan.lightsqlite.dao.BaseDao;
import com.guan.lightsqlite_example.model.PhotoBean;

import java.util.List;

/**
 * Created by david on 20/1/2017.
 */
public class PhotoDao extends BaseDao<PhotoBean> {

    public PhotoDao() {
    }

    @Override
    protected String createTable() {
        return "create table if not exists tb_photo(time varchar(20), path varchar(20))";
    }

    @Override
    public List<PhotoBean> query(String sql) {
        return null;
    }
}
