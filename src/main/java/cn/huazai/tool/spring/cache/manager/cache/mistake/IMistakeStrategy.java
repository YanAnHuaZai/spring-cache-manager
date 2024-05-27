package cn.huazai.tool.spring.cache.manager.cache.mistake;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

/**
 * IMistakeStrategy
 *
 * @author YanAnHuaZai on 2024-05-27 16:23:47
 */
public interface IMistakeStrategy {

    /**
     * 配置错误的策略
     *
     * @param cacheManager 缓存管理器
     * @param name 缓存名称
     * @return 缓存
     * @author YanAnHuaZai on 2024年05月27日16:24:31
     */
    Cache getCache(CacheManager cacheManager, String name);

}
