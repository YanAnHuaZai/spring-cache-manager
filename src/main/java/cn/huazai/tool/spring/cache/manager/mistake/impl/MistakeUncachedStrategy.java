package cn.huazai.tool.spring.cache.manager.mistake.impl;

import cn.huazai.tool.spring.cache.manager.CustomCacheManager;
import cn.huazai.tool.spring.cache.manager.mistake.IMistakeStrategy;
import cn.huazai.tool.spring.cache.manager.mistake.MistakeStrategyEnum;
import cn.huazai.tool.spring.cache.manager.mistake.MistakeStrategyFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

/**
 * MistakeUncachedStrategy
 * 配置错误即不缓存策略
 *
 * @author YanAnHuaZai on 2024-05-28 16:41:26
 */
public class MistakeUncachedStrategy implements IMistakeStrategy {

    /**
     * 不缓存的缓存的缓存名称
     */
    private static final String UNCACHED_CACHE_NAME = "uncached";

    @Override
    public Cache getCache(CacheManager cacheManager, String name) {
        if (cacheManager instanceof CustomCacheManager) {
            CustomCacheManager customCacheManager = (CustomCacheManager) cacheManager;
            // 使用不缓存 重新生成缓存
            return customCacheManager.getCache(UNCACHED_CACHE_NAME, MistakeStrategyFactory.getStrategy(MistakeStrategyEnum.THROW_EXCEPTION));
        }
        return cacheManager.getCache(UNCACHED_CACHE_NAME);
    }
}
