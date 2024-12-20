package ru.yandex.masterskaya.model.mapper;

import org.mapstruct.Mapper;
import ru.yandex.masterskaya.model.Review;
import ru.yandex.masterskaya.model.dto.CreateReviewDto;
import ru.yandex.masterskaya.model.dto.ReviewDto;
import ru.yandex.masterskaya.model.dto.ReviewFullDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    Review toReview(CreateReviewDto dto);

    ReviewDto toReviewDto(Review review);

    ReviewFullDto toReviewFullDto(Review review);


    List<ReviewFullDto> toListReviewFullDto(List<Review> reviews);
}