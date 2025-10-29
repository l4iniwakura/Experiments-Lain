package com.github.l4iniwakura.experiments.cache.lru.manual;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LRUCacheTest {

    private final static Integer DEFAULT_VALUE = -1;

    private LRUCache<Integer, Integer> lruCache;

    @Test
    void putShouldReturnInsertedValuesCorrectly() {
        lruCache = new LRUCache<>(2, DEFAULT_VALUE);
        lruCache.put(1, 1);
        lruCache.put(2, 2);
        assertEquals(2, lruCache.get(2));
        assertEquals(1, lruCache.get(1));
    }

    @Test
    void cacheEvictionShouldWorkCorrectly() {
        lruCache = new LRUCache<>(2, DEFAULT_VALUE);
        lruCache.put(1, 1);
        lruCache.put(2, 2);
        lruCache.put(3, 3);
        assertEquals(DEFAULT_VALUE, lruCache.get(1));
        assertEquals(2, lruCache.get(2));
        assertEquals(3, lruCache.get(3));
    }

    @Test
    void repeatedPutShouldReplaceValueToNew() {
        lruCache = new LRUCache<>(2, DEFAULT_VALUE);
        lruCache.put(1, 1);
        lruCache.put(1, 2);
        lruCache.put(2, 2);
        assertEquals(2, lruCache.get(1));
        assertEquals(2, lruCache.get(2));
    }

    @Test
    void putShouldReturnDefaultValueIfValueInsertedFirstTime() {
        lruCache = new LRUCache<>(2, DEFAULT_VALUE);
        var prev = lruCache.put(1, 1);
        assertEquals(DEFAULT_VALUE, prev);
    }

    @Test
    void get() {

    }

    @Test
    void removeShouldRemoveItemsCorrectly() {
        lruCache = new LRUCache<>(3, DEFAULT_VALUE);
        lruCache.put(1, 1);
        lruCache.put(2, 2);
        lruCache.put(3, 3);
        var removed1 = lruCache.remove(1);
        var removed2 = lruCache.remove(2);
        assertEquals(DEFAULT_VALUE, lruCache.get(1));
        assertEquals(DEFAULT_VALUE, lruCache.get(2));
        assertNotEquals(removed1, lruCache.get(1));
        assertNotEquals(removed2, lruCache.get(2));
    }

    @Test
    void removeOnNonExistingItemShouldReturnDefaultValue() {
        lruCache = new LRUCache<>(2, DEFAULT_VALUE);
        lruCache.put(1, 1);
        lruCache.remove(1);
        var remove1 = lruCache.remove(1);
        var remove2 = lruCache.remove(10);
        assertEquals(DEFAULT_VALUE, remove1);
        assertEquals(DEFAULT_VALUE, remove2);
    }

    @Test
    void removeShouldReturnRemovedItemsValue() {
        lruCache = new LRUCache<>(2, DEFAULT_VALUE);
        lruCache.put(1, 1);
        var removedValue = lruCache.remove(1);
        assertEquals(1, removedValue);
        assertNotEquals(DEFAULT_VALUE, removedValue);
    }

    @Test
    void sizeShouldReturnZero_whenCallWithoutCalledPutMethod() {
        lruCache = new LRUCache<>(1, DEFAULT_VALUE);
        assertEquals(0, lruCache.size());
    }

    @Test
    void containsKeyShouldReturnTrueIfItemWithThatKeyArePresentInCache() {
        lruCache = new LRUCache<>(2, DEFAULT_VALUE);
        lruCache.put(1, 1);
        assertTrue(lruCache.containsKey(1));
    }

    @Test
    void containsKeyShouldReturnFalseIfItemWithThatKeyAreNotPresentInCache() {
        lruCache = new LRUCache<>(2, DEFAULT_VALUE);
        assertFalse(lruCache.containsKey(1));
    }

    @Test
    void isEmpty_shouldReturnFalseIfCacheHasAtLeastOneItemInside() {
        lruCache = new LRUCache<>(2, DEFAULT_VALUE);
        lruCache.put(1, 1);
        assertFalse(lruCache.isEmpty());
    }

    @Test
    void isEmpty_shouldReturnTrueIfCacheHasNoOneItemInside() {
        lruCache = new LRUCache<>(2, DEFAULT_VALUE);
        assertTrue(lruCache.isEmpty());
    }

    @Test
    void clear_shouldMakeCacheIsEmpty() {
        lruCache = new LRUCache<>(2, DEFAULT_VALUE);
        lruCache.put(1, 1);
        lruCache.clear();
        assertTrue(lruCache.isEmpty());
        assertEquals(0, lruCache.size());
    }

    @Test
    void sizeShouldReturnCorrectNumberOfElementsWhenInvoked() {
        lruCache = new LRUCache<>(10, DEFAULT_VALUE);
        lruCache.put(1, 1);
        lruCache.put(2, 2);
        lruCache.put(3, 3);
        lruCache.put(3, 3);
        lruCache.put(2, 2);
        assertEquals(3, lruCache.size());
    }

    @Test
    void getFirst_shouldReturnDefaultValue_whenCacheJustCreated() {
        var defaultVal = -1;
        lruCache = new LRUCache<>(1, defaultVal);
        assertEquals(defaultVal, lruCache.getFirst());
    }

    @Test
    void getLast_shouldReturnDefaultValue_whenCacheJustCreated() {
        var defaultVal = -1;
        lruCache = new LRUCache<>(1, defaultVal);
        assertEquals(defaultVal, lruCache.getLast());
    }
}