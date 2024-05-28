package cn.huazai.tool.spring.cache.manager.handler;

import cn.huazai.tool.spring.cache.manager.core.CacheTemplate;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;

import java.util.Locale;

/**
 * 缓存处理
 * <p>根据缓存名称提供可处理的实际执行者</p>
 *
 * @author YanAnHuaZai on 2024-05-28 13:58:57
 */
@Slf4j
public abstract class AbstractCacheHandler {

    /**
     * 继承者
     */
    @Setter
    protected AbstractCacheHandler successor;

    /**
     * 是否适配（抽象方法）
     *
     * @param cacheName 缓存名称
     * @return true适配 false不适配
     * @author YanAnHuaZai on 2024年05月28日14:00:09
     */
    public abstract boolean isFit(String cacheName);

    /**
     * 获取缓存对象处理（抽象方法由子类实现）
     * <p>调用前请先确认适配</p>
     *
     * @param cacheName 缓存名称
     * @return 缓存对象
     * @author YanAnHuaZai on 2024年05月28日14:17:00
     */
    protected abstract Cache getCacheHandle(CacheTemplate cacheTemplate, String cacheName);

    /**
     * 获取缓存
     * @author YanAnHuaZai on 2024年05月28日15:06:15
     */
    public Cache getCache(CacheTemplate cacheTemplate, String cacheName) {
        // 将缓存名称转成小写
        cacheName = cacheName.toLowerCase(Locale.ROOT);

        if (isFit(cacheName)) {
            // 如果匹配，则调用获取缓存的处理方法
            log.debug("缓存名称 '{}' 命中 '{}'处理", cacheName, this.getClass().getSimpleName());
            return getCacheHandle(cacheTemplate, cacheName);
        }
        if (null != successor) {
            log.debug("缓存名称 '{}' 未命中 '{}'处理，继续交由'{}'处理", cacheName, this.getClass().getSimpleName(), successor.getClass().getSimpleName());
            return successor.getCache(cacheTemplate, cacheName);
        }
        log.warn("无继承者可获取缓存处理");
        return null;
    }

}
