package cn.huazai.tool.spring.cache.manager.handler.impl;

import cn.huazai.tool.spring.cache.manager.cache.UncachedCache;
import cn.huazai.tool.spring.cache.manager.core.CacheTemplate;
import cn.huazai.tool.spring.cache.manager.handler.AbstractCacheHandler;
import org.springframework.cache.Cache;

/**
 * UncachedHandler
 * 不缓存的处理
 *
 * @author YanAnHuaZai on 2024-05-28 14:59:32
 */
public class UncachedHandler extends AbstractCacheHandler {

    private final String CACHE_NAME = "uncached";

    @Override
    public boolean isFit(String cacheName) {
        return CACHE_NAME.equalsIgnoreCase(cacheName);
    }

    @Override
    public Cache getCacheHandle(CacheTemplate cacheTemplate, String cacheName) {
        if (!CACHE_NAME.equalsIgnoreCase(cacheName)) {
            return null;
        }

        return new UncachedCache();
    }
}
