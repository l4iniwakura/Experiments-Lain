package com.github.l4iniwakura.experiments.ac.document.scorer.validation;

public final class Contract {
    public static void requires(boolean condition, String message) {
        if (!condition) {
            throw new IllegalArgumentException(message);
        }
    }
}
