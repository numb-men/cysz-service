package cn.hengyumo.humor.base.cache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * ehcache工具类
 */
@SuppressWarnings({"unused", "WeakerAccess", "UnusedReturnValue"})
@Component
public class EhcacheCache implements EhcacheConfig {

    private static CacheManager cacheManager = null;
    private static Cache cache = null;

    static {
        initCacheManager();
        initCache();
    }
    /**
     *
     * 初始化缓存管理容器
     */
    public static CacheManager initCacheManager() {
        try {
            if (cacheManager == null){
                cacheManager = CacheManager.getInstance();
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return cacheManager;
    }

    /**
     *
     * 初始化缓存管理容器
     *
     * @param path ehcache.xml存放的路徑
     */
    public static CacheManager initCacheManager(String path) {
        try {
            if (cacheManager == null) {
                CacheManager.getInstance();
                cacheManager = CacheManager.create(path);
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return cacheManager;
    }


    /**
     * 初始化cache
     */
    public static Cache initCache() {
        return initCache(DEFAULT_CACHE);
    }

    /**
     * 获取cache
     */
    public static Cache initCache(String cacheName) {
        checkCacheManager();
        if (null == cacheManager.getCache(cacheName)) {
            cacheManager.addCache(cacheName);
        }
        cache = cacheManager.getCache(cacheName);
        return cache;
    }

    /**
     * 添加缓存
     *
     * @param key 关键字
     * @param value 值
     */
    public static void put(Object key, Object value) {
        checkCache();
        // 创建Element,然后放入Cache对象中
        Element element = new Element(key, value);
        cache.put(element);
    }

    /**
     * 获取cache
     *
     * @param key 关键字
     * @return value
     */
    public static Object get(Object key) {
        checkCache();
        Element element = cache.get(key);
        if (null == element) {
            return null;
        }
        return element.getObjectValue();
    }

    /**
     * 初始化缓存
     *
     * @param cacheName 缓存名称
     * @param maxElementsInMemory 元素最大数量
     * @param overflowToDisk 是否持久化到硬盘
     * @param eternal 是否永远存活
     * @param timeToLiveSeconds 缓存存活时间
     * @param timeToIdleSeconds 缓存的间隔时间
     *
     * @return cache 缓存
     * @throws RuntimeException e
     */
    public static Cache initCache(String cacheName, int maxElementsInMemory, boolean overflowToDisk, boolean eternal,
                                  long timeToLiveSeconds, long timeToIdleSeconds) throws RuntimeException {
        try {
            Cache myCache = cacheManager.getCache(cacheName);
            if (myCache != null) {
                CacheConfiguration config = cache.getCacheConfiguration();
                config.setTimeToLiveSeconds(timeToLiveSeconds);
                config.setMaxEntriesLocalHeap(maxElementsInMemory);
                config.setOverflowToDisk(overflowToDisk);
                config.setEternal(eternal);
                config.setTimeToIdleSeconds(timeToIdleSeconds);
            }
            if (myCache == null) {
                Cache memoryOnlyCache = new Cache(cacheName, maxElementsInMemory, overflowToDisk, eternal, 
                        timeToLiveSeconds, timeToIdleSeconds);
                cacheManager.addCache(memoryOnlyCache);
                myCache = cacheManager.getCache(cacheName);
            }
            return myCache;
        } catch (RuntimeException e) {
            throw new RuntimeException("init cache " + cacheName + " failed!!!");
        }
    }

    /**
     * 初始化cache
     *
     * @param cacheName cache的名字
     * @param timeToLiveSeconds 有效时间
     *                          
     * @return cache 缓存
     * @throws RuntimeException e
     */
    public static Cache initCache(String cacheName, long timeToLiveSeconds) throws RuntimeException {
        return initCache(cacheName, MAX_ELEMENTS_IN_MEMORY, OVER_FLOW_TO_DISK,
                ETERNAL, timeToLiveSeconds, TIME_TO_IDLE_SECONDS);
    }

    /**
     * 初始化Cache
     *
     * @param cacheName cache容器名
     * @return cache容器
     * @throws RuntimeException e
     */
    public static Cache initMyCache(String cacheName) throws RuntimeException {
        return initCache(cacheName, TIME_TO_lIVE_SECONDS);
    }

    /**
     * 修改缓存容器配置
     *
     * @param cacheName 缓存名
     * @param timeToLiveSeconds 有效时间
     * @param maxElementsInMemory 最大数量
     * @throws RuntimeException e
     */
    public static boolean modifyCache(String cacheName, long timeToLiveSeconds, int maxElementsInMemory) throws RuntimeException {
        try {
            if (cacheName != null &&  ! cacheName.equals("")
                    && timeToLiveSeconds != 0L && maxElementsInMemory != 0) {
                Cache myCache = cacheManager.getCache(cacheName);
                CacheConfiguration config = myCache.getCacheConfiguration();
                config.setTimeToLiveSeconds(timeToLiveSeconds);
                config.setMaxEntriesLocalHeap(maxElementsInMemory);
                return true;
            } else {
                return false;
            }
        } catch (RuntimeException e) {
            throw new RuntimeException("modify cache " + cacheName + " failed!!!");
        }
    }

    /**
     *
     * 向指定容器中设置值
     *
     * @param cacheName 容器名
     * @param key 键
     * @param value 值
     * @return 返回真
     *
     * @throws RuntimeException e
     */

    public static boolean setValue(String cacheName, String key, Object value) throws RuntimeException {
        try {
            Cache myCache = cacheManager.getCache(cacheName);
            if (myCache == null) {
                myCache = initCache(cacheName);
            }
            myCache.put(new Element(key, value));
            return true;
        } catch (RuntimeException e) {
            throw new RuntimeException("set cache " + cacheName + " failed!!!");
        }
    }

    /**
     *
     * 向指定容器中设置值
     *
     * @param cacheName 容器名
     * @param key 键
     * @param value 值
     * @param timeToLiveSeconds 存活时间
     * @return 真
     * @throws RuntimeException e
     */
    public static boolean setValue(String cacheName, String key, Object value, Integer timeToLiveSeconds) throws RuntimeException {
        try {
            Cache myCache = cacheManager.getCache(cacheName);
            if (myCache == null) {
                initCache(cacheName, timeToLiveSeconds);
                myCache = cacheManager.getCache(cacheName);
            }
            myCache.put(new Element(key, value, TIME_TO_IDLE_SECONDS, timeToLiveSeconds));
            return true;
        } catch (RuntimeException e) {
            throw new RuntimeException("set cache " + cacheName + " failed!!!");
        }
    }

    /**
     * 从ehcache的指定容器中取值
     *
     * @param key 键
     * @return 返回Object类型的值
     *
     * @throws RuntimeException e
     */
    public static Object getValue(String cacheName, String key) throws RuntimeException {
        try {
            Cache myCache = cacheManager.getCache(cacheName);
            if (myCache == null) {
                myCache = initMyCache(cacheName);
            }
            Element element = myCache.get(key);
            return element == null ? null : element.getObjectValue();
        } catch (RuntimeException e) {
            throw new RuntimeException("get cache " + cacheName + " value failed!!!");
        }
    }

    /**
     * 删除指定的ehcache容器
     *
     * @param cacheName cacheName
     *
     * @return 真
     * @throws RuntimeException e
     */

    public static boolean removeCache(String cacheName) throws RuntimeException {
        try {
            cacheManager.removeCache(cacheName);
            return true;
        } catch (RuntimeException e) {
            throw new RuntimeException("remove cache " + cacheName + " failed!!!");
        }
    }

    /**
     * 删除所有的ehcache容器
     *
     * @return 返回真
     * @throws RuntimeException e
     */

    public static boolean removeAllCache() throws RuntimeException {
        try {
            cacheManager.removeAllCaches();
            return true;
        } catch (RuntimeException e) {
            throw new RuntimeException("remove cache failed!!!");
        }
    }

    /**
     *
     * 删除ehcache容器中的元素
     *
     * @param cacheName 容器名
     * @param key 键
     *
     * @return 真
     * @throws RuntimeException e
     */

    public static boolean removeElement(String cacheName, String key) throws RuntimeException {
        try {
            Cache myCache = cacheManager.getCache(cacheName);
            myCache.remove(key);
            return true;
        } catch (RuntimeException e) {
            throw new RuntimeException("remove cache " + cacheName + " failed!!!");
        }
    }

    /**
     * 删除指定容器中的所有元素
     *
     * @param cacheName 容器名
     * @param key 键
     *
     * @return 真
     * @throws RuntimeException e
     */

    public static boolean removeAllElement(String cacheName, String key) throws RuntimeException {
        try {
            Cache myCache = cacheManager.getCache(cacheName);
            myCache.removeAll();
            return true;
        } catch (RuntimeException e) {
            throw new RuntimeException("remove cache failed!!!");
        }
    }

    /**
     * 释放CacheManage
     */
    public static void shutdown() {
        cacheManager.shutdown();
    }

    /**
     * 移除默认cache
     */
    public static void removeCache() {
        checkCacheManager();
        if (null != cache) {
            cacheManager.removeCache(DEFAULT_CACHE);
        }
        cache = null;
    }

    /**
     * 移除默认cache中的key
     *
     * @param key key
     */
    public static void remove(String key) {
        checkCache();
        cache.remove(key);
    }

    /**
     * 移除默认cache所有Element
     */
    public static void removeAllKey() {
        checkCache();
        cache.removeAll();
    }

    /**
     * 获取所有的cache名称
     *
     * @return String[]
     */
    public static String[] getAllCaches() {
        checkCacheManager();
        return cacheManager.getCacheNames();
    }

    /**
     * 获取Cache所有的Keys
     *
     * @return List
     */
    public static List getKeys() {
        checkCache();
        return cache.getKeys();
    }

    /**
     * 检测cacheManager
     */
    private static void checkCacheManager() {
        if (null == cacheManager) {
            throw new IllegalArgumentException("调用前请先初始化CacheManager值：initCacheManager");
        }
    }

    /**
     * 检测cache
     */
    private static void checkCache() {
        if (null == cache) {
            throw new IllegalArgumentException("调用前请先初始化Cache值：initCache(参数)");
        }
    }

    /**
     * 输出cache信息
     */
    public static void showCache() {
        String[] cacheNames = cacheManager.getCacheNames();
        System.out.println("缓存的key cacheNames length := "
                + cacheNames.length + " 具体详细列表如下：");
        for (String cacheName : cacheNames) {
            System.out.println("cacheName := " + cacheName);
            Cache cache = cacheManager.getCache(cacheName);
            List cacheKeys = cache.getKeys();
            for (Object key : cacheKeys) {
                System.out.println(key + " = " + cache.get(key));
            }
        }
    }

    public static void main(String[] arg) {
        put("A", "AAAAA");
        put("B", "BBBBB");
        put("F", "FFFFF");
        System.out.println(get("F"));
        List keys = getKeys();
        for (Object key : keys) {
            System.out.println(key);
        }
        shutdown();
    }
}