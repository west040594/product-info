package com.tstu.productinfo.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import com.tstu.commons.dto.rabbit.response.ProductParseResponse;
import com.tstu.commons.dto.rabbit.response.RabbitClientResponse;
import com.tstu.commons.model.enums.QualityType;
import com.tstu.commons.model.enums.ReviewSystem;
import com.tstu.productinfo.mapping.ReviewMapper;
import com.tstu.productinfo.model.Product;
import com.tstu.productinfo.model.Quality;
import com.tstu.productinfo.model.Review;
import com.tstu.productinfo.service.ParseResponseProcessingService;
import com.tstu.productinfo.service.ProductService;
import com.tstu.productinfo.service.QualityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ParseResponseProcessingServiceImpl implements ParseResponseProcessingService {

    private final ProductService productService;
    private final QualityService qualityService;
    private final ReviewMapper reviewMapper;
    private final ObjectMapper objectMapper;

    public ParseResponseProcessingServiceImpl(@Lazy ProductService productService,
                                              ReviewMapper reviewMapper,
                                              ObjectMapper objectMapper,
                                              QualityService qualityService) {
        this.productService = productService;
        this.reviewMapper = reviewMapper;
        this.objectMapper = objectMapper;
        this.qualityService = qualityService;
    }


    @Override
    public void processParseResponse(RabbitClientResponse response) {
        if(Objects.nonNull(response.getError())) {
            log.info("Неудачная попытка получения отзывов - {}", response.getError());
        } else {
            ProductParseResponse productParseResponse = objectMapper.convertValue(response.getData(), ProductParseResponse.class);
            this.processParseResponse(productParseResponse);
        }
    }


    /**
     * Обновление продукта в бд по данным полученным из структуры ответа парсинг системы
     * Алгоритм:
     * 1. Найти продукт по его id
     * 2. Усановить значение fill - true. Что означает что продукт полностью обновлен и заполненен
     * 3. Установить изображение
     * 4. Установить рейтинг
     * 5. Сохранить все его качества
     * 6. Установить все отзывы для данного продукта
     * 7. Внести изменения
     * @param response Структура ответа парсинг сервиса
     */
    private void processParseResponse(ProductParseResponse response) {
        log.info("Получена информация о продукте из review-dom-parser. Отзывы - {}. Рейтинг - {}", response.getReviews().size(), response.getRating());
        Product product = productService.findById(response.getProductId());
        product.setFill(true);
        product.setImageUrl(response.getImageUrl());
        product.setRating(Double.parseDouble(response.getRating()));
        saveAllQualities(response);
        product.setReviews(formReviews(response));
        Product updatedProduct = productService.update(product);
        log.info("Вносим изменение  в продукт - {}", updatedProduct);
    }

    /**
     * Соханение всех качеств товара(плюсы и минусы)
     * Алгоритм:
     * 1. Объедиение двух колекций(множеств) в одну
     * 2. Сохранение всех каечств в базу
     * @param response Структура ответа парсинг сервиса
     */
    private void saveAllQualities(ProductParseResponse response) {
        response.getReviews().stream()
                .map(review -> Sets.union (
                        review.getMinuses().stream().map(minus -> new Quality(minus, QualityType.MINUS)).collect(Collectors.toSet()),
                        review.getPluses().stream().map(minus -> new Quality(minus, QualityType.PLUS)).collect(Collectors.toSet()))
                )
                .flatMap(Collection::stream)
                .collect(Collectors.toSet())
                .forEach(qualityService::create);
    }

    /**
     * Формирование коллекции ревью из ответа парсинг сервиса
     * Алгоритм:
     * 1. Взять все ревью, полученные из системы отзывов
     * 2. Спамить их в формат ревью хранимый бд
     * 3. Установить для каждого ревью систему из которой был получен отзыв
     * @param response Структура ответа парсинг сервиса
     * @return Множество ревью
     */
    private Set<Review> formReviews(ProductParseResponse response) {
        Set<Review> reviews = response.getReviews().stream()
                .map(reviewParseResponse -> reviewMapper.reviewParseResponseToReview(reviewParseResponse, qualityService))
                .collect(Collectors.toSet());

        reviews.forEach(review -> review.setReviewSystem(ReviewSystem.fromValue(response.getReviewSystem())));
        return reviews;
    }
}
