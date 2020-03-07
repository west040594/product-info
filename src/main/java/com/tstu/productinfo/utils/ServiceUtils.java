package com.tstu.productinfo.utils;

import com.tstu.productinfo.repository.ProductRepository;
import com.tstu.productinfo.repository.ReviewSystemLinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Вспомогательный сервис для получения кокретных бинов. Используется в валидаторах
 * {@link com.tstu.productinfo.validation.UniqueProductValidator}
 * {@link com.tstu.productinfo.validation.UniqueReviewSystemLinkValidator}
 */
@Component
public class ServiceUtils {

    private static ServiceUtils instance;

    private ApplicationContext applicationContext;

    @PostConstruct
    public void fillInstance() {
        instance = this;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Autowired
    public ServiceUtils(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public static ReviewSystemLinkRepository getReviewSystemLinkRepository() {
        return instance.applicationContext.getBean(ReviewSystemLinkRepository.class);
    }

    public static ProductRepository getProductRepository() {
        return instance.applicationContext.getBean(ProductRepository.class);
    }
}
