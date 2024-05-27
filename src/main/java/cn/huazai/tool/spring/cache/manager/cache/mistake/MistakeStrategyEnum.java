package cn.huazai.tool.spring.cache.manager.cache.mistake;

/**
 * MistakeStrategyEnum
 * 配置错误策略枚举
 *
 * @author YanAnHuaZai on 2024-05-27 16:19:32
 */
public enum MistakeStrategyEnum {

    /**
     * 抛出异常
     * <p>默认</p>
     */
    THROW_EXCEPTION,
    /**
     * 指定缓存时间
     */
    SPECIFIED_CACHE_TIME
    ;

}
