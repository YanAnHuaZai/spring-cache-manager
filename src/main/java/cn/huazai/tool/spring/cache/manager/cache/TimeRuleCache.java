package cn.huazai.tool.spring.cache.manager.cache;


import cn.huazai.tool.spring.cache.manager.core.CacheTemplate;
import cn.huazai.tool.spring.cache.manager.utils.ValueLoaderCatchUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;

import java.time.Duration;
import java.util.concurrent.Callable;

/**
 * 按照时间规则维度
 *
 * @author YanAnHuaZai on 2024-05-22 15:50:40
 */
@Slf4j
public class TimeRuleCache implements Cache {

    /** 缓存template */
    private final CacheTemplate cacheTemplate;
    /** 缓存名称 */
    private final String name;
    /** 有效期 */
    private final Duration ttl;

    public TimeRuleCache(CacheTemplate cacheTemplate, String name, Duration ttl) {
        this.cacheTemplate = cacheTemplate;
        this.name = name;
        this.ttl = ttl;
    }

    @Override
    public String getName() {
        log.debug("TimeRuleCache.getName(): {}", name);
        return name;
    }

    @Override
    public Object getNativeCache() {
        log.debug("TimeRuleCache.getNativeCache(): {}", cacheTemplate);
        return cacheTemplate;
    }

    @Override
    public ValueWrapper get(Object key) {
        log.debug("TimeRuleCache.get(key): {}", key);
        Object result = cacheTemplate.get(key);
        if (null != result) {
            return () -> result;
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(Object key, Class<T> type) {
        log.debug("TimeRuleCache.get(key, type): {}, type: {}", key, type);
        return (T) cacheTemplate.get(key);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(Object key, Callable<T> valueLoader) {
        log.debug("TimeRuleCache.get(key, valueLoader): {}", key);
        T t = (T) cacheTemplate.get(key);
        if (null == t) {
            T call = ValueLoaderCatchUtil.call(key, valueLoader);
            Boolean result = cacheTemplate.setIfAbsent(key, call, ttl);
            if (Boolean.TRUE.equals(result)) {
                log.debug("TimeRuleCache.valueLoader '{}' set cache success", key);
            } else {
                log.warn("TimeRuleCache.valueLoader '{}' set cache fail", key);
            }
            return call;
        }
        return t;
    }

    @Override
    public void put(Object key, Object value) {
        log.debug("TimeRuleCache.put(key, value): {}, value: {}", key, value);
        cacheTemplate.setIfAbsent(key, value, ttl);
    }

    @Override
    public void evict(Object key) {
        log.debug("TimeRuleCache.evict(key): {}", key);
        cacheTemplate.delete(key);
    }

    @Override
    public void clear() {
        log.debug("TimeRuleCache.clear()");
    }
}
