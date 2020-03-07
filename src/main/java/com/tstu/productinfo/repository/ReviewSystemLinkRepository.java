package com.tstu.productinfo.repository;

import com.tstu.productinfo.model.ReviewSystemLink;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewSystemLinkRepository extends JpaRepository<ReviewSystemLink, Long> {
    boolean existsByName(String name);
}
