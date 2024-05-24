# spring-cache-manager
## spring缓存管理器（针对`@Cacheable`使用场景）

## 使用方式

1. 实现`cn.huazai.tool.spring.cache.manager.core.CacheTemplate`接口

    缓存的实际执行类

   eg.
   ```java
   @Slf4j
   @Component
   public class RedisCacheTemplateImpl implements CacheTemplate {
   
       @Resource
       private RedisTemplate<String, Object> redisTemplate;
   
       @Override
       public Object get(Object key) {
           return redisTemplate.opsForValue().get(key);
       }
   
       @Override
       public Boolean set(Object key, Object value, Duration timeout) {
           redisTemplate.opsForValue().set((String) key, value, timeout);
           return true;
       }
   
       @Override
       public Boolean setIfAbsent(Object key, Object value, Duration timeout) {
           return redisTemplate.opsForValue().setIfAbsent((String) key, value, timeout);
       }
   
       @Override
       public void delete(Object key) {
           redisTemplate.delete((String) key);
       }
   }
   ```

2. 注入`cn.huazai.tool.spring.cache.manager.cache.CustomCacheManager`

   eg.
   ```java
   @Slf4j
   @Configuration
   public class CacheManagerConfig {
   
       @Bean
       public CacheManager cacheManager(CacheTemplate cacheTemplate) {
           log.info("initializing cacheManager: cacheTemplate");
           return new CustomCacheManager(cacheTemplate);
       }
   
   }   
   ```