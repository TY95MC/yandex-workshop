package ru.yandex.masterskaya.repository;

import ru.yandex.masterskaya.model.Review;
import ru.yandex.masterskaya.model.dto.ReviewStatisticDto;

import java.util.List;

public interface CustomReviewRepository {


    ReviewStatisticDto getStatistic(Long eventId);

    List<List<Review>> getTopReview(Long eventId);

    ReviewStatisticDto getStatisticByUser(Long authorId);
}
