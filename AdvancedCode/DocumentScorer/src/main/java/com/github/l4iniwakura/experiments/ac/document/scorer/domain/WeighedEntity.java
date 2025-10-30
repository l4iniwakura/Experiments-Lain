package com.github.l4iniwakura.experiments.ac.document.scorer.domain;

public record WeighedEntity<ENTITY, WEIGHT extends Number>(
        ENTITY entity,
        WEIGHT score
) implements Comparable<WeighedEntity<ENTITY, WEIGHT>> {
    @Override
    public int compareTo(WeighedEntity<ENTITY, WEIGHT> o) {
        return Double.compare(this.score.doubleValue(), o.score.doubleValue());
    }
}
