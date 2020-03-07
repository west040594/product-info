package com.tstu.productinfo.service;

import com.tstu.commons.dto.rabbit.request.ProductReviewParseRequest;
import com.tstu.productinfo.model.Product;
import com.tstu.productinfo.model.ReviewSystemLink;

import java.util.List;

public interface ParseRequestService {



    /**
     * Выполнить запрос в review-dom-parse сервис
     * Алгоритм:
     * 1. Сформировать список запросов {@link ParseRequestService#formParseRequests(Product)}
     * 2. Отправить запросы {@link ParseRequestService#sendParseRequests(List)}
     * @param product Новый добавляемый продукт
     */
    void execute(Product product);

    /**
     * Отправить список запросов в сервис парсинга системы отзывов(review-dom-parser)
     * Алгоритм:
     * 1. Обернуть каждый объект в коллекции ProductReviewParseRequest в RabbitClientRequest(структура сообщения для передачи в очередь)
     * 2. Отправить все запросы в очередь для обработки в системы парсинга отзывов
     * @param requests Списка запросов в парсинг систему(review-dom-parser)
     */
    void sendParseRequests(List<ProductReviewParseRequest> requests);

    /**
     * Формирование списка запросов в сервис парсинга системы отзывов(review-dom-parser)
     * Алгоритм:
     * 1. Взять все ссылки(url) систем парсинг отзывов из добавляемого продукта
     * 2. Установить для этих ссылок данный продукт
     * 3. Сформировать для каждой ссылки запрос в систему парсинг отзывов {@link ParseRequestService#createParseRequest(ReviewSystemLink)}
     * @param product Новый добавляемый продукт
     * @return Коллекция запросов в сервис парсинга системы отзывов(review-dom-parser)
     */
    List<ProductReviewParseRequest> formParseRequests(Product product);

    /**
     * Создание структуру запроса в сервис парсинга систмы отзывов(review-dom-parser)
     * На основе ссылки(url) сайта системы отзывов формируется объект запроса в очередь ProductReviewParseRequest
     * @param reviewSystemLink Ссылка на сайт систимы отзывов с информацией о данном продукте
     * @return Структура запроса в в парсинг сервис
     */
    ProductReviewParseRequest createParseRequest(ReviewSystemLink reviewSystemLink);
}
