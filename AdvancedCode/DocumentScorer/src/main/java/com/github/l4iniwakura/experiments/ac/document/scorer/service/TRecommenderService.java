package com.github.l4iniwakura.experiments.ac.document.scorer.service;

import java.util.List;

public interface TRecommenderService<Document, User> {
    List<Document> getTop(User user, int limit);

    void addDocument(Document document);
}
