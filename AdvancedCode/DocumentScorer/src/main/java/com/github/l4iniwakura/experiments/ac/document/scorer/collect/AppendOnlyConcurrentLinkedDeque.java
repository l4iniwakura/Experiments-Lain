package com.github.l4iniwakura.experiments.ac.document.scorer.collect;

import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.Predicate;

public class AppendOnlyConcurrentLinkedDeque<E> extends ConcurrentLinkedDeque<E> {
    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException("remove operation are not permitted");
    }

    @Override
    public E remove() {
        throw new UnsupportedOperationException("remove operation are not permitted");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("removeAll operation are not permitted");
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        throw new UnsupportedOperationException("removeFirstOccurrence operation are not permitted");
    }

    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        throw new UnsupportedOperationException("removeIf operation are not permitted");
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        throw new UnsupportedOperationException("removeLastOccurrence operation are not permitted");
    }

    @Override
    public E removeFirst() {
        throw new UnsupportedOperationException("removeFirst operation are not permitted");
    }

    @Override
    public E removeLast() {
        throw new UnsupportedOperationException("removeLast operation are not permitted");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("clear operation are not permitted");
    }
}
