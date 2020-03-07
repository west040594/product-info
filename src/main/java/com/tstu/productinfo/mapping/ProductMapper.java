package com.tstu.productinfo.mapping;

import com.tstu.commons.constants.ReviewSystemsConstants;
import com.tstu.commons.dto.http.request.productinfo.ProductDataRequest;
import com.tstu.commons.dto.http.response.productinfo.ProductResponse;
import com.tstu.commons.exception.PrsHttpException;
import com.tstu.commons.mapping.DateMapper;
import com.tstu.commons.model.enums.ReviewSystem;
import com.tstu.productinfo.exception.ProductInfoErrors;
import com.tstu.productinfo.model.Product;
import com.tstu.productinfo.model.ReviewSystemLink;
import com.tstu.productinfo.service.CategoryService;
import com.tstu.productinfo.service.QualityService;
import org.mapstruct.*;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        uses = {DateMapper.class, ReviewMapper.class})
public interface ProductMapper {

    Pattern pattern = Pattern.compile("(^https://\\w+.+?)/");

    @Mappings({
            @Mapping(target = "name", source = "name"),
            @Mapping(target = "description", source = "description"),
            @Mapping(target = "averagePrice", source = "averagePrice"),
            @Mapping(target = "createdBy", source = "createdBy"),

            @Mapping(target = "id", ignore = true),
            @Mapping(target = "category", ignore = true),
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "lastModifyTime", ignore = true),
            @Mapping(target = "imageUrl", ignore = true),
            @Mapping(target = "rating", ignore = true),
            @Mapping(target = "fill", ignore = true),
            @Mapping(target = "reviews", ignore = true),
            @Mapping(target = "lastModifiedBy", ignore = true),
            @Mapping(target = "reviewSystemLinks", ignore = true),
    })
    Product productDataRequestToProduct(ProductDataRequest productDataRequest, @Context CategoryService categoryService);


    @Mappings({
            @Mapping(target = "name", source = "name"),
            @Mapping(target = "description", source = "description"),
            @Mapping(target = "averagePrice", source = "averagePrice"),
            @Mapping(target = "reviewSystemLinks", ignore = true),
            @Mapping(target = "category", ignore = true),
            @Mapping(target = "reviews", source = "reviews"),
            @Mapping(target = "fill", source = "fill"),
            @Mapping(target = "status", source = "status"),
            @Mapping(target = "createTime", source = "createTime"),
            @Mapping(target = "lastModifyTime", source = "lastModifyTime"),
            @Mapping(target = "createdBy", source = "createdBy"),
            @Mapping(target = "lastModifiedBy", source = "lastModifiedBy"),
    })
    ProductResponse productToProductResponse(Product product);


    @AfterMapping
    default void afterMappingProductDataRequestToProduct(ProductDataRequest productDataRequest,
                                                         @MappingTarget Product product,
                                                         @Context CategoryService categoryService) {
        Set<ReviewSystemLink> reviewSystemLinks = productDataRequest.getReviewSystemLinks().stream()
                .map(this::parseReviewSystemLink)
                .collect(Collectors.toSet());
        product.setReviewSystemLinks(reviewSystemLinks);

        product.setCategory(categoryService.findByName(productDataRequest.getCategory()));
    }


    default ReviewSystemLink parseReviewSystemLink(String link) {
        Matcher linkMatcher = pattern.matcher(link);
        String matchDomainName = null;
        while (linkMatcher.find()) {
            matchDomainName = linkMatcher.group(1);
        }
        if(matchDomainName != null && ReviewSystemsConstants.getReviewSystemUrlMap().containsKey(matchDomainName)) {
            ReviewSystem reviewSystem = ReviewSystemsConstants.getReviewSystemUrlMap().get(matchDomainName);
            return new ReviewSystemLink(link, reviewSystem);
        } else {
            throw new PrsHttpException(ProductInfoErrors.UNSUPPORTED_REVIEW_SYSTEM, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @AfterMapping
    default void afterMappingProductToProductResponse(Product product, @MappingTarget ProductResponse productResponse) {
        List<String> reviewLinks = product.getReviewSystemLinks().stream()
                .map(ReviewSystemLink::getName)
                .collect(Collectors.toList());

        productResponse.setReviewSystemLinks(reviewLinks);
        productResponse.setCategory(product.getCategory().getName());
    }
}
