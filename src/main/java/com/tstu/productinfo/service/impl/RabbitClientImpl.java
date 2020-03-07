package com.tstu.productinfo.service.impl;

import com.tstu.commons.dto.rabbit.request.RabbitClientRequest;
import com.tstu.commons.dto.rabbit.response.RabbitClientResponse;
import com.tstu.productinfo.service.AmqpClient;
import com.tstu.productinfo.service.ParseResponseProcessingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RabbitClientImpl implements AmqpClient {

    private final AmqpTemplate amqpTemplate;
    private final ParseResponseProcessingService parseProcessingService;

    @Value("${rabbit.parser.exchange}")
    private String requestExchange;

    @Override
    @RabbitListener(queues = "${rabbit.parser.inQueue}")
    public void receive(RabbitClientResponse response) {
        log.info("Получение сообщения");
        parseProcessingService.processParseResponse(response);
    }

    @Override
    public void send(RabbitClientRequest request) {
        log.info("Отправка сообщения в {}. Пейлоад - {}", requestExchange, request);
        amqpTemplate.convertAndSend(requestExchange, "RQ", request);
    }
}
