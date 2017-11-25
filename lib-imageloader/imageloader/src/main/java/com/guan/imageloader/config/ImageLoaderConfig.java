package com.guan.imageloader.config;

import com.guan.imageloader.cache.BitmapCache;
import com.guan.imageloader.cache.MemoryCache;
import com.guan.imageloader.policy.LoadPolicy;
import com.guan.imageloader.policy.ReversePolicy;

/**
 * Created by Administrator on 2017/2/6 0006.
 */
public class ImageLoaderConfig {

    /**
     * 缓存策略
     */
    private BitmapCache bitmapCache = new MemoryCache();
    /**
     * 加载策略
     */
    private LoadPolicy loadPolicy = new ReversePolicy();
    /**
     * 默认线程数
     */
    private int defaultThreadCount = Runtime.getRuntime().availableProcessors();
    /**
     * 显示的配置
     */
    private DisplayConfig displayConfig = new DisplayConfig();

    private ImageLoaderConfig() {
    }

    /**
     * 建造者模式
     * 和AlterDialog建造过程类似
     */
    public static class Builder {
        private ImageLoaderConfig config;

        public Builder() {
            config = new ImageLoaderConfig();
        }

        /**
         * 设置缓存策略
         *
         * @param bitmapCache
         * @return
         */
        public Builder setCachePolicy(BitmapCache bitmapCache) {
            config.bitmapCache = bitmapCache;
            return this;
        }

        /**
         * 设置加载策略
         *
         * @param loadPolicy
         * @return
         */
        public Builder setLoadPolicy(LoadPolicy loadPolicy) {
            config.loadPolicy = loadPolicy;
            return this;
        }

        /**
         * 设置线程个数
         *
         * @param count
         * @return
         */
        public Builder setThreadCount(int count) {
            config.defaultThreadCount = count;
            return this;
        }

        /**
         * 设置加载过程中的图片
         *
         * @param resID
         * @return
         */
        public Builder setLoadingImage(int resID) {
            config.displayConfig.loadingImage = resID;
            return this;
        }

        /**
         * 设置加载过程中的图片
         *
         * @param resID
         * @return
         */
        public Builder setFaildImage(int resID) {
            config.displayConfig.faildImage = resID;
            return this;
        }

        public ImageLoaderConfig build() {
            return config;
        }
    }

    public BitmapCache getBitmapCache() {
        return bitmapCache;
    }

    public LoadPolicy getLoadPolicy() {
        return loadPolicy;
    }

    public int getThreadCount() {
        return defaultThreadCount;
    }

    public DisplayConfig getDisplayConfig() {
        return displayConfig;
    }
}
