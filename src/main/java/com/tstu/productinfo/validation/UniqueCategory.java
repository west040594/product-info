package com.tstu.productinfo.validation;

import com.tstu.productinfo.exception.ProductInfoExceptionMessage;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ TYPE })
@Constraint(validatedBy = UniqueCategoryValidator.class)
public @interface UniqueCategory {
    String message() default ProductInfoExceptionMessage.CATEGORY_IS_ALREADY_IN_USE_MSG;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
