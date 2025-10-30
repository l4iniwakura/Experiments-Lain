package com.github.l4iniwakura.experiments.ac.document.scorer.repository;

import java.util.List;

/**
 * ID - index in array list
 *
 * @param <ENTITY>
 */
public interface ArrayListRepository<ENTITY> extends Repository<Integer, ENTITY> {

    List<ENTITY> getAllSinceIndex(int index);

}
