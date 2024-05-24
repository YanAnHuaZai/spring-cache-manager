package cn.huazai.tool.spring.cache.manager.cache;


import cn.huazai.tool.spring.cache.manager.core.CacheTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.util.Assert;

import java.lang.reflect.Constructor;
import java.time.Duration;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 自定义缓存实现类
 *
 * @author YanAnHuaZai on 2024-05-22 15:50:40
 */
@Slf4j
public class CustomCache implements Cache {

    /**
     * 缓存名称格式正则
     */
    private static final Pattern CACHE_NAME_PATTERN = Pattern.compile("^(\\d+)(ms|s|min|h|day|month|year)$");

    /** 缓存template */
    private final CacheTemplate cacheTemplate;
    /** 缓存名称 */
    private final String name;
    /** 有效期 */
    private final Duration ttl;

    public CustomCache(CacheTemplate cacheTemplate, String name, Duration ttl) {
        this.cacheTemplate = cacheTemplate;
        this.name = name;
        this.ttl = ttl;
    }

    /**
     * 缓存时间规则
     * <p>可选后缀单位 {@link CacheUnitEnum}</p>
     * <ul>
     *     <li>{@code ms}: 毫秒</li>
     *     <li>{@code s}: 秒</li>
     *     <li>{@code min}: 分钟</li>
     *     <li>{@code h}: 小时</li>
     *     <li>{@code day}: 天(24小时)</li>
     *     <li>{@code week}: 周(7天)</li>
     *     <li>{@code month}: 月(31天)</li>
     *     <li>{@code year}: 年(365天)</li>
     * </ul>
     *
     * @return 缓存时间表达式
     */
    public static CustomCache buildByName(CacheTemplate cacheTemplate, String name) {
        name = name.toLowerCase();

        // 校验缓存名称是否合规
        Matcher matcher = CACHE_NAME_PATTERN.matcher(name);
        if (!matcher.find()) {
            log.warn("CustomCache.buildByName 不符合缓存名称规则, name:{}", name);
            return null;
        }

        // 解析缓存名称
        long cacheTime = Long.parseLong(matcher.group(1));
        String cacheTimeUnit = matcher.group(2);
        log.debug("有效期:{} 单位:{}", cacheTime, cacheTimeUnit);

        // 按照单位生成ttl
        CacheUnitEnum cacheTimeUnitEnum = CacheUnitEnum.valueOf(cacheTimeUnit);
        // 生成缓存时间
        Duration timeout = cacheTimeUnitEnum.genTimeout(cacheTime);

        return new CustomCache(cacheTemplate, name, timeout);
    }

    @Override
    public String getName() {
        log.debug("CustomCache.getName(): {}", name);
        return name;
    }

    @Override
    public Object getNativeCache() {
        log.debug("CustomCache.getNativeCache(): {}", cacheTemplate);
        return cacheTemplate;
    }

    @Override
    public ValueWrapper get(Object key) {
        log.debug("CustomCache.get(key): {}", key);
        Object result = cacheTemplate.get(key);
        if (null != result) {
            return () -> result;
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(Object key, Class<T> type) {
        log.debug("CustomCache.get(key, type): {}, type: {}", key, type);
        return (T) cacheTemplate.get(key);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(Object key, Callable<T> valueLoader) {
        log.debug("CustomCache.get(key, valueLoader): {}", key);
        Assert.notNull(name, "Name must not be null!");
        Assert.notNull(key, "Key must not be null!");
        T t = (T) cacheTemplate.get(key);
        if (null == t) {
            try {
                T call = valueLoader.call();
                Boolean result = cacheTemplate.setIfAbsent(key, call, ttl);
                if (Boolean.TRUE.equals(result)) {
                    return call;
                }
            } catch (Exception ex) {
                RuntimeException exception;
                try {
                    Class<?> c = Class.forName("org.springframework.cache.Cache$ValueRetrievalException");
                    Constructor<?> constructor = c.getConstructor(Object.class, Callable.class, Throwable.class);
                    exception = (RuntimeException) constructor.newInstance(key, valueLoader, ex);
                } catch (Exception e) {
                    throw new IllegalStateException(e);
                }
                throw exception;
            }
        }
        return t;
    }

    @Override
    public void put(Object key, Object value) {
        log.debug("CustomCache.put(key, value): {}, value: {}", key, value);
        cacheTemplate.setIfAbsent(key, value, ttl);
    }

    @Override
    public void evict(Object key) {
        log.debug("CustomCache.evict(key): {}", key);
        cacheTemplate.delete(key);
    }

    @Override
    public void clear() {
        log.debug("CustomCache.clear()");
    }
}
