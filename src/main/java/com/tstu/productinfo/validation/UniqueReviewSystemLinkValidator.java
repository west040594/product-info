package com.tstu.productinfo.validation;

import com.tstu.productinfo.repository.ReviewSystemLinkRepository;
import com.tstu.productinfo.utils.ServiceUtils;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Валидатор на проверку уникальности ссылки продукта сайта отзывов
 * Валидация будет считаться не успешной, если продукт с данной ссылкой уже хранится в БД
 */
@Component
public class UniqueReviewSystemLinkValidator implements ConstraintValidator<UniqueReviewSystemLink, String> {

    private ReviewSystemLinkRepository reviewSystemLinkRepository;

    @Override
    public void initialize(UniqueReviewSystemLink constraintAnnotation) {
        this.reviewSystemLinkRepository = ServiceUtils.getReviewSystemLinkRepository();
    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        return name != null && !reviewSystemLinkRepository.existsByName(name);
    }
}
