package ru.yandex.masterskaya.service;

import ru.yandex.masterskaya.model.dto.ReviewFullDto;

public interface LikeService {

    ReviewFullDto addLike(Long reviewId, Long userId);

    ReviewFullDto removeLike(Long reviewId, Long userId);

    ReviewFullDto getReviewWithLikesAndDislikes(Long reviewId);

    ReviewFullDto addDislike(Long reviewId, Long userId);

    ReviewFullDto removeDislike(Long reviewId, Long userId);
}
