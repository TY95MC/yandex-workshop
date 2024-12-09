package ru.yandex.masterskaya.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.yandex.masterskaya.client.EventClient;
import ru.yandex.masterskaya.client.RegistrationClient;
import ru.yandex.masterskaya.exception.ConflictException;
import ru.yandex.masterskaya.exception.EntityNotFoundException;
import ru.yandex.masterskaya.model.Review;
import ru.yandex.masterskaya.model.dto.CreateReviewDto;
import ru.yandex.masterskaya.model.dto.ReviewDto;
import ru.yandex.masterskaya.model.dto.ReviewFullDto;
import ru.yandex.masterskaya.model.dto.UpdateReviewDto;
import ru.yandex.masterskaya.model.dto.client.EventDto;
import ru.yandex.masterskaya.model.dto.client.RegistrationDto;
import ru.yandex.masterskaya.model.mapper.ReviewMapper;
import ru.yandex.masterskaya.repository.ReviewRepository;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final EventClient eventClient;
    private final RegistrationClient registrationClient;

    @Override
    public ReviewFullDto createReview(CreateReviewDto dto) {
        RegistrationDto registration = registrationClient.getStatusByEventIdAndUserId(dto.getEventId(), dto.getAuthorId());
        if (!registration.getStatus().equals("APPROVED")) {
            throw new ConflictException("Оставлять отзыв могут только лица участвовавшие в мероприятии");
        }

        ResponseEntity<EventDto> event = eventClient.getEventById(dto.getEventId());

        if (Objects.requireNonNull(event.getBody()).getOwnerId().equals(dto.getAuthorId())) {
            throw new ConflictException("Создатель мероприятия не может оставлять отзывы");
        }

        Review rev = reviewMapper.toReview(dto);
        rev.setCreatedDateTime(LocalDateTime.now());
        rev = reviewRepository.save(rev);
        return reviewMapper.toReviewFullDto(rev);
    }

    @Override
    public ReviewFullDto updateReview(Long authorId, Long reviewId, UpdateReviewDto dto) {
        Review rev = checkReviewOwner(authorId, reviewId);

        if (dto.getUsername() != null) {
            rev.setUsername(dto.getUsername());
        }

        if (dto.getTitle() != null) {
            rev.setTitle(dto.getTitle());
        }

        if (dto.getContent() != null) {
            rev.setContent(dto.getContent());
        }

        if (dto.getMark() != null) {
            rev.setMark(dto.getMark());
        }

        rev.setUpdatedDateTime(LocalDateTime.now());
        rev = reviewRepository.save(rev);
        return reviewMapper.toReviewFullDto(rev);
    }

    @Override
    public ReviewDto getReview(Long reviewId) {
        return reviewMapper.toReviewDto(findReview(reviewId));
    }

    @Override
    public Slice<ReviewDto> getReviews(int from, int size, Long eventId) {
        Pageable page = PageRequest.of(from, size, Sort.by("id").ascending());
        Slice<Review> reviews = reviewRepository.findReviews(eventId, page);
        return reviews.map(reviewMapper::toReviewDto);
    }

    @Override
    public void delete(Long authorId, Long reviewId) {
        Review rev = checkReviewOwner(authorId, reviewId);
        reviewRepository.delete(rev);
    }

    private Review findReview(Long reviewId) {
        return reviewRepository.findById(reviewId).orElseThrow(
                () -> new EntityNotFoundException("Review with id=" + reviewId + " was not found")
        );
    }

    private Review checkReviewOwner(Long authorId, Long reviewId) {
        Review rev = findReview(reviewId);

        if (!rev.getAuthorId().equals(authorId)) {
            throw new ConflictException("User with id=" + authorId + "is not review author");
        }

        return rev;
    }
}
