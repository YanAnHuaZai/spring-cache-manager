package cn.huazai.tool.spring.cache.manager.cache.mistake.impl;

import cn.huazai.tool.spring.cache.manager.cache.CustomCacheManager;
import cn.huazai.tool.spring.cache.manager.cache.mistake.IMistakeStrategy;
import cn.huazai.tool.spring.cache.manager.cache.mistake.MistakeStrategyEnum;
import cn.huazai.tool.spring.cache.manager.cache.mistake.MistakeStrategyFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

/**
 * MistakeSpecifiedCacheTimeStrategy
 * 特定缓存时间枚举
 *
 * @author YanAnHuaZai on 2024-05-27 16:28:08
 */
public class MistakeSpecifiedCacheTimeStrategy implements IMistakeStrategy {

    public final String specifiedCacheTime;

    public MistakeSpecifiedCacheTimeStrategy(String specifiedCacheTime) {
        this.specifiedCacheTime = specifiedCacheTime;
    }

    @Override
    public Cache getCache(CacheManager cacheManager, String name) {
        if (cacheManager instanceof CustomCacheManager) {
            CustomCacheManager customCacheManager = (CustomCacheManager) cacheManager;
            // 使用传入的指定时间 重新生成缓存
            return customCacheManager.getCache(specifiedCacheTime, MistakeStrategyFactory.getStrategy(MistakeStrategyEnum.THROW_EXCEPTION));
        }
        return cacheManager.getCache(specifiedCacheTime);
    }
}
