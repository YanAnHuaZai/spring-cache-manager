package cn.huazai.tool.spring.cache.manager.cache.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Duration;

/**
 * 缓存单位枚举
 * <ul>
 *     <li>{@code ms}: 毫秒</li>
 *     <li>{@code s}: 秒</li>
 *     <li>{@code min}: 分钟</li>
 *     <li>{@code h}: 小时</li>
 *     <li>{@code day}: 天(24小时)</li>
 *     <li>{@code week}: 周(7天)</li>
 *     <li>{@code month}: 月(31天)</li>
 *     <li>{@code year}: 年(365天)</li>
 * </ul>
 *
 * @author YanAnHuaZai on 2024-05-23 19:42:59
 */
@Getter
@AllArgsConstructor
public enum TimeRuleCacheUnitEnum {

    /**
     * 缓存单位
     */
    ms("ms", "毫秒"),
    s("s", "秒"),
    min("min", "分钟"),
    h("h", "小时"),
    day("day", "天(24小时)"),
    week("week", "周(7天)"),
    month("month", "月(31天)"),
    year("year", "年(365天)"),
    ;

    private final String unit;
    private final String desc;

    /**
     * 按照当前缓存单位 获取超时时间
     *
     * @param cacheTime 缓存时间
     * @return 超时时间
     */
    public Duration genTimeout(Long cacheTime) {
        switch (this) {
            case ms:
                return Duration.ofMillis(cacheTime);
            case s:
                return Duration.ofSeconds(cacheTime);
            case min:
                return Duration.ofMinutes(cacheTime);
            case h:
                return Duration.ofHours(cacheTime);
            case day:
                return Duration.ofDays(cacheTime);
            case week:
                return Duration.ofDays(7 * cacheTime);
            case month:
                return Duration.ofDays(31 * cacheTime);
            case year:
                return Duration.ofDays(365 * cacheTime);
            default:
                return null;
        }
    }

}
