package com.tstu.productinfo.repository;

import com.tstu.productinfo.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String name);
    boolean existsByAlias(String alias);
    Optional<Category> findByName(String name);
    Optional<Category> findByAlias(String alias);
}
