package com.tstu.productinfo.exception;

import com.tstu.commons.exception.ExceptionMessage;

public interface ProductInfoExceptionMessage extends ExceptionMessage {
    String PRODUCT_NOT_FOUND_MSG = "Продукт не найден";
    String PRODUCT_IS_ALREADY_IN_USE_MSG = "Продукт с этим именем уже существует";
    String CATEGORY_IS_ALREADY_IN_USE_MSG = "Категория с этим именем или псевдонимом уже существует";
    String REVIEW_SYSTEM_LINK_IS_ALREADY_IN_USE_MSG = "Ссылка на продукт с таким именем уже существует";
    String CATEGORY_NOT_FOUND_MSG = "Категория не найден";

}
