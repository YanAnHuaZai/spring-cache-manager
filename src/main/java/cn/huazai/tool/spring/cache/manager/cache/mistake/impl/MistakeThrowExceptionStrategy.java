package cn.huazai.tool.spring.cache.manager.cache.mistake.impl;

import cn.huazai.tool.spring.cache.manager.cache.mistake.IMistakeStrategy;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

/**
 * MistakeThrowExceptionStrategy
 * 遇到错误即抛出异常策略
 *
 * @author YanAnHuaZai on 2024-05-27 16:24:54
 */
public class MistakeThrowExceptionStrategy implements IMistakeStrategy {
    @Override
    public Cache getCache(CacheManager cacheManager, String name) {
        throw new IllegalArgumentException(String.format("cache name '%s' is illegal, please check it", name));
    }
}
