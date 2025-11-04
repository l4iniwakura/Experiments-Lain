package com.github.l4iniwakura.experiments.ac.document.scorer.collect;

import javax.annotation.concurrent.ThreadSafe;
import java.util.concurrent.CopyOnWriteArrayList;

@ThreadSafe
public class AppendOnlyCopyOnWriteArrayList<E> extends CopyOnWriteArrayList<E> {
    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException("Remove not allowed");
    }

    @Override
    public E set(int index, E element) {
        throw new UnsupportedOperationException("Set not allowed");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Clear not allowed");
    }
}
