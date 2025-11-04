package com.github.l4iniwakura.experiments.ac.document.scorer.repository;

import com.github.l4iniwakura.experiments.ac.document.scorer.domain.Document;
import com.github.l4iniwakura.experiments.ac.document.scorer.validation.Contract;

import javax.annotation.concurrent.ThreadSafe;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@ThreadSafe
public class DocumentCopyOnWriteArrayListRepository implements ArrayListRepository<Document> {

    private final List<Document> storage = new CopyOnWriteArrayList<Document>();

    @Override
    public Optional<Document> findById(Integer idx) {
        Contract.requires(idx < storage.size(), "Index shouldn't be out of range");
        Contract.requires(idx > 0, "Index should be grater than zero");

        return Optional.ofNullable(storage.get(idx));
    }

    @Override
    public List<Document> findAll() {
        return List.copyOf(storage);
    }

    @Override
    public void save(Document document) {
        storage.add(document);
    }

    @Override
    public List<Document> getAllSinceIndex(int index) {
        return List.copyOf(storage.subList(index, storage.size()));
    }
}
