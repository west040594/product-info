package com.tstu.productinfo.service;

import com.tstu.commons.dto.rabbit.response.RabbitClientResponse;

public interface ParseResponseProcessingService {

    /**
     * Обработка входящего ответа на заполнения информации о продукте из очереди
     * @param response Ответ из очереди. содержащий информацию о продукте из системы отзывов
     */
    void processParseResponse(RabbitClientResponse response);
}
