package cn.huazai.tool.spring.cache.manager.cache;

import cn.huazai.tool.spring.cache.manager.cache.mistake.IMistakeStrategy;
import cn.huazai.tool.spring.cache.manager.cache.mistake.MistakeStrategyEnum;
import cn.huazai.tool.spring.cache.manager.cache.mistake.MistakeStrategyFactory;
import cn.huazai.tool.spring.cache.manager.core.CacheTemplate;
import lombok.Setter;
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

    // 配置错误策略
    @Setter
    private IMistakeStrategy mistakeStrategy;

    public CustomCacheManager(CacheTemplate cacheTemplate) {
        Assert.notNull(cacheTemplate, "CacheTemplate must not be null");

        this.cacheTemplate = cacheTemplate;
        // 配置错误默认为抛出异常策略
        this.mistakeStrategy = MistakeStrategyFactory.getStrategy(MistakeStrategyEnum.THROW_EXCEPTION);
    }

    public CustomCacheManager(CacheTemplate cacheTemplate, IMistakeStrategy mistakeStrategy) {
        Assert.notNull(cacheTemplate, "CacheTemplate must not be null");
        Assert.notNull(mistakeStrategy, "MistakeStrategy must not be null");

        this.cacheTemplate = cacheTemplate;
        this.mistakeStrategy = mistakeStrategy;
    }

    /**
     * 存储所有的缓存对象
     */
    private static final ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<>();

    @Override
    public Cache getCache(String name) {
        return this.getCache(name, this.mistakeStrategy);
    }

    public Cache getCache(String name, IMistakeStrategy mistakeStrategy) {
        log.debug("DchCacheManager.getCache name:{}, mistakeStrategy:{}", name, mistakeStrategy);

        Assert.notNull(name, "Name must not be null!");
        Assert.notNull(mistakeStrategy, "MistakeStrategy must not be null!");

        return getCacheByName(name, mistakeStrategy);
    }

    @Override
    public Collection<String> getCacheNames() {
        log.debug("DchCacheManager.getCacheNames");
        return cacheMap.keySet();
    }

    /**
     * 缓存时间规则
     *
     * @param name 缓存名称
     *             <p>可选后缀单位</p>
     *             <ul>
     *                 <li>{@code ms}: 毫秒</li>
     *                 <li>{@code s}: 秒</li>
     *                 <li>{@code min}: 分钟</li>
     *                 <li>{@code h}: 小时</li>
     *                 <li>{@code day}: 天</li>
     *                 <li>{@code month}: 月</li>
     *                 <li>{@code year}: 年</li>
     *             </ul>
     * @param mistakeStrategy 配置错误策略
     * @return 缓存时间表达式
     */
    private Cache getCacheByName(final String name, final IMistakeStrategy mistakeStrategy) {
        log.debug("DchCacheManager.getCacheByName name:{}", name);

        Assert.notNull(name, "Name must not be null!");

        Cache cache = cacheMap.get(name);
        if (cache == null) {
            synchronized (CustomCacheManager.class) {
                cache = cacheMap.get(name);
                if (cache == null) {
                    // 构建 缓存 对象
                    cache = CustomCache.buildByName(cacheTemplate, name);
                    if (null == cache) {
                        // 构建为null表示缓存名称存在问题（不符合规则），使用默认缓存（1分钟）
                        log.warn("DchCacheManager.getCacheByName '{}' 构建为null，将按照'{}'策略重试", name, mistakeStrategy);
                        if (null == mistakeStrategy) {
                            throw new IllegalStateException("MistakeStrategy must not be null");
                        }
                        cache = mistakeStrategy.getCache(this, name);
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
