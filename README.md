# spring-cache-manager
## spring缓存管理器（针对`@Cacheable`使用场景）

## 使用方式

1. 实现`cn.huazai.tool.spring.cache.manager.core.CacheTemplate`接口

    缓存的实际执行类

2. 注入`cn.huazai.tool.spring.cache.manager.cache.CustomCacheManager`