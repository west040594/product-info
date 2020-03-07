package com.tstu.productinfo.repository;

import com.tstu.commons.model.enums.QualityType;
import com.tstu.productinfo.model.Quality;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QualityRepository extends JpaRepository<Quality, Long> {
    Optional<Quality> findByNameAndType(String name, QualityType qualityType);
    boolean existsByNameAndType(String name, QualityType qualityType);
}
