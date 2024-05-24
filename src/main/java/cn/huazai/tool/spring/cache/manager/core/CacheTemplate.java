package cn.huazai.tool.spring.cache.manager.core;

import java.time.Duration;

/**
 * CacheTemplate
 * 缓存操作-接口
 * <p>需要使用方提供基于该接口的实现类，实现缓存的具体操作</p>
 *
 * @author YanAnHuaZai on 2024-05-24 14:13:52
 */
public interface CacheTemplate {

    /**
     * 获取缓存
     * @author YanAnHuaZai on 2024年05月24日15:45:01
     * @param key 缓存键
     * @return 缓存值
     */
    Object get(Object key);

    /**
     * 设置缓存
     * @author YanAnHuaZai on 2024年05月24日15:45:06
     * @param key 缓存键
     * @param value 缓存值
     * @param timeout 缓存有效期
     * @return 是否设置成功
     */
    Boolean set(Object key, Object value, Duration timeout);

    /**
     * 设置缓存（如果缓存不存在）
     * @author YanAnHuaZai on 2024年05月24日15:45:06
     * @param key 缓存键
     * @param value 缓存值
     * @param timeout 缓存有效期
     * @return 是否设置成功（如果缓存不存在设置成功则返回true）
     */
    Boolean setIfAbsent(Object key, Object value, Duration timeout);

    /**
     * 删除缓存
     * @author YanAnHuaZai on 2024年05月24日15:45:06
     * @param key 缓存键
     */
    void delete(Object key);

}
