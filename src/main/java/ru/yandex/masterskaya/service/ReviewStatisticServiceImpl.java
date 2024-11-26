package ru.yandex.masterskaya.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.masterskaya.model.Review;
import ru.yandex.masterskaya.model.dto.ReviewFullDto;
import ru.yandex.masterskaya.model.dto.ReviewStatisticDto;
import ru.yandex.masterskaya.model.dto.ReviewTopStatisticDto;
import ru.yandex.masterskaya.model.mapper.ReviewMapper;
import ru.yandex.masterskaya.repository.ReviewRepository;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
public class ReviewStatisticServiceImpl implements ReviewStatisticService {
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;


    @Override
    public ReviewStatisticDto getStatisticByEvent(Long eventId) {
        return reviewRepository.getStatistic(eventId);
    }

    @Override
    public ReviewTopStatisticDto getTopStatisticByEvent(Long eventId) {
        List<List<Review>> topReview = reviewRepository.getTopReview(eventId);
        List<ReviewFullDto> listBestReviewFullDto = reviewMapper.toListReviewFullDto(topReview.getFirst());
        List<ReviewFullDto> listBadReviewFullDto = reviewMapper.toListReviewFullDto(topReview.getLast());

        return ReviewTopStatisticDto.builder()
                .bestReview(listBestReviewFullDto)
                .badReview(listBadReviewFullDto)
                .build();
    }

    @Override
    public ReviewStatisticDto getStatisticByUser(Long authorId) {
        return reviewRepository.getStatisticByUser(authorId);
    }
}
