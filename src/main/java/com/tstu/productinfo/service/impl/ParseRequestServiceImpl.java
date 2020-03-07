package com.tstu.productinfo.service.impl;

import com.tstu.commons.dto.rabbit.request.ProductReviewParseRequest;
import com.tstu.commons.dto.rabbit.request.RabbitClientRequest;
import com.tstu.productinfo.model.Product;
import com.tstu.productinfo.model.ReviewSystemLink;
import com.tstu.productinfo.service.AmqpClient;
import com.tstu.productinfo.service.ParseRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ParseRequestServiceImpl implements ParseRequestService {

    private final AmqpClient amqpClient;

    @Override
    public void execute(Product product) {
        List<ProductReviewParseRequest> parseRequests = this.formParseRequests(product);
        this.sendParseRequests(parseRequests);
    }

    @Override
    public void sendParseRequests(List<ProductReviewParseRequest> requests) {
        requests.stream()
                .map(productRequest -> new RabbitClientRequest(Instant.now(), productRequest.getReviewSystem(), productRequest))
                .forEach(amqpClient::send);
    }

    @Override
    public List<ProductReviewParseRequest> formParseRequests(Product product) {

        product.getReviewSystemLinks()
                .forEach(reviewSystemLink -> reviewSystemLink.setProduct(product));

       return product.getReviewSystemLinks().stream()
                .map(this::createParseRequest)
                .collect(Collectors.toList());
    }

    @Override
    public ProductReviewParseRequest createParseRequest(ReviewSystemLink reviewSystemLink) {
        return ProductReviewParseRequest.builder()
                .productId(reviewSystemLink.getProduct().getId())
                .reviewSystem(reviewSystemLink.getReviewSystem().getValue())
                .url(reviewSystemLink.getName())
                .build();
    }
}
