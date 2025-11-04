package com.github.l4iniwakura.experiments.ac.document.scorer.service;

public interface TScorer<Document, User> {
    double getScore(Document doc, User user);
}