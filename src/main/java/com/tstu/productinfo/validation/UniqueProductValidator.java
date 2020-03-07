package com.tstu.productinfo.validation;


import com.tstu.productinfo.repository.ProductRepository;
import com.tstu.productinfo.utils.ServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Валидатор на проверку уникальности наименования продукта
 * Валидация будет считаться не успешной, если продукт с данным наименованием уже хранится в БД
 */
@Component
public class UniqueProductValidator implements ConstraintValidator<UniqueProduct, String> {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void initialize(UniqueProduct constraintAnnotation) {
        //this.productRepository = ServiceUtils.getProductRepository();
    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        return name != null && !productRepository.existsByName(name);
    }
}
