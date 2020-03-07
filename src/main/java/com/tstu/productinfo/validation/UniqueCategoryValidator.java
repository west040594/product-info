package com.tstu.productinfo.validation;

import com.tstu.commons.dto.http.request.productinfo.CategoryDataRequest;
import com.tstu.productinfo.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Валидатор на проверку уникальности наименования и псевдонима
 * Валидация будет считаться не успешной, если категория с данным наименованием или псвевдонимом уже хранится в БД
 */
@Component
public class UniqueCategoryValidator implements ConstraintValidator<UniqueCategory, CategoryDataRequest> {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public void initialize(UniqueCategory constraintAnnotation) {

    }

    @Override
    public boolean isValid(CategoryDataRequest request, ConstraintValidatorContext context) {
        return (request.getName() != null && !categoryRepository.existsByName(request.getName()))
                &&
                (request.getAlias() != null && !categoryRepository.existsByAlias(request.getAlias()));
    }
}
