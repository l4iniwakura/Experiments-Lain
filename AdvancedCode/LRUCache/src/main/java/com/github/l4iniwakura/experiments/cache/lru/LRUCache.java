package com.github.l4iniwakura.experiments.cache.lru;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/*
  LinkedHashMap based LRUCache.
  All operations O(1)
  Not thread safe
 */
public class LRUCache<K, V> implements Cache<K, V>, Map<K, V> {

    private final int capacity;
    private final Map<K, V> cache;

    public LRUCache(int capacity) {
        this.cache = new LinkedHashMap<>(16, 0.75F, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > capacity;
            }
        };
        this.capacity = capacity;
    }

    @Override
    public V put(K key, V value) {
        return cache.put(key, value);
    }

    @Override
    public V remove(Object key) {
        return cache.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        cache.putAll(m);
    }

    @Override
    public void clear() {
        cache.clear();
    }

    @Override
    public Set<K> keySet() {
        return cache.keySet();
    }

    @Override
    public Collection<V> values() {
        return cache.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return cache.entrySet();
    }

    @Override
    public int size() {
        return cache.size();
    }

    @Override
    public boolean isEmpty() {
        return cache.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return cache.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return cache.containsValue(value);
    }

    @Override
    public V get(Object key) {
        return cache.get(key);
    }

    public int getCapacity() {
        return capacity;
    }

    public Map<K, V> snapshot() {
        return Map.copyOf(this.cache);
    }
}
