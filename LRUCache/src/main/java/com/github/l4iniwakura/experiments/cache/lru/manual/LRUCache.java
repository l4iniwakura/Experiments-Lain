package com.github.l4iniwakura.experiments.cache.lru.manual;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Manually written LRUCache with manual deque management
 */
public class LRUCache<K, V> implements Iterable<Map.Entry<K, V>> {

    private final Node<K, V> head;
    private final Node<K, V> tail;
    private final int capacity;
    private final V defaultValue;
    private final Map<K, Node<K, V>> cache;
    private final AtomicInteger counter = new AtomicInteger(0);

    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock readLock = readWriteLock.readLock();
    private final Lock writeLock = readWriteLock.writeLock();

    private final AtomicInteger modificationCount = new AtomicInteger(0);

    public LRUCache(int capacity, V defaultValue) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }

        cache = new ConcurrentHashMap<>(16, 0.75F, Runtime.getRuntime().availableProcessors());
        this.capacity = capacity;
        this.defaultValue = defaultValue;

        head = new Node<>(null, defaultValue);
        tail = new Node<>(null, defaultValue);
        head.next = tail;
        tail.prev = head;
    }

    public V put(K key, V value) {
        writeLock.lock();
        try {
            var node = cache.get(key);
            if (node != null) {
                var oldValue = node.value;
                node.value = value;
                moveToHead(node);
                return oldValue;
            }
            node = new Node<>(key, value);
            cache.put(key, node);
            if (counter.get() >= this.capacity) {
                remove(tail.prev.key);
            } else {
                counter.incrementAndGet();
            }
            addToHead(node);
            return defaultValue;
        } finally {
            writeLock.unlock();
        }
    }

    public V get(K key) {
        writeLock.lock();
        try {
            var node = cache.get(key);
            if (node == null) {
                return defaultValue;
            }
            moveToHead(node);
            return node.value;
        } finally {
            writeLock.unlock();
        }
    }

    public V remove(K key) {
        writeLock.lock();
        try {
            var node = cache.remove(key);
            if (node == null) {
                return defaultValue;
            }
            deleteNode(node);
            counter.decrementAndGet();
            return node.value;
        } finally {
            writeLock.unlock();
        }
    }

    public int size() {
        readLock.lock();
        try {
            return counter.get();
        } finally {
            readLock.unlock();
        }
    }

    public V getFirst() {
        readLock.lock();
        try {
            return head.next.value;
        } finally {
            readLock.unlock();
        }
    }

    public V getLast() {
        readLock.lock();
        try {
            return tail.prev.value;
        } finally {
            readLock.unlock();
        }
    }

    public boolean containsKey(K key) {
        readLock.lock();
        try {
            return cache.containsKey(key);
        } finally {
            readLock.unlock();
        }
    }

    public boolean isEmpty() {
        readLock.lock();
        try {
            return size() == 0;
        } finally {
            readLock.unlock();
        }
    }

    public void clear() {
        writeLock.lock();
        try {
            cache.clear();
            // Восстанавливаем начальное состояние списка
            head.next = tail;
            tail.prev = head;
            counter.set(0);
        } finally {
            writeLock.unlock();
        }
    }

    private void deleteNode(Node<K, V> node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;

        // Защита от утечек памяти
        node.prev = null;
        node.next = null;

        modificationCount.incrementAndGet();
    }

    private void addToHead(Node<K, V> node) {
        node.next = head.next;
        node.next.prev = node;
        node.prev = head;
        head.next = node;

        modificationCount.incrementAndGet();
    }

    private void moveToHead(Node<K, V> node) {
        deleteNode(node);
        addToHead(node);
    }

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        readLock.lock();
        try {
            return new Iterator<>() {
                private Node<K, V> current = head.next;
                private final int currentModificationCount = modificationCount.get();

                @Override
                public boolean hasNext() {
                    return current != null && !current.equals(tail);
                }

                @Override
                public Map.Entry<K, V> next() {
                    if (!hasNext()) {
                        throw new NoSuchElementException();
                    }
                    if (currentModificationCount != modificationCount.get()) {
                        throw new ConcurrentModificationException("Cache modified during iteration");
                    }
                    Map.Entry<K, V> entry = Map.entry(current.key, current.value);
                    current = current.next;
                    return entry;
                }
            };
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public String toString() {
        readLock.lock();
        try {
            var sb = new StringBuilder(size() * 6 - 1 + 2);
            sb.append('[');
            Iterator<Map.Entry<K, V>> it = iterator();
            while (it.hasNext()) {
                var entry = it.next();
                sb.append('{')
                        .append(entry.getKey())
                        .append(':')
                        .append(entry.getValue())
                        .append('}');
                if (it.hasNext()) {
                    sb.append(", ");
                }
            }
            return sb.append(']').toString();
        } finally {
            readLock.unlock();
        }
    }

    private static class Node<K, V> {
        Node<K, V> prev;
        Node<K, V> next;
        private final K key;
        private V value;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
