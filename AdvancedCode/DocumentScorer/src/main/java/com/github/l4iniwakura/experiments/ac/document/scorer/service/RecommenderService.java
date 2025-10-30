package com.github.l4iniwakura.experiments.ac.document.scorer.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.l4iniwakura.experiments.ac.document.scorer.cache.CacheEntry;
import com.github.l4iniwakura.experiments.ac.document.scorer.domain.Document;
import com.github.l4iniwakura.experiments.ac.document.scorer.domain.User;
import com.github.l4iniwakura.experiments.ac.document.scorer.domain.WeighedEntity;
import com.github.l4iniwakura.experiments.ac.document.scorer.repository.ArrayListRepository;
import com.github.l4iniwakura.experiments.ac.document.scorer.repository.DocumentCopyOnWriteArrayListRepository;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class RecommenderService implements TRecommenderService<Document, User> {

    private final TScorer<Document, User> scorer;
    private final ArrayListRepository<Document> documentRepository = new DocumentCopyOnWriteArrayListRepository();
    private final Cache<UUID, CacheEntry<UserScoredDocumentsCache>> cache = Caffeine.newBuilder()
            .maximumSize(10_000)
            .build();

    public RecommenderService(
            final TScorer<Document, User> documentScorer
    ) {
        this.scorer = documentScorer;
    }

    @Override
    public List<Document> getTop(User user, int limit) {
        return documentRepository.getAllSinceIndex(0)
                .stream()
                .map(document -> new WeighedEntity<>(document, scorer.getScore(document, user)))
                .sorted()
                .limit(limit)
                .map(WeighedEntity::entity)
                .toList();
    }

    @Override
    public void addDocument(Document document) {
        documentRepository.save(document);
    }

    private static class UserScoredDocumentsCache {
        private final BlockingQueue<WeighedEntity<Document, Double>> cache = new PriorityBlockingQueue<>();
        private int lastIndex = 0;

        public BlockingQueue<WeighedEntity<Document, Double>> getCache() {
            return cache;
        }

        public int getLastIndex() {
            return lastIndex;
        }

        public void setLastIndex(int lastIndex) {
            this.lastIndex = lastIndex;
        }
    }
}
