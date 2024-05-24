package cn.huazai.tool.spring.cache.manager.cache;

import cn.huazai.tool.spring.cache.manager.core.CacheTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 自定义缓存管理器
 *
 * @author YanAnHuaZai on 2024-05-22 15:33:03
 */
@Slf4j
public class CustomCacheManager implements CacheManager {

    private final CacheTemplate cacheTemplate;

    public CustomCacheManager(CacheTemplate cacheTemplate) {
        this.cacheTemplate = cacheTemplate;
    }

    /**
     * 存储所有的缓存对象
     */
    private static final ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<>();

    /**
     * 默认缓存 1分钟
     */
    private static final String DEFAULT_CACHE_TIME = "1min";

    @Override
    public Cache getCache(String name) {
        log.debug("DchCacheManager.getCache name:{}", name);

        Assert.notNull(name, "Name must not be null!");

        return genCacheByName(name);
    }

    @Override
    public Collection<String> getCacheNames() {
        log.debug("DchCacheManager.getCacheNames");
        return cacheMap.keySet();
    }

    private Cache genCacheByName(final String name) {
        return this.genCacheByName(name, true);
    }

    /**
     * 缓存时间规则
     * @param name 缓存名称
     * <p>可选后缀单位</p>
     * <ul>
     *     <li>{@code ms}: 毫秒</li>
     *     <li>{@code s}: 秒</li>
     *     <li>{@code min}: 分钟</li>
     *     <li>{@code h}: 小时</li>
     *     <li>{@code day}: 天</li>
     *     <li>{@code month}: 月</li>
     *     <li>{@code year}: 年</li>
     * </ul>
     * @param retry 是否重试（当缓存名称配置错误时，使用默认配置）
     *
     * @return 缓存时间表达式
     */
    private Cache genCacheByName(final String name, boolean retry) {
        log.debug("DchCacheManager.genCacheByName name:{}", name);

        Assert.notNull(name, "Name must not be null!");

        Cache cache = cacheMap.get(name);
        if (cache == null) {
            synchronized (CustomCacheManager.class) {
                cache = cacheMap.get(name);
                if (cache == null) {
                    // 构建 缓存 对象
                    cache = CustomCache.buildByName(cacheTemplate, name);
                    if (null == cache && retry) {
                        // 构建为null表示缓存名称存在问题（不符合规则），使用默认缓存（1分钟）
                        log.warn("DchCacheManager.genCacheByName '{}'构建为null 使用默认值'{}'重新构建", name, DEFAULT_CACHE_TIME);
                        // 使用默认值重新构建缓存
                        cache = genCacheByName(DEFAULT_CACHE_TIME, false);
                    }
                    if (null != cache) {
                        cacheMap.putIfAbsent(name, cache);
                        log.info("DchCacheManager.cacheMap size:{} value:{}", cacheMap.size(), cacheMap);
                    }
                }
            }
        }
        return cache;
    }

}
