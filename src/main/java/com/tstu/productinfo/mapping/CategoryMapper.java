package com.tstu.productinfo.mapping;

import com.tstu.commons.dto.http.request.productinfo.CategoryDataRequest;
import com.tstu.commons.dto.http.response.productinfo.CategoryResponse;
import com.tstu.commons.mapping.DateMapper;
import com.tstu.productinfo.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring",
        uses = {DateMapper.class})
public interface CategoryMapper {

    @Mappings({
            @Mapping(target = "name", source = "name"),
            @Mapping(target = "alias", source = "alias"),
            @Mapping(target = "imageUrl", source = "imageUrl"),

            @Mapping(target = "id", ignore = true),
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "lastModifyTime", ignore = true),
    })
    Category categoryDataRequestToCategory(CategoryDataRequest categoryDataRequest);

    @Mappings({
            @Mapping(target = "name", source = "name"),
            @Mapping(target = "alias", source = "alias"),
            @Mapping(target = "imageUrl", source = "imageUrl"),
            @Mapping(target = "id",  source = "id"),
            @Mapping(target = "status",  source = "status"),
            @Mapping(target = "createTime",  source = "createTime"),
            @Mapping(target = "lastModifyTime",  source = "lastModifyTime"),
    })
    CategoryResponse categoryToCategoryResponse(Category category);
}
