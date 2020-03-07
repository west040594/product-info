package com.tstu.productinfo.exception;

import com.tstu.commons.exception.PrsErrorCode;

import java.util.Arrays;
import java.util.Optional;

/**
 * Перечисление всех возможных ошибок в product-info-service
 */
public enum ProductInfoErrors implements PrsErrorCode {

    EXPIRED_OR_INVALID_JWT_TOKEN(1, ProductInfoExceptionMessage.EXPIRED_OR_INVALID_JWT_TOKEN_MSG),
    PRODUCT_NOT_FOUND(2, ProductInfoExceptionMessage.PRODUCT_NOT_FOUND_MSG),
    JSON_NOT_READABLE(3, ProductInfoExceptionMessage.JSON_NOT_READABLE),
    ACCESS_DENIED(4, ProductInfoExceptionMessage.ACCESS_DENIED_MSG),
    PRODUCT_IS_ALREADY_IN_USE(5, ProductInfoExceptionMessage.PRODUCT_IS_ALREADY_IN_USE_MSG),
    UNSUPPORTED_REVIEW_SYSTEM(6, ProductInfoExceptionMessage.UNSUPPORTED_REVIEW_SYSTEM_MSG),
    REVIEW_SYSTEM_LINK_IS_ALREADY_IN_USE(7, ProductInfoExceptionMessage.REVIEW_SYSTEM_LINK_IS_ALREADY_IN_USE_MSG),
    CATEGORY_NOT_FOUND(8, ProductInfoExceptionMessage.CATEGORY_NOT_FOUND_MSG);

    private Integer errorCode;
    private String errorDescription;

    ProductInfoErrors(Integer errorCode, String errorDescription) {
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public static Optional<ProductInfoErrors> getByDescription(String errorDescription) {
        return Arrays.stream(values())
                .filter(productInfoErrors -> productInfoErrors.errorDescription.equals(errorDescription))
                .findFirst();
    }
}
