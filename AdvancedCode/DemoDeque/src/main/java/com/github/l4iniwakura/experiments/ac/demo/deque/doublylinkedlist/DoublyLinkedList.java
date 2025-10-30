package com.github.l4iniwakura.experiments.ac.demo.deque.doublylinkedlist;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DoublyLinkedList<V> {
    private final Node<V> head;
    private final Node<V> tail;
    private final V defaultValue;
    private final int size = 0;

    public DoublyLinkedList(
            V defaultValue
    ) {
        this.defaultValue = defaultValue;
        this.head = new Node<>();
        this.tail = new Node<>();
        this.head.next = null;
        this.head.prev = tail;
        this.tail.next = head;
        this.tail.prev = null;
    }

    public Node<V> add(V val) {
        var newNode = new Node<>(val);
        moveToHead(newNode);
        return newNode;
    }

    public List<Node<V>> getAll() {
        return getSince(this.tail);
    }

    public List<Node<V>> getSince(Node<V> since) {
        var result = new ArrayList<Node<V>>(this.size);
        var cur = since.next;
        while (!Objects.equals(cur, this.head)) {
            result.add(cur);
            cur = cur.next;
        }
        return result;
    }

    private void moveToHead(Node<V> node) {
        var curPrev = this.head.prev;

        curPrev.next = node;
        node.prev = curPrev;
        this.head.prev = node;
        node.next = this.head;
    }

    public class Node<T extends V> {
        Node<V> next;
        Node<V> prev;
        V val;

        public Node() {
            this.val = defaultValue;
        }

        public Node(V val) {
            this.val = val;
        }

        public V getVal() {
            return this.val;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "val=" + val +
                    '}';
        }
    }
}
