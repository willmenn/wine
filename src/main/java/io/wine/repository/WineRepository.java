package io.wine.repository;

import io.wine.model.Wine;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface WineRepository extends PagingAndSortingRepository<Wine, Integer> {
}
