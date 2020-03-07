package com.tstu.productinfo.validation;

import com.tstu.productinfo.exception.ProductInfoExceptionMessage;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Documented
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueReviewSystemLinkValidator.class)
public @interface UniqueReviewSystemLink {
    String message() default ProductInfoExceptionMessage.REVIEW_SYSTEM_LINK_IS_ALREADY_IN_USE_MSG;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
