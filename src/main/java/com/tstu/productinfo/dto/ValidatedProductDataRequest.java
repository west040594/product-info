package com.tstu.productinfo.dto;

import com.tstu.commons.dto.http.request.productinfo.ProductDataRequest;
import com.tstu.productinfo.validation.UniqueProduct;
import com.tstu.productinfo.validation.UniqueReviewSystemLink;

import java.util.List;


public class ValidatedProductDataRequest extends ProductDataRequest {

    @Override
    @UniqueProduct
    public String getName() {
        return super.getName();
    }

    @Override
    public List<@UniqueReviewSystemLink String> getReviewSystemLinks() {
        return super.getReviewSystemLinks();
    }
}
