package com.github.l4iniwakura.experiments.cache.lru;

public interface Cache<K, V> {
    V put(K key, V value);
    V get(K key);
    V remove(Object key);
    int size();
}
