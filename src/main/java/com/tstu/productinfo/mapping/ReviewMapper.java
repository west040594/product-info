package com.tstu.productinfo.mapping;

import com.google.common.collect.Sets;
import com.tstu.commons.dto.http.response.productinfo.ReviewResponse;
import com.tstu.commons.dto.rabbit.response.ReviewParseResponse;
import com.tstu.commons.mapping.DateMapper;
import com.tstu.commons.model.enums.QualityType;
import com.tstu.commons.model.enums.Status;
import com.tstu.productinfo.model.Quality;
import com.tstu.productinfo.model.Review;
import com.tstu.productinfo.service.QualityService;
import org.mapstruct.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        uses = {DateMapper.class})
public interface ReviewMapper {


    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "title", source = "title"),
            @Mapping(target = "body", source = "body"),
            @Mapping(target = "reviewerName", source = "username"),
            @Mapping(target = "readLink", source = "readLink"),
            @Mapping(target = "rating", source = "rating"),
            @Mapping(target = "postTime", source = "postTime"),
            @Mapping(target = "qualities", ignore = true),
            @Mapping(target = "product", ignore = true),
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "reviewSystem", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "lastModifyTime", ignore = true),
    })
    Review reviewParseResponseToReview(ReviewParseResponse reviewResponse, @Context QualityService qualityService);


    @Mappings({
            @Mapping(target = "title", source = "title"),
            @Mapping(target = "body", source = "body"),
            @Mapping(target = "reviewerName", source = "reviewerName"),
            @Mapping(target = "readLink", source = "readLink"),
            @Mapping(target = "rating", source = "rating"),
            @Mapping(target = "postTime", source = "postTime"),
            @Mapping(target = "pluses", ignore = true),
            @Mapping(target = "minuses", ignore = true),
            @Mapping(target = "status", source = "status"),
            @Mapping(target = "reviewSystem", source = "reviewSystem"),
            @Mapping(target = "createTime", source = "createTime"),
            @Mapping(target = "lastModifyTime", source = "lastModifyTime"),
    })
    ReviewResponse reviewToReviewResponse(Review review);

    @AfterMapping
    default void afterMappingReviewToReviewResponse(Review review,
                                                    @MappingTarget ReviewResponse reviewResponse) {

        List<String> pluses = review.getQualities().stream()
                .filter(quality -> quality.getType().equals(QualityType.PLUS))
                .map(Quality::getName)
                .collect(Collectors.toList());

        List<String> minuses = review.getQualities().stream()
                .filter(quality -> quality.getType().equals(QualityType.MINUS))
                .map(Quality::getName)
                .collect(Collectors.toList());

        reviewResponse.setPluses(pluses);
        reviewResponse.setMinuses(minuses);
    }


    @AfterMapping
    default void afterMappingReviewParseResponseToReview(ReviewParseResponse reviewResponse,
                                                         @MappingTarget Review review,
                                                         @Context QualityService qualityService) {
        Set<Quality> plusQualities = reviewResponse.getPluses().stream()
                .map(plus -> qualityService.find(plus, QualityType.PLUS))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());

        Set<Quality> minusQualities = reviewResponse.getMinuses().stream()
                .map(minus -> qualityService.find(minus, QualityType.MINUS))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());

        review.setQualities(Sets.union(plusQualities, minusQualities));
        review.setStatus(Status.ACTIVE);
    }
}
