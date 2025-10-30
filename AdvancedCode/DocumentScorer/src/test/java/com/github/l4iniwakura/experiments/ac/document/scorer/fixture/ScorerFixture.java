package com.github.l4iniwakura.experiments.ac.document.scorer.fixture;

import com.github.l4iniwakura.experiments.ac.document.scorer.domain.Document;
import com.github.l4iniwakura.experiments.ac.document.scorer.domain.User;
import com.github.l4iniwakura.experiments.ac.document.scorer.service.TScorer;

public class ScorerFixture {
    public static TScorer<Document, User> randomScorer() {
        return (_, _) -> Math.random() * 1000;
    }

    public static TScorer<Document, User> idScorer() {
        return (document, _) -> document.id();
    }
}
