package cn.huazai.tool.spring.cache.manager.handler;

import cn.huazai.tool.spring.cache.manager.handler.impl.TimeRuleCacheHandler;
import cn.huazai.tool.spring.cache.manager.handler.impl.UncachedHandler;

/**
 * CacheBuilderFactory
 * 缓存构建工厂
 *
 * @author YanAnHuaZai on 2024-05-28 14:01:40
 */
public class CacheBuilderFactory {
    private CacheBuilderFactory(){}

    /** 首个处理类 */
    private static AbstractCacheHandler firstCacheHandler;
    /** 末个处理类 */
    private static AbstractCacheHandler lastCacheHandler;

    static {
        // 如有其他的处理类，在此新增
        addHandler(new TimeRuleCacheHandler());
        addHandler(new UncachedHandler());
    }

    public static AbstractCacheHandler getInstance() {
        return firstCacheHandler;
    }

    public static void addHandler(AbstractCacheHandler cacheHandler) {
        if (null == firstCacheHandler) {
            // 首个处理类
            firstCacheHandler = cacheHandler;
        }
        if (null != lastCacheHandler) {
            // 将上一位处理，设置继承者为当前
            lastCacheHandler.setSuccessor(cacheHandler);
        }
        // 将当前继承者设置为末尾处理
        lastCacheHandler = cacheHandler;
    }

}
