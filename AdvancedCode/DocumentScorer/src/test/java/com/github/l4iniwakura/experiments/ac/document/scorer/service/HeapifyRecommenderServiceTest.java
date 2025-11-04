package com.github.l4iniwakura.experiments.ac.document.scorer.service;

import com.github.l4iniwakura.experiments.ac.document.scorer.domain.Document;
import com.github.l4iniwakura.experiments.ac.document.scorer.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.github.l4iniwakura.experiments.ac.document.scorer.fixture.ScorerFixture.idScorer;

class HeapifyRecommenderServiceTest {

    private TRecommenderService<Document, User> recommenderService = new HeapifyRecommenderService(idScorer());

    @Test
    void addedDocumentShouldBeReturnedBack() {
        var expected = new Document(1);
        recommenderService.addDocument(expected);
        var top = recommenderService.getTop(new User(UUID.randomUUID()), 1);
        Assertions.assertEquals(expected, top.getFirst());
    }
}