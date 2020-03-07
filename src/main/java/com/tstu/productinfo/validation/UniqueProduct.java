package com.tstu.productinfo.validation;

import com.tstu.productinfo.exception.ProductInfoExceptionMessage;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.Payload;
import java.io.Serializable;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;


@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Constraint(validatedBy = UniqueProductValidator.class)
public @interface UniqueProduct {
    String message() default ProductInfoExceptionMessage.PRODUCT_IS_ALREADY_IN_USE_MSG;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
