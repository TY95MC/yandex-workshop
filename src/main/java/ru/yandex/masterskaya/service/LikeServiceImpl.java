package ru.yandex.masterskaya.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.masterskaya.exception.ConflictException;
import ru.yandex.masterskaya.exception.EntityNotFoundException;
import ru.yandex.masterskaya.model.Like;
import ru.yandex.masterskaya.model.Review;
import ru.yandex.masterskaya.model.dto.ReviewFullDto;
import ru.yandex.masterskaya.model.mapper.ReviewMapper;
import ru.yandex.masterskaya.repository.LikeRepository;
import ru.yandex.masterskaya.repository.ReviewRepository;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    @Transactional
    @Override
    public ReviewFullDto addLike(Long reviewId, Long userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Отзыв не найден"));

        if (!Objects.equals(review.getAuthorId(), userId)) {
            Optional<Like> existingLike = likeRepository.findByReviewIdAndUserId(reviewId, userId);
            if (existingLike.isEmpty()) {
                Like like = new Like();
                like.setReview(review);
                like.setUserId(userId);
                like.setLike(true);
                likeRepository.save(like);
            } else {
                throw new ConflictException("Этот пользователь уже ставил лайк :) данному отзыву");
            }
            return getReviewWithLikesAndDislikes(review.getId());

        } else {
            throw new ConflictException("Автор отзыва не может ставить себе лайк");
        }
    }

    @Transactional
    @Override
    public ReviewFullDto removeLike(Long reviewId, Long userId) {
        Optional<Like> existingLike = likeRepository.findByReviewIdAndUserId(reviewId, userId);
        if (existingLike.isPresent() && existingLike.get().isLike()) {
            likeRepository.deleteByReviewIdAndUserId(reviewId, userId);
        }

        return getReviewWithLikesAndDislikes(reviewId);
    }

    @Transactional
    @Override
    public ReviewFullDto addDislike(Long reviewId, Long userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Отзыв не найден"));

        if (!Objects.equals(review.getAuthorId(), userId)) {
            Optional<Like> existingLike = likeRepository.findByReviewIdAndUserId(reviewId, userId);
            if (existingLike.isPresent()) {
                if (existingLike.get().isLike()) {
                    likeRepository.deleteByReviewIdAndUserId(reviewId, userId);
                } else {
                    throw new ConflictException("Этот пользователь уже ставил дизлайк :( данному отзыву");
                }
            } else {
                Like dislike = new Like();
                dislike.setReview(review);
                dislike.setUserId(userId);
                dislike.setLike(false);
                likeRepository.save(dislike);
            }
        } else {
            throw new ConflictException("Автор отзыва не может ставить себе дизлайк");
        }

        return getReviewWithLikesAndDislikes(review.getId());
    }

    @Transactional
    @Override
    public ReviewFullDto removeDislike(Long reviewId, Long userId) {
        Optional<Like> existingLike = likeRepository.findByReviewIdAndUserId(reviewId, userId);
        if (existingLike.isPresent() && !existingLike.get().isLike()) {
            likeRepository.deleteByReviewIdAndUserId(reviewId, userId);
        }
        return getReviewWithLikesAndDislikes(reviewId);
    }

    @Transactional
    @Override
    public ReviewFullDto getReviewWithLikesAndDislikes(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Отзыв не найден"));
        Set<Like> likes = likeRepository.findByReviewId(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Лайк не найден"));
        Long likesCount = likes.stream().filter(Like::isLike).count();
        Long dislikesCount = likes.stream().filter(like -> !like.isLike()).count();

        ReviewFullDto reviewFullDto = reviewMapper.toReviewFullDto(review);
        reviewFullDto.setNumberOfLikes(likesCount);
        reviewFullDto.setNumberOfDisLikes(dislikesCount);

        return reviewFullDto;
    }
}

