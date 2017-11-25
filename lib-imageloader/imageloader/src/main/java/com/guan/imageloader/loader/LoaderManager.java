package com.guan.imageloader.loader;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/8 0008.
 */

public class LoaderManager {
    //缓存所有支持的Loader类型
    private Map<String, Loader> mLoaderMap = new HashMap<>();

    private static LoaderManager mInstance = new LoaderManager();

    private LoaderManager() {
        register("http", new UrlLoader());
        register("https", new UrlLoader());
        register("file", new LocalLoader());
    }

    public static LoaderManager getInstance() {
        return mInstance;
    }

    private void register(String schema, Loader loader) {
        mLoaderMap.put(schema, loader);
    }

    public Loader getLoader(String schema) {
        if (mLoaderMap.containsKey(schema)) {
            return mLoaderMap.get(schema);
        }
        return new NullLoader();
    }


}
