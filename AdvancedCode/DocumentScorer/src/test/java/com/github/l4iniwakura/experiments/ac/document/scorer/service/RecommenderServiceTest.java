package com.github.l4iniwakura.experiments.ac.document.scorer.service;

import com.github.l4iniwakura.experiments.ac.document.scorer.domain.Document;
import com.github.l4iniwakura.experiments.ac.document.scorer.domain.User;
import org.junit.jupiter.api.Test;

import static com.github.l4iniwakura.experiments.ac.document.scorer.fixture.RecommenderServiceFixture.idRecommenderService;

class RecommenderServiceTest {

    private TRecommenderService<Document, User> recommenderService;

    @Test
    void addDocumentShouldSaveDocumentInside() {
        recommenderService = idRecommenderService();
        recommenderService.addDocument(new Document(1));

    }
}