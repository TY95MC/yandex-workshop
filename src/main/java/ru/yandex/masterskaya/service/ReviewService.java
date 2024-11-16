package ru.yandex.masterskaya.service;

import org.springframework.data.domain.Slice;
import ru.yandex.masterskaya.model.dto.CreateReviewDto;
import ru.yandex.masterskaya.model.dto.ReviewDto;
import ru.yandex.masterskaya.model.dto.ReviewFullDto;
import ru.yandex.masterskaya.model.dto.UpdateReviewDto;

public interface ReviewService {
    ReviewFullDto createReview(CreateReviewDto dto);

    ReviewFullDto updateReview(Long authorId, Long reviewId, UpdateReviewDto dto);

    ReviewDto getReview(Long reviewId);

    Slice<ReviewDto> getReviews(int page, int size, Long eventId);

    void delete(Long authorId, Long reviewId);
}
