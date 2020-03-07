package com.tstu.productinfo.service.impl;

import com.tstu.commons.model.enums.QualityType;
import com.tstu.commons.model.enums.Status;
import com.tstu.productinfo.model.Quality;
import com.tstu.productinfo.repository.QualityRepository;
import com.tstu.productinfo.service.QualityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class QualityServiceImpl implements QualityService {

    private final QualityRepository qualityRepository;

    @Override
    public Optional<Quality> create(Quality quality) {
        if(!qualityRepository.existsByNameAndType(quality.getName(), quality.getType())) {
            quality.setStatus(Status.ACTIVE);
            return Optional.of(qualityRepository.save(quality));
        }
        return Optional.empty();
    }

    @Override
    public Optional<Quality> find(String name, QualityType qualityType) {
        return qualityRepository.findByNameAndType(name, qualityType);
    }
}
