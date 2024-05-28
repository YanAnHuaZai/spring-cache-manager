package cn.huazai.tool.spring.cache.manager.cache;

import cn.huazai.tool.spring.cache.manager.utils.ValueLoaderCatchUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;

import java.util.concurrent.Callable;

/**
 * UncachedCache
 * 不缓存
 *
 * @author YanAnHuaZai on 2024-05-28 14:41:03
 */
@Slf4j
public class UncachedCache implements Cache {

    @Override
    public String getName() {
        return "uncached";
    }

    @Override
    public Object getNativeCache() {
        return null;
    }

    @Override
    public ValueWrapper get(Object key) {
        // 表示无缓存
        return null;
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        return null;
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        // 直接调用业务方法获取响应
        return ValueLoaderCatchUtil.call(key, valueLoader);
    }

    @Override
    public void put(Object key, Object value) {

    }

    @Override
    public void evict(Object key) {

    }

    @Override
    public void clear() {

    }
}
