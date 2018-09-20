package io.wine.repository;

import io.wine.model.Wine;
import org.springframework.data.repository.CrudRepository;

public interface WineRepository extends CrudRepository<Wine, Integer> {
}
