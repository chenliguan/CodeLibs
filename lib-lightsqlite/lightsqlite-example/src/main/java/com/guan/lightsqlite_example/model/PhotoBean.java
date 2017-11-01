package com.guan.lightsqlite_example.model;

import com.guan.lightsqlite.annotion.DbFiled;
import com.guan.lightsqlite.annotion.DbTable;

/**
 * Created by david on 20/1/2017.
 */
@DbTable("tb_photo")
public class PhotoBean {

    @DbFiled("time")
    public String time;
    @DbFiled("path")
    public String path;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
