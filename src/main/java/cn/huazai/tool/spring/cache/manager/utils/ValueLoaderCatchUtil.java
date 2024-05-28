package cn.huazai.tool.spring.cache.manager.utils;

import java.lang.reflect.Constructor;
import java.util.concurrent.Callable;

/**
 * ValueLoaderCatchUtil
 *
 * @author YanAnHuaZai on 2024-05-28 14:52:15
 */
public class ValueLoaderCatchUtil {

    /**
     * 调用业务方法，封装异常处理
     *
     * @param key         缓存键
     * @param valueLoader 业务方法
     * @param <T>         业务返回类型
     * @return 业务返回
     * @author YanAnHuaZai on 2024年05月28日14:53:50
     */
    public static <T> T call(Object key, Callable<T> valueLoader) {
        try {
            return valueLoader.call();
        } catch (Exception e) {
            RuntimeException exception;
            try {
                Class<?> c = Class.forName("org.springframework.cache.Cache$ValueRetrievalException");
                Constructor<?> constructor = c.getConstructor(Object.class, Callable.class, Throwable.class);
                exception = (RuntimeException) constructor.newInstance(key, valueLoader, e);
            } catch (Exception ex) {
                throw new IllegalStateException(ex);
            }
            throw exception;
        }
    }

}
