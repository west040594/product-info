package com.tstu.productinfo.repository;

import com.tstu.productinfo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByName(String name);
    Optional<Product> findByName(String name);
    List<Product> findByCategory_name(String categoryName);
    List<Product> findByNameIn(List<String> names);
}
