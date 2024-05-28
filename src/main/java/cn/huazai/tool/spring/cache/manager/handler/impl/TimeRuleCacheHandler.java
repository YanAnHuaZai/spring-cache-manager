package cn.huazai.tool.spring.cache.manager.handler.impl;

import cn.huazai.tool.spring.cache.manager.cache.TimeRuleCache;
import cn.huazai.tool.spring.cache.manager.cache.enums.TimeRuleCacheUnitEnum;
import cn.huazai.tool.spring.cache.manager.core.CacheTemplate;
import cn.huazai.tool.spring.cache.manager.handler.AbstractCacheHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;

import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * CacheTimeHandler
 * 按照时间维度缓存的处理
 *
 * @author YanAnHuaZai on 2024-05-28 14:20:03
 */
@Slf4j
public class TimeRuleCacheHandler extends AbstractCacheHandler {

    /**
     * 缓存名称格式正则
     */
    private static final Pattern CACHE_NAME_PATTERN = Pattern.compile("^(\\d+)(ms|s|min|h|day|month|year)$");

    @Override
    public boolean isFit(String cacheName) {
        return CACHE_NAME_PATTERN.matcher(cacheName).matches();
    }

    @Override
    public Cache getCacheHandle(CacheTemplate cacheTemplate, String cacheName) {
        // 校验缓存名称是否合规
        Matcher matcher = CACHE_NAME_PATTERN.matcher(cacheName);
        if (!matcher.find()) {
            log.warn("CacheTimeHandler.getCache '{}' 不符合缓存名称规则", cacheName);
            return null;
        }

        // 解析缓存名称
        long cacheTime = Long.parseLong(matcher.group(1));
        String cacheTimeUnit = matcher.group(2);
        log.debug("有效期:{} 单位:{}", cacheTime, cacheTimeUnit);

        // 按照单位生成ttl
        TimeRuleCacheUnitEnum timeRuleCacheUnitEnum = TimeRuleCacheUnitEnum.valueOf(cacheTimeUnit);
        // 生成缓存时间
        Duration timeout = timeRuleCacheUnitEnum.genTimeout(cacheTime);

        return new TimeRuleCache(cacheTemplate, cacheName, timeout);
    }
}
