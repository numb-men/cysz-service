package cn.hengyumo.humor.base.cache;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * EasyCache
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/8/2
 */
@Component
public class EasyCache<T> {

    private Map<String, T> cache = new ConcurrentHashMap<>();

    public T get(String key) {
        return cache.get(key);
    }

    public void put(String key, T value) {
        cache.put(key, value);
    }

    public void delete(String key) {
        cache.remove(key);
    }

    public void clear() {
        cache.clear();
    }
}
