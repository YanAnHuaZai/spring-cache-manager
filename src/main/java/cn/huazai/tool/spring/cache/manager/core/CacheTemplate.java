package cn.huazai.tool.spring.cache.manager.core;

import java.time.Duration;

/**
 * CacheTemplate
 *
 * @author YanAnHuaZai on 2024-05-24 14:13:52
 */
public interface CacheTemplate {

    Object get(Object key);

    Boolean set(Object key, Object value, Duration timeout);

    Boolean setIfAbsent(Object key, Object value, Duration timeout);

    void delete(Object key);

}
