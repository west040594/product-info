package com.tstu.productinfo.dto;

import com.tstu.commons.dto.http.request.productinfo.CategoryDataRequest;
import com.tstu.productinfo.validation.UniqueCategory;

@UniqueCategory
public class ValidatedCategoryDataRequest extends CategoryDataRequest {
}
