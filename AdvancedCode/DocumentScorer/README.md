Нам дана функция которая оценивает релевантность документа пользователю

```java
interface Scorer<Document, User> {
    double getScore(Document doc, User user);
}
```

Необходимо реализовать сервис, который может сохранять документ и получать топ К (limit) документов для пользователя по скору этой функции
```java
interface RecommenderService<Document, User> {
    List<Document> getTop(User user, int limit);

    void addDocument(Document document);
}
```

Примерное решение

```java
package com.github.l4iniwakura.experiments.ac.document.scorer.service;

import com.github.l4iniwakura.experiments.ac.document.scorer.collect.AppendOnlyCopyOnWriteArrayList;
import com.github.l4iniwakura.experiments.ac.document.scorer.domain.Document;
import com.github.l4iniwakura.experiments.ac.document.scorer.domain.User;
import com.github.l4iniwakura.experiments.ac.document.scorer.domain.WeighedEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class HeapifyRecommenderService implements TRecommenderService<Document, User> {
    private final List<Document> storage;
    private final TScorer<Document, User> scorer;

    public HeapifyRecommenderService(TScorer<Document, User> scorer) {
        this.storage = new AppendOnlyCopyOnWriteArrayList<>();
        this.scorer = scorer;
    }

    @Override
    public List<Document> getTop(User user, int limit) {
        int size = storage.size();
        var heapify = storage.subList(0, Math.min(limit, size))
                .stream()
                .map(document -> new WeighedEntity<>(document, scorer.getScore(document, user)))
                .toList();
        var heap = new PriorityQueue<>(heapify);
        if (limit < size) {
            var toHeapify = storage.subList(limit, size);
            for (Document document : toHeapify) {
                var score = scorer.getScore(document, user);
                if (score > heap.element().score()) {
                    heap.poll();
                    heap.add(new WeighedEntity<>(document, score));
                }
            }
        }
        var result = new ArrayList<WeighedEntity<Document, Double>>(heap.size());
        for (int i = 0; i < heap.size(); i++) {
            result.add(heap.poll());
        }
        return result.stream()
                .map(WeighedEntity::entity)
                .toList()
                .reversed();
    }

    @Override
    public void addDocument(Document document) {
        storage.add(document);
    }
}

```