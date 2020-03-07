package com.tstu.productinfo.service.impl;

import com.tstu.commons.dto.rabbit.request.ProductReviewParseRequest;
import com.tstu.commons.exception.PrsHttpException;
import com.tstu.commons.model.enums.Status;
import com.tstu.productinfo.exception.ProductInfoErrors;
import com.tstu.productinfo.model.Category;
import com.tstu.productinfo.model.Product;
import com.tstu.productinfo.repository.ProductRepository;
import com.tstu.productinfo.service.ParseRequestService;
import com.tstu.productinfo.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ParseRequestService parseRequestService;

    @Override
    public List<Product> findAll() {
        List<Product> products = productRepository.findAll();
        return products;
    }

    @Override
    public List<Product> findAllByNames(List<String> names) {
        log.info("Поиск продуктов по наименованиям - {}", names);
        List<Product> products = productRepository.findByNameIn(names);
        log.info("Найденные продукты - {}", products);
        return products;
    }

    @Override
    public Product findById(Long id) {
        log.info("Поиск продукта по id - {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new PrsHttpException(ProductInfoErrors.PRODUCT_NOT_FOUND, HttpStatus.NOT_FOUND));
        log.info("Найденный продукт - {}", product);
        return product;
    }

    @Override
    public List<Product> findByCategoryName(String categoryName) {
        log.info("Поиск продукта по категории - {}", categoryName);
        List<Product> productsByCategory = productRepository.findByCategory_name(categoryName);
        log.info("Продукты найдены - {}", productsByCategory.stream().map(Product::getName).collect(Collectors.toList()));
        return productsByCategory;
    }

    @Override
    public Product create(Product product) {
        log.info("Запрос на добавление продукта - {}", product);
        product.setStatus(Status.ACTIVE);
        product.setFill(false);
        product.setLastModifiedBy(product.getCreatedBy());
        Product savedProduct = productRepository.save(product);
        parseRequestService.execute(product);
        log.info("Сохраненный продукт - {}", savedProduct);
        return savedProduct;
    }

    @Override
    public Product update(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void deleteById(Long id) {
        log.info("Запрос на удаление продукта по id - {}", id);
        productRepository.deleteById(id);
    }
}
