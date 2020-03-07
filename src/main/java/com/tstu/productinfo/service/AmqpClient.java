package com.tstu.productinfo.service;

import com.tstu.commons.dto.rabbit.request.RabbitClientRequest;
import com.tstu.commons.dto.rabbit.response.RabbitClientResponse;
import org.springframework.messaging.Message;

public interface AmqpClient {
    /**
     * Получение сообщения из очереди RabbitMQ
     * @param response Стуктура ответа из очереди
     */
    void receive(RabbitClientResponse response);


    /**
     * Отправлене сообщения в очередь RabbitMQ
     * @param request Структура запроса в очередь
     */
    void send(RabbitClientRequest request);
}
