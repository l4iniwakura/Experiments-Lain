package com.github.l4iniwakura.experiments.ac.document.scorer.repository;

import java.util.List;
import java.util.Optional;

public interface Repository<ID, ENTITY> {
    Optional<ENTITY> findById(ID idx);

    List<ENTITY> findAll();

    void save(ENTITY entity);
}
