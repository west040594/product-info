package com.tstu.productinfo.service.impl;

import com.tstu.commons.exception.PrsHttpException;
import com.tstu.commons.model.enums.Status;
import com.tstu.productinfo.exception.ProductInfoErrors;
import com.tstu.productinfo.model.Category;
import com.tstu.productinfo.model.Product;
import com.tstu.productinfo.repository.CategoryRepository;
import com.tstu.productinfo.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> findAll() {
        List<Category> categories = categoryRepository.findAll();
        return categories;
    }

    @Override
    public Category findByName(String name) {
        log.info("Поиск категории по наименованию - {}", name);
        Category category = categoryRepository.findByName(name)
                .orElseThrow(() -> new PrsHttpException(ProductInfoErrors.CATEGORY_NOT_FOUND, HttpStatus.NOT_FOUND));;
        log.info("Найденная категория - {}", category);
        return category;
    }

    @Override
    public Category findByAlias(String alias) {
        log.info("Поиск категории по псевдониму - {}", alias);
        Category category = categoryRepository.findByAlias(alias)
                .orElseThrow(() -> new PrsHttpException(ProductInfoErrors.CATEGORY_NOT_FOUND, HttpStatus.NOT_FOUND));;
        log.info("Найденная категория - {}", category);
        return category;
    }

    @Override
    public Category findById(Long id) {
        log.info("Поиск категории по id - {}", id);
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new PrsHttpException(ProductInfoErrors.CATEGORY_NOT_FOUND, HttpStatus.NOT_FOUND));
        log.info("Найденная категория - {}", category);
        return category;
    }

    @Override
    public Category create(Category category) {
        log.info("Запрос на добавление категории - {}", category);
        category.setStatus(Status.ACTIVE);
        Category savedCategory = categoryRepository.save(category);
        log.info("Сохраненная категория - {}", savedCategory);
        return savedCategory;
    }

    @Override
    public Category update(Category product) {
        return categoryRepository.save(product);
    }

    @Override
    public void deleteById(Long id) {
        log.info("Запрос на удаление категории по id - {}", id);
        categoryRepository.deleteById(id);
    }
}
