package cn.hengyumo.humor.base.cache;

/**
 * EhcacheConfig
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/9
 */
public interface EhcacheConfig {

    /* 内存中最大的缓存对象数量 */
    int MAX_ELEMENTS_IN_MEMORY = 50000;

    /* 否把溢出数据持久化到硬盘 */
    boolean OVER_FLOW_TO_DISK = true;

    /* 是否永久存活 */
    boolean ETERNAL = false;

    /* 缓存空闲多久后失效/秒 */
    int TIME_TO_IDLE_SECONDS = 600;

    /* 缓存最多存活时间/秒 */
    int TIME_TO_lIVE_SECONDS = 86400;

    /* 是否需要持久化到硬盘 */
    boolean DISK_PERSISTENT = false;

    /* 缓存策略 */
    String MEMORY_STORE_EVICTION_POLICY = "LFU";

    /* 默认的cacheName */
    String DEFAULT_CACHE = "cache";
}
