package com.github.l4iniwakura.experiments.ac.document.scorer.fixture;

import com.github.l4iniwakura.experiments.ac.document.scorer.domain.Document;
import com.github.l4iniwakura.experiments.ac.document.scorer.domain.User;
import com.github.l4iniwakura.experiments.ac.document.scorer.service.RecommenderService;
import com.github.l4iniwakura.experiments.ac.document.scorer.service.TRecommenderService;

import static com.github.l4iniwakura.experiments.ac.document.scorer.fixture.ScorerFixture.idScorer;
import static com.github.l4iniwakura.experiments.ac.document.scorer.fixture.ScorerFixture.randomScorer;

public class RecommenderServiceFixture {
    public static TRecommenderService<Document, User> randomRecommenderService() {
        return new RecommenderService(randomScorer());
    }

    public static TRecommenderService<Document, User> idRecommenderService() {
        return new RecommenderService(idScorer());
    }
}
