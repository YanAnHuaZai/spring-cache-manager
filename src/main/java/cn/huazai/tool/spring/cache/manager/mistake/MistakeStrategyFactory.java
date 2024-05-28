package cn.huazai.tool.spring.cache.manager.mistake;

import cn.huazai.tool.spring.cache.manager.mistake.impl.MistakeThrowExceptionStrategy;

import java.util.HashMap;
import java.util.Map;

/**
 * MistakeStrategyFactory
 * 配置错误策略工厂
 *
 * @author YanAnHuaZai on 2024-05-27 16:30:11
 */
public class MistakeStrategyFactory {

    private MistakeStrategyFactory() {}

    /**
     * 配置错误策略集合
     */
    private static final Map<MistakeStrategyEnum, IMistakeStrategy> STRATEGY_MAP = new HashMap<>();

    static {
        // 抛出异常策略
        STRATEGY_MAP.put(MistakeStrategyEnum.THROW_EXCEPTION, new MistakeThrowExceptionStrategy());
    }

    public static IMistakeStrategy getStrategy(MistakeStrategyEnum strategyEnum) {
        return STRATEGY_MAP.get(strategyEnum);
    }

}
